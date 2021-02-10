package com.example.screenbindger.db.remote.repo

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.FavoriteMovieRequestBody
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.response.*
import com.example.screenbindger.db.remote.service.auth.firebase.AuthService
import com.example.screenbindger.db.remote.service.auth.tmdb.TmdbAuthService
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.db.remote.service.storage.StorageService
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.db.remote.service.user.UserService
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.view.fragment.login.AuthorizationEventObservable
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewState
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import retrofit2.Response
import javax.inject.Inject

class ScreenBindgerRemoteDataSource
@Inject constructor(
    private val session: Session,
    private val movieService: MovieService,
    private val genreService: GenreService,
    private val tmdbAuthService: TmdbAuthService,
    private val authService: AuthService,
    private val userService: UserService,
    private val storageService: StorageService

) {

    suspend fun login(
        user: UserEntity,
        authStateObservable: AuthorizationEventObservable
    ) {
        authService.signIn(user.email, user.password, authStateObservable)
    }

    suspend fun register(
        user: UserEntity,
        authStateObservable: AuthorizationEventObservable
    ) {
        authService.signUp(user.email, user.password, authStateObservable)
    }

    suspend fun create(userStateObservable: UserStateObservable) {
        userService.create(userStateObservable)
    }

    suspend fun getTrending(trendingViewState: MutableLiveData<TrendingViewState>) {
        movieService.getTrending(trendingViewState)
    }

    suspend fun getUpcoming(upcomingViewState: MutableLiveData<UpcomingViewState>){
        movieService.getUpcoming(upcomingViewState)
    }

    suspend fun getGenres(): Response<GenresResponse> {
        return genreService.getAll()
    }

    suspend fun getMovieDetails(
        movieId: Int,
        viewState: MutableLiveData<MovieDetailsViewState>
    ) {
        movieService.getMovieDetails(movieId, viewState)
    }

    suspend fun getMovieCasts(
        movieId: Int,
        viewState: MutableLiveData<MovieDetailsViewState>
    ) {
        movieService.getMovieCasts(movieId, viewState)
    }

    suspend fun getMoviesByGenre(id: String): Response<GenreMoviesResponse> {
        return genreService.getMoviesByGenre(id)
    }

    suspend fun getRequestToken(
        authStateObservable: AuthorizationEventObservable
    ) {
        tmdbAuthService.getRequestToken(authStateObservable)
    }

    suspend fun createSession(authStateObservable: AuthorizationEventObservable) {
        tmdbAuthService.createSession(authStateObservable)
    }

    suspend fun getAccountDetails(authStateObservable: AuthorizationEventObservable) {
        tmdbAuthService.getAccountDetails(session, authStateObservable)
    }

    suspend fun postMovieAsFavorite(
        favoriteMovieRequestBody: FavoriteMovieRequestBody
    ): Response<FavoriteMovieResponse> {
        return movieService.postMovieAsFavorite(
            sessionId = session.id!!,
            favoriteMovieRequestBody = favoriteMovieRequestBody
        )
    }

    suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    ) {
        authService.changePassword(newPassword, userStateObservable)
    }

    suspend fun fetchUser(userStateObservable: UserStateObservable) {
        userService.read(userStateObservable)
    }

    suspend fun updateUser(userStateObservable: UserStateObservable) {
        userService.update(userStateObservable)
    }

    suspend fun uploadImage(uri: Uri, userStateObservable: UserStateObservable) {
        storageService.uploadImage(uri, userStateObservable)
    }

    suspend fun fetchProfilePicture(userStateObservable: UserStateObservable) {
        storageService.downloadImage(userStateObservable)
    }

    fun setSession(createdSession: Session) {
        this.session.apply {
            accountId = createdSession.accountId
            id = createdSession.id
            expiresAt = createdSession.expiresAt
        }
    }

    fun getDetails(): String {
        return "SessionID: $session.id\n AccountID: ${session.accountId}"
    }

}