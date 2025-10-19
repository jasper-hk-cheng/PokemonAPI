package tw.com.poke.api.resume

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat

@Entity(tableName = "pokemon_resume")
@JsonClass(generateAdapter = true)
data class Resume(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val photoUrl: String,
    val updateAt: Long,
) {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        fun getEmptyOne(): Resume = Resume(
            id = 0,
            name = "",
            weight = 0,
            height = 0,
            photoUrl = "",
            updateAt = System.currentTimeMillis(),
        )
    }

    fun getFormattedUpdateAt(): String = dateFormat.format(updateAt)
}
