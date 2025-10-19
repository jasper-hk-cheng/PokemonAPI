package tw.com.poke.api.pokemon

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tw.com.poke.api.databinding.FragmentPokemonListBinding

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private var viewBinding: FragmentPokemonListBinding? = null

    private val navController by lazy { findNavController() }

    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewBinding = FragmentPokemonListBinding.inflate(inflater, container, false).apply {
            viewBinding = this
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PokemonListAdapter { clickedPokemonItem ->
            val action = PokemonListFragmentDirections.actionListToItem(clickedPokemonItem)
            navController.navigate(action)
        }
        viewBinding?.rvPokemon?.let {
            it.adapter = adapter
            it.addItemDecoration(MyItemDecoration(16))
        }
        lifecycleScope.launch {
            viewModel.pokemonListFlow.collectLatest { pagingData: PagingData<PokemonListItem> ->
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }
}

class PokemonListAdapter(
    private val onItemClick: (PokemonListItem) -> Unit,
) : PagingDataAdapter<PokemonListItem, PokemonListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val composeView = ComposeView(parent.context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
        return ViewHolder(composeView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { pokemonListItem ->
            holder.composeView.setContent {
                PokemonItemScreen(pokemonListItem, onItemClick)
            }
        }
    }

    class ViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    object DiffCallback : DiffUtil.ItemCallback<PokemonListItem>() {
        override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean = oldItem.name == newItem.name && oldItem.url == newItem.url
    }
}

class MyItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = space
    }
}

@Composable
fun PokemonItemScreen(
    pokemonListItem: PokemonListItem,
    onItemClick: (PokemonListItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpend by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp))
            .padding(PaddingValues(12.dp))
            .combinedClickable(
                onClick = { onItemClick.invoke(pokemonListItem) },
                onLongClick = { isExpend = !isExpend },
            )
    ) {
        Text(pokemonListItem.name)
        if (isExpend) {
            Spacer(Modifier.size(2.dp))
            Text(pokemonListItem.url)
        }
    }
}

@Preview
@Composable
fun PokemonItemScreenPreview() {
    PokemonItemScreen(
        pokemonListItem = PokemonListItem(name = "list item name", url = "list item url"),
        onItemClick = {},
        modifier = Modifier,
    )
}
