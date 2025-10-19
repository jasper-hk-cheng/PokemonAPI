package tw.com.poke.api.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tw.com.poke.api.resume.Resume
import tw.com.poke.api.resume.ResumeRepo
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokemonRepo: PokemonRepo,
    private val resumeRepo: ResumeRepo,
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow(PokemonDetail.getEmptyOne())
    val pokemonDetail = _pokemonDetail.asStateFlow()

    fun getPokemonDetail(url: String) {
        viewModelScope.launch {
            val pokemonDetail = pokemonRepo.getPokemonDetail(url)
            recordResume(pokemonDetail.toResume())
            _pokemonDetail.emit(pokemonDetail)
        }
    }

    fun recordResume(resume: Resume) {
        viewModelScope.launch {
            if (resumeRepo.getCountById(resume.id) > 0) {
                resumeRepo.refreshUpdateAt(System.currentTimeMillis(), resume.id)
            } else {
                val result = resumeRepo.insertResume(resume)
                check(result != 0L)
            }
        }
    }
}
