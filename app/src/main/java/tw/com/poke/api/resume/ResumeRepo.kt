package tw.com.poke.api.resume

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import tw.com.poke.api.ResumeDao
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResumeRepoModule {

    @Singleton
    @Binds
    abstract fun provideResumeRepo(impl: ResumeRepoImpl): ResumeRepo
}

interface ResumeRepo {
    fun selectAllResumes(): Flow<PagingData<Resume>>
    suspend fun getCountById(id: Int): Int
    suspend fun insertResume(resume: Resume): Long
    suspend fun refreshUpdateAt(updateAt: Long, id: Int)
    suspend fun clearAll()
}

class ResumeRepoImpl @Inject constructor(
    private val resumeDao: ResumeDao,
) : ResumeRepo {

    override fun selectAllResumes(): Flow<PagingData<Resume>> = Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 5,
            prefetchDistance = 3,
            enablePlaceholders = true,
        ),
        pagingSourceFactory = { resumeDao.selectAllResumes() }
    ).flow

    override suspend fun getCountById(id: Int): Int = resumeDao.getCountById(id)
    override suspend fun insertResume(resume: Resume): Long = resumeDao.insertResume(resume)
    override suspend fun refreshUpdateAt(updateAt: Long, id: Int) {
        resumeDao.refreshUpdateAt(updateAt, id)
    }

    override suspend fun clearAll() {
        resumeDao.clearAll()
    }
}
