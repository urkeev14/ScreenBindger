package com.example.screenbindger.db.remote.response.movie

import com.example.screenbindger.model.domain.movie.MovieEntity
import com.google.gson.annotations.SerializedName

class TrendingMoviesResponse
constructor(
    @SerializedName("results")
    val list: List<MovieEntity>
)