package tw.com.poke.api.pokemon

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import tw.com.poke.api.resume.Resume

@JsonClass(generateAdapter = true)
data class PokemonList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>,
)

@Parcelize
data class PokemonListItem(
    val name: String,
    val url: String,
) : Parcelable

@JsonClass(generateAdapter = true)
data class PokemonDetail(
    val id: Int,
    val name: String,
    val order: Int,
    val weight: Int,
    @field:Json("is_default") val isDefault: Boolean,
    @field:Json("base_experience") val baseExperience: Int,
    val height: Int,
    @field:Json("location_area_encounters") val locationAreaEncounters: String,
    val sprites: Sprites,
) {
    companion object {
        fun getEmptyOne(): PokemonDetail = PokemonDetail(
            0,
            "",
            0,
            0,
            false,
            0,
            0,
            "",
            Sprites(""),
        )
    }

    fun toResume(): Resume = Resume(
        id = id,
        name = name,
        weight = weight,
        height = height,
        photoUrl = sprites.backDefault ?: "",
        updateAt = System.currentTimeMillis(),
    )
}

@JsonClass(generateAdapter = true)
data class Sprites(
    @field:Json("back_default") val backDefault: String?,
)
