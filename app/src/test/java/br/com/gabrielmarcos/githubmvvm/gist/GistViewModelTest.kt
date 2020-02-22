package br.com.gabrielmarcos.githubmvvm.gist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.gabrielmarcos.githubmvvm.core.TrampolineSchedulerRule
import br.com.gabrielmarcos.githubmvvm.model.Gist
import br.com.gabrielmarcos.githubmvvm.utils.favExpectedResponse
import br.com.gabrielmarcos.githubmvvm.utils.gistExpectedResponse
import br.com.gabrielmarcos.githubmvvm.utils.starredGistExpectedValue
import br.com.gabrielmarcos.githubmvvm.utils.unstarredGistExpectedValue
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.Single.just
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
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

    private lateinit var testObserver: TestObserver<Single<List<Gist>>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = GistViewModel(gistRepository)
        testObserver = TestObserver()
    }

    @Test
    fun `when remote gist list response success then assert no errors`() {
        `when`(gistRepository.getGistList(0, true)).thenReturn(just(gistExpectedResponse))
        `when`(gistRepository.getSavedFavoriteGist()).thenReturn(favExpectedResponse)
        `when`(gistRepository.saveLocalGist(emptyList())).thenReturn(Completable.complete())

        viewModel.connectionAvailability = true
        viewModel.getGistList()

        verify(gistRepository, times(1)).getGistList(0, true)
        verify(gistRepository, times(1)).getSavedFavoriteGist()
        verify(gistRepository, times(1)).saveLocalGist(emptyList())

        testObserver.assertNoErrors()
    }

    @Test
    fun `when local favorite gist list response success then assert that favorite id list has the same size`() {
        `when`(gistRepository.getSavedFavoriteGist()).thenReturn(favExpectedResponse)
        `when`(gistRepository.saveLocalGist(emptyList())).thenReturn(Completable.complete())
        viewModel.connectionAvailability = true
        viewModel.getLocalFavoriteList()

        verify(gistRepository, times(1)).getSavedFavoriteGist()
        Assert.assertTrue(viewModel.favIdList.size == 3)
        testObserver.assertNoErrors()
    }

    @Test
    fun `when remote gist response success then assert no errors`() {
        `when`(gistRepository.getGist("", true)).thenReturn(just(starredGistExpectedValue))
        viewModel.connectionAvailability = true
        viewModel.getGist("")

        verify(gistRepository, times(1)).getGist("", true)

        testObserver.assertNoErrors()
        testObserver.assertNoErrors()
    }

    @Test
    fun `when save local response success then assert no errors`() {
        `when`(gistRepository.saveLocalGist(emptyList())).thenReturn(Completable.complete())
        viewModel.connectionAvailability = true
        viewModel.saveLocalResponse(emptyList())

        verify(gistRepository, times(1)).saveLocalGist(emptyList())

        testObserver.assertNoErrors()
    }

    @Test
    fun `when gist was not starred then added gist in local database`() {
        `when`(gistRepository.setFavoriteGist(starredGistExpectedValue))
            .thenReturn(Completable.complete())
        viewModel.connectionAvailability = true
        viewModel.handleFavoriteState(starredGistExpectedValue)

        verify(gistRepository, times(1)).setFavoriteGist(starredGistExpectedValue)
        verify(gistRepository, times(0)).deletFavoriteGistById("")

        testObserver.assertNoErrors()
    }

    @Test
    fun `when gist was  starred then delete gist in local database`() {
        `when`(gistRepository.deletFavoriteGistById(unstarredGistExpectedValue.gistId)).thenReturn(
            Completable.complete()
        )
        viewModel.connectionAvailability = true
        viewModel.handleFavoriteState(unstarredGistExpectedValue)

        verify(gistRepository, times(0)).setFavoriteGist(unstarredGistExpectedValue)
        verify(gistRepository, times(1)).deletFavoriteGistById(unstarredGistExpectedValue.gistId)

        testObserver.assertNoErrors()
    }
}