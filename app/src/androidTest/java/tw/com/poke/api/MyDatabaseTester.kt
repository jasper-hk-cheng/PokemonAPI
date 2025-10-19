package tw.com.poke.api

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tw.com.poke.api.resume.Resume

@RunWith(AndroidJUnit4::class)
class ResumeDaoTest {

    private lateinit var db: MyDatabase
    private lateinit var resumeDao: ResumeDao

    private val targetEntity = Resume(
        id = 1,
        name = "name 1",
        weight = 50,
        height = 120,
        photoUrl = "https://i.imgur.com/xL8Y8cs.jpeg",
        updateAt = System.currentTimeMillis(),
    )

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MyDatabase::class.java)
            .allowMainThreadQueries()
            .build().apply {
                resumeDao = getResumeDao()
            }
    }

    @Test
    fun testCheckInsertWithSelect() = runBlocking {
        val rowId = resumeDao.insertResume(targetEntity)
        assertNotEquals(0L, rowId)
        val resume = resumeDao.getResumeById(targetEntity.id)
        assertNotNull(resume)
        assertEquals(targetEntity.id, resume!!.id)
    }

    @Test
    fun testCheckInsertWithCount() = runBlocking {
        val rowId = resumeDao.insertResume(targetEntity)
        assertNotEquals(0L, rowId)
        val count = resumeDao.getCountById(targetEntity.id)
        assertEquals(1, count)
    }

    @After
    fun closeDB() {
        db.close()
    }
}
