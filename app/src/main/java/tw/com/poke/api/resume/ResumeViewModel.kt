package tw.com.poke.api.resume

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val resumeRepo: ResumeRepo,
) : ViewModel() {

    val allResume = resumeRepo.selectAllResumes()
        .cachedIn(viewModelScope)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun clearAllResume() {
        viewModelScope.launch {
            resumeRepo.clearAll()
        }
    }

    fun refresh(lazyPagingItems: LazyPagingItems<Resume>) {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            lazyPagingItems.refresh()
            _isRefreshing.emit(false)
        }
    }
}
