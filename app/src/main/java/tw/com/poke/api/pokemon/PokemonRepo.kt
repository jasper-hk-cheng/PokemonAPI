package tw.com.poke.api.pokemon

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import tw.com.poke.api.PokemonService
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PokemonReopModule{

    @Singleton
    @Binds
    abstract fun providePokemonRepo(impl:PokemonRepoImpl): PokemonRepo
}

interface PokemonRepo {
    fun getPokemonList(): Flow<PagingData<PokemonListItem>>
    suspend fun getPokemonDetail(url: String): PokemonDetail
}

class PokemonRepoImpl @Inject constructor(
    private val pokemonService: PokemonService,
) : PokemonRepo {

    override fun getPokemonList(): Flow<PagingData<PokemonListItem>> = Pager(
        config = PagingConfig(
            initialLoadSize = 20,
            pageSize = 10,
            prefetchDistance = 3,
            enablePlaceholders = true,
        ),
        pagingSourceFactory = { PokemonPagingSource(pokemonService) },
    ).flow

    override suspend fun getPokemonDetail(url: String): PokemonDetail = pokemonService.getPokemonDetail(url)
}

class PokemonPagingSource(
    private val pokemonService: PokemonService,
) : PagingSource<Int, PokemonListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonListItem> = try {
        val page = params.key ?: 0
        val limit = params.loadSize
        val offset = page * limit
        val pokemonList = pokemonService.getPokemonList(offset, limit)
        LoadResult.Page(
            data = pokemonList.results,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (pokemonList.results.count() < limit) null else page + 1,
        )
    } catch (t: Throwable) {
        Timber.e(t)
        LoadResult.Error(t)
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonListItem>): Int? = state.anchorPosition?.let { pos: Int ->
        val page: LoadResult.Page<Int, PokemonListItem> = state.closestPageToPosition(pos) ?: return 0
        page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}
