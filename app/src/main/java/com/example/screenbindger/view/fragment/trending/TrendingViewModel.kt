package com.example.screenbindger.view.fragment.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.util.constants.POSITION_TAB_MOVIES
import com.example.screenbindger.util.constants.POSITION_TAB_TV_SHOWS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrendingViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<TrendingViewState>,
    val viewAction: MutableLiveData<TrendingViewAction>,
    private val coroutineIo: CoroutineScope
) : ViewModel() {

    var currentPage: Int = 1

    fun fetchMovies() = coroutineIo.launch {
        remoteDataSource.getTrendingMovies(currentPage, viewState)
    }

    fun fetchTvShows() = coroutineIo.launch {
        remoteDataSource.getTrendingTvShows(currentPage, viewState)
    }

    fun getState(): TrendingViewState = viewState.value!!

    fun setAction(action: TrendingViewAction) {
        viewAction.postValue(action)
    }

    fun nextPage() {
        currentPage++
        when (viewState.value) {
            is TrendingViewState.Fetched.Movies -> fetchMovies()
            is TrendingViewState.Fetched.TvShows -> fetchTvShows()
            else -> return
        }
    }

    fun previousPage() {
        currentPage--
        when (viewState.value) {
            is TrendingViewState.Fetched.Movies -> fetchMovies()
            is TrendingViewState.Fetched.TvShows -> fetchTvShows()
            else -> return
        }
    }

    fun getDirection(showId: Int): NavDirections? = when (viewState.value) {
        is TrendingViewState.Fetched.Movies -> {
            TrendingFragmentDirections.actionTrendingFragmentToMovieDetailsFragment(showId)
        }
        is TrendingViewState.Fetched.TvShows -> {
            TrendingFragmentDirections.actionTrendingFragmentToTvShowDetailsFragment(showId)
        }
        else -> {
            null
        }
    }

    fun tabSelected(position: Int) {
        when (position) {
            POSITION_TAB_MOVIES -> setAction(TrendingViewAction.FetchMovies)
            POSITION_TAB_TV_SHOWS -> setAction(TrendingViewAction.FetchTvShows)
        }
    }

}