package tw.com.poke.api.resume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.flowOf

@AndroidEntryPoint
class ResumeListFragment : Fragment() {

    private val viewModel: ResumeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            ResumeListTemplate(viewModel)
        }
    }
}

@Composable
fun ResumeListTemplate(
    viewModel: ResumeViewModel,
    modifier: Modifier = Modifier,
) {
    val resumeLazyPagingItems = viewModel.allResume.collectAsLazyPagingItems()

    ResumeList(
        resumeLazyPagingItems = resumeLazyPagingItems,
        isRefreshing = viewModel.isRefreshing.collectAsState(),
        onLazyPagingItemsRefresh = { viewModel.refresh(resumeLazyPagingItems) },
        onClearAllResume = { viewModel.clearAllResume() },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResumeList(
    resumeLazyPagingItems: LazyPagingItems<Resume>,
    isRefreshing: State<Boolean>,
    onLazyPagingItemsRefresh: (LazyPagingItems<Resume>) -> Unit,
    onClearAllResume: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            onLazyPagingItemsRefresh.invoke(resumeLazyPagingItems)
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Button(
            onClick = { onClearAllResume.invoke() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("清空資料")
        }
        PullRefreshIndicator(isRefreshing.value, pullRefreshState, modifier = Modifier.align(Alignment.CenterHorizontally))
        LazyColumn(Modifier.fillMaxSize()) {
            items(count = resumeLazyPagingItems.itemCount) { index: Int ->
                resumeLazyPagingItems[index]?.let { resume ->
                    ResumeListItem(resume)
                    if (index < resumeLazyPagingItems.itemCount - 1) {
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(color = Color.LightGray)
                        )
                    }
                }
            }
            resumeLazyPagingItems.apply {
                when {
                    loadState.append.endOfPaginationReached -> {
                        item {
                            Text("已無更多資料", modifier = Modifier.padding(4.dp))
                        }
                    }

                    loadState.refresh is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(Modifier.padding(4.dp))
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ResumeListPreview() {
    val mockItems = listOf(
        Resume.getEmptyOne(),
        Resume.getEmptyOne(),
        Resume.getEmptyOne(),
        Resume.getEmptyOne(),
    )
    val pagingData: PagingData<Resume> = PagingData.from(mockItems)
    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()
    ResumeList(
        resumeLazyPagingItems = lazyPagingItems,
        isRefreshing = remember { mutableStateOf(false) },
        onLazyPagingItemsRefresh = {},
        onClearAllResume = {},
        modifier = Modifier,
    )
}

@Composable
fun ResumeListItem(
    resume: Resume,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = resume.photoUrl,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .weight(1f)
                .height(100.dp)
        ) {
            Text("id: ${resume.id}", fontSize = 20.sp)
            Text(resume.name, fontSize = 20.sp)
        }
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .weight(1f)
                .height(100.dp)
        ) {
            Text("weight: ${resume.weight}", fontSize = 15.sp)
            Text("height: ${resume.height}", fontSize = 15.sp)
            Text("update at: ${resume.getFormattedUpdateAt()}", fontSize = 15.sp)
        }
    }
}

@Preview
@Composable
fun ResumeListItemPreview() {
    ResumeListItem(
        resume = Resume(
            id = 10,
            name = "name content text",
            weight = 100,
            height = 150,
            photoUrl = "url content text",
            updateAt = System.currentTimeMillis(),
        ),
        modifier = Modifier,
    )
}
