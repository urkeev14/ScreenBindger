package com.example.screenbindger.di.dagger.main

import androidx.lifecycle.ViewModel
import com.example.screenbindger.di.dagger.ViewModelKey
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewModel
import com.example.screenbindger.view.fragment.genre_movies.GenreMoviesViewModel
import com.example.screenbindger.view.fragment.genres.GenresFragmentViewModel
import com.example.screenbindger.view.fragment.details.movie.MovieDetailsFragmentViewModel
import com.example.screenbindger.view.fragment.details.tv_show.TvShowDetailsViewModel
import com.example.screenbindger.view.fragment.profile.ProfileFragmentViewModel
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewModel
import com.example.screenbindger.view.fragment.trending.TrendingViewModel
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {


    @Binds
    @IntoMap
    @ViewModelKey(GenreMoviesViewModel::class)
    abstract fun bindGenreMoviesViewModel(viewModel: GenreMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GenresFragmentViewModel::class)
    abstract fun bindGenresViewModel(viewModel: GenresFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsFragmentViewModel::class)
    abstract fun bindMovieDetailsViewModel(viewModel: MovieDetailsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TvShowDetailsViewModel::class)
    abstract fun bindTvShowDetailsViewModel(viewModel: TvShowDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileFragmentViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel::class)
    abstract fun bindTrendingViewModel(viewModel: TrendingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingViewModel::class)
    abstract fun bindUpcomingViewModel(viewModel: UpcomingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindFavoriteMoviesViewModel(viewModel: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewFragmentViewModel::class)
    abstract fun bindReviewFragmentViewModel(viewModel: ReviewFragmentViewModel): ViewModel
}