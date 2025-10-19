package tw.com.poke.api.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private val navController by lazy { findNavController() }
    private val navArgs: PokemonDetailFragmentArgs by navArgs()

    private val viewModel: PokemonDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PokemonDetailTemplate(navArgs.pokemon.url, viewModel, onBackPress = {
                    navController.popBackStack()
                })
            }
        }
}

@Composable
fun PokemonDetailTemplate(
    url: String,
    viewModel: PokemonDetailViewModel,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        viewModel.getPokemonDetail(url)
    }
    PokemonDetailScreen(
        url = url,
        pokemonDetailState = viewModel.pokemonDetail.collectAsState(),
        onBackPress = onBackPress,
        modifier = modifier,
    )
}


@Composable
fun PokemonDetailScreen(
    url: String,
    pokemonDetailState: State<PokemonDetail>,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val detail = pokemonDetailState.value

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxSize(),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AsyncImage(
                model = detail.sprites.backDefault,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
            ) {
                Text("id: ${detail.id}", fontSize = 20.sp)
                Text("name: ${detail.name}", fontSize = 20.sp)
                Text("order: ${detail.order}", fontSize = 20.sp)
            }
        }
        Text("weight: ${detail.weight}", fontSize = 20.sp)
        Text("is default: ${detail.isDefault}", fontSize = 20.sp)
        Text("base experience: ${detail.baseExperience}", fontSize = 20.sp)
        Text("height: ${detail.height}", fontSize = 20.sp)
        Text("location area encounters: ${detail.locationAreaEncounters}", fontSize = 20.sp)
    }
    BackHandler {
        onBackPress.invoke()
    }
}

@Preview
@Composable
fun PokemonDetailScreenPreview() {
    val pokemonDetailState = remember {
        mutableStateOf(
            PokemonDetail(
                id = 0,
                name = "name content text",
                order = 100,
                weight = 80,
                isDefault = false,
                baseExperience = 1000,
                height = 150,
                locationAreaEncounters = "locationAreaEncounters content text",
                sprites = Sprites(
                    backDefault = "photoUrl content text"
                )
            )
        )
    }
    PokemonDetailScreen(
        url = "",
        pokemonDetailState = pokemonDetailState,
        onBackPress = {},
        modifier = Modifier,
    )
}
