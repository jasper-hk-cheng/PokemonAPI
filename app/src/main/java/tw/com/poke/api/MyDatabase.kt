package tw.com.poke.api

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import tw.com.poke.api.resume.Resume
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyDatabaseModule {

    @Singleton
    @Provides
    fun provideMyDatabase(@ApplicationContext context: Context): MyDatabase =
        Room.databaseBuilder(context, MyDatabase::class.java, MyDatabase::class.java.simpleName)
            .setQueryCallback(
                object : RoomDatabase.QueryCallback {
                    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                        Timber.i("sqlQuery: $sqlQuery, bindArgs: $bindArgs")
                    }

                }, Executors.newSingleThreadExecutor()
            )
            .build()

    @Singleton
    @Provides
    fun provideResumeDao(database: MyDatabase): ResumeDao = database.getResumeDao()
}

@Database(entities = [Resume::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getResumeDao(): ResumeDao
}

@Dao
interface ResumeDao {

    @Query("select * from pokemon_resume order by id")
    fun selectAllResumes(): PagingSource<Int, Resume>

    @Query("select count(*) from pokemon_resume where id = :id")
    suspend fun getCountById(id: Int): Int

    @Query("select * from pokemon_resume where id =:id")
    suspend fun getResumeById(id: Int): Resume?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: Resume): Long

    @Query("update pokemon_resume set updateAt = :updateAt where id = :id")
    suspend fun refreshUpdateAt(updateAt: Long, id: Int)

    @Query("delete from pokemon_resume")
    suspend fun clearAll()
}
