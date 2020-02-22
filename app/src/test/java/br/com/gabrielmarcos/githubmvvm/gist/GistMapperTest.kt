package br.com.gabrielmarcos.githubmvvm.gist

import br.com.gabrielmarcos.githubmvvm.utils.favIdsExpectedValue
import br.com.gabrielmarcos.githubmvvm.utils.gistExpectedResponse
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GistMapperTest {

    @Test
    fun `when response mapped assert that gist list has favorite gist`() {
        val mappedValueWithFavorites = GistMapper.map(
            gistExpectedResponse, favIdsExpectedValue
        )
        mappedValueWithFavorites.forEach {
            Assert.assertTrue(it.starred)
        }
    }

    @Test
    fun `when favIds is empty assert that return gist list`() {
        val mappedValueWithFavorites = GistMapper.map(
            gistExpectedResponse, emptyList()
        )

        Assert.assertTrue(mappedValueWithFavorites == gistExpectedResponse)
    }
}
