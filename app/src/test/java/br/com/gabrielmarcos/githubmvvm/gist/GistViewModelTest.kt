package br.com.gabrielmarcos.githubmvvm.gist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.gabrielmarcos.githubmvvm.core.TrampolineSchedulerRule
import br.com.gabrielmarcos.githubmvvm.model.FavModel
import br.com.gabrielmarcos.githubmvvm.utils.getOrAwaitValue
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
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


    private var favLocalResponse: Single<List<FavModel>>? = null

    private lateinit var viewModel: GistViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = GistViewModel(gistRepository)
        favLocalResponse = Single.just(listOf(FavModel(0, "")))
    }

    @Test
    fun `when get list of gist thows exception then asset showError is not null`() {
        `when`(gistRepository.getSavedFavoriteGist()).thenThrow(RuntimeException())
        viewModel.connectionAvailability = true
        viewModel.getLocalFavoriteList()

        viewModel.showSnackbarMessage.getOrAwaitValue().run {
            Assert.assertNotNull(getContentIfNotHandled())
        }

        viewModel.resultError.getOrAwaitValue().run {
            Assert.assertNotNull(getContentIfNotHandled())
        }
    }
}