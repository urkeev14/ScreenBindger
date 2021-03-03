package com.example.screenbindger.db.remote.service.tv_show

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import com.example.screenbindger.view.fragment.details.ShowDetailsState
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvShowService @Inject constructor(
    private val api: TvShowApi
) {
    suspend fun getUpcoming(viewState: MutableLiveData<UpcomingViewState>) {
        api.getUpcoming().let { response ->
            var state: UpcomingViewState? = null
            if (response.isSuccessful) {
                val list = response.body()?.list?.sortedByDescending { it.rating } ?: emptyList()

                state = if (response.isSuccessful) {
                    list.generateGenres()
                    UpcomingViewState(ListState.Fetched, list)
                } else {
                    val message = response.getErrorResponse().statusMessage
                    UpcomingViewState(ListState.NotFetched(Event(message)), null)
                }
            } else {
                val message = response.getErrorResponse().statusMessage
                UpcomingViewState(ListState.NotFetched(Event(message)), null)
            }
            viewState.postValue(state)
        }
    }

    suspend fun getTrending(viewState: MutableLiveData<TrendingFragmentViewState>) {
        api.getTrending().let { response ->
            var state: TrendingFragmentViewState? = null
            if (response.isSuccessful) {
                val list = response.body()?.list?.sortedByDescending { it.rating } ?: emptyList()

                state = if (response.isSuccessful) {
                    list.generateGenres()
                    TrendingFragmentViewState(ListState.Fetched, list)
                } else {
                    val message = response.getErrorResponse().statusMessage
                    TrendingFragmentViewState(ListState.NotFetched(Event(message)), null)
                }
            } else {
                val message = response.getErrorResponse().statusMessage
                TrendingFragmentViewState(ListState.NotFetched(Event(message)), null)
            }
            viewState.postValue(state)
        }
    }

    suspend fun getDetails(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        api.getDetails(showId).also { response ->
            if (response.isSuccessful) {
                viewState.apply {
                    show = response.body()
                    CoroutineScope(Dispatchers.Default).launch {
                        show?.generateGenreString()
                    }
                }

                val isCastsProcessedOrNotFetched =
                    (viewState.currentState() is ShowDetailsState.CastsProcessed)
                        .or(viewState.currentState() is ShowDetailsState.Error.CastsNotFetched)

                if (isCastsProcessedOrNotFetched) {
                    viewState.prepareForFinalState()
                } else {
                    viewState.setState(ShowDetailsState.ShowProcessed)
                }
            } else {
                val message = response.getErrorResponse().statusMessage
                viewState.setState(ShowDetailsState.Error.ShowNotFetched(message))
            }
        }
    }

    suspend fun getCasts(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        api.getCasts(showId).also { response ->
            if (response.isSuccessful) {
                viewState.casts = response.body()?.casts

                val movieProcessedOrNotFetched: Boolean =
                    (viewState.currentState() is ShowDetailsState.ShowProcessed)
                        .or(viewState.currentState() is ShowDetailsState.Error.ShowNotFetched)


                if (movieProcessedOrNotFetched) {
                    viewState.prepareForFinalState()
                } else {
                    viewState.setState(ShowDetailsState.CastsProcessed)
                }

            } else {
                val message = response.getErrorResponse().statusMessage
                viewState.setState(ShowDetailsState.Error.CastsNotFetched(message))
            }
        }
    }

    suspend fun getTvShowTrailers(
        tvShowId: Int,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        api.getTrailers(tvShowId).let { response ->
            if (response.isSuccessful) {
                response.body()?.list?.let { list ->
                    if (list.isNotEmpty()) {
                        viewEvent.postValue(Event(DetailsFragmentViewEvent.TrailersFetched(list)))
                    } else {
                        viewEvent.postValue(Event(DetailsFragmentViewEvent.TrailersNotFetched))
                    }
                }
            } else {
                viewEvent.postValue(Event(DetailsFragmentViewEvent.TrailersNotFetched))
            }
        }
    }

    suspend fun postMovieAsFavorite(
        session: Session,
        body: MarkAsFavoriteRequestBody,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {

        ifLet(session.id, session.accountId) {
            api.postMarkAsFavorite(
                sessionId = session.id!!,
                accountId = session.accountId!!,
                body = body
            ).let { response ->
                if (response.isSuccessful) {
                    val isAddedToFavorites = body.favorite

                    if (isAddedToFavorites)
                        viewEvent.postValue(Event(DetailsFragmentViewEvent.AddedToFavorites()))
                    else
                        viewEvent.postValue(Event(DetailsFragmentViewEvent.RemovedFromFavorites()))
                } else {
                    val error = response.getErrorResponse().statusMessage
                    viewEvent.postValue(Event(DetailsFragmentViewEvent.Error(error)))
                }
            }
        }

    }

    suspend fun getIsTvShowFavorite(
        showId: Int,
        session: Session,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            api.getFavoriteTvShowList(
                sessionId = session.id!!,
                accountId = session.accountId!!
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.list?.forEach { show ->
                        if (show.id!! == showId) {
                            viewEvent.postValue(Event(DetailsFragmentViewEvent.IsLoadedAsFavorite))
                            return
                        }
                    }
                    viewEvent.postValue(Event(DetailsFragmentViewEvent.IsLoadedAsNotFavorite))
                } else {
                    viewEvent.postValue(
                        Event(
                            DetailsFragmentViewEvent.Error(
                                "Error finding out if this is you favorite movie :("
                            )
                        )
                    )
                }
            }
        }
    }
}