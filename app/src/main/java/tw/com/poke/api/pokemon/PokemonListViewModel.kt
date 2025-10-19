package tw.com.poke.api.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepo: PokemonRepo,
) : ViewModel() {

    val pokemonListFlow = pokemonRepo.getPokemonList().cachedIn(viewModelScope)
}
