package br.com.gabrielmarcos.githubmvvm.gist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.gabrielmarcos.githubmvvm.core.TrampolineSchedulerRule
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.model.Gist
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyList
import org.mockito.Mockito.doNothing
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GistViewModelTest {

    @get:Rule
    val trampolineSchedulerRule = TrampolineSchedulerRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var gistRepository: GistRepository

    private lateinit var viewModel: GistViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = GistViewModel(gistRepository)
    }

    @Test
    fun `when get list of gist response success then return a list of favorite gist`() {
        val viewModelSpy = Mockito.spy(viewModel)
        val expectedResponse = Single.just(
            listOf(
                FavModel(0, ""),
                FavModel(0, ""),
                FavModel(0, "")
            )
        )

        `when`(gistRepository.getSavedFavoriteGist()).thenReturn(expectedResponse)
        doNothing().`when`(viewModelSpy).saveLocalResponse(emptyList())

        viewModel.connectionAvailability = true
        viewModel.getLocalFavoriteList()

        Assert.assertTrue(viewModel.favIdList.size == 2)
    }
}