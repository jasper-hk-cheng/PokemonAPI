package tw.com.poke.api

import junit.framework.TestCase.assertEquals
import org.junit.Test
import tw.com.poke.api.pokemon.PokemonDetail

class PokemonEntityTester {
    @Test
    fun testToResume() {
        val emptyResume = PokemonDetail.getEmptyOne().toResume()
        assertEquals(0, emptyResume.id)
    }
}
