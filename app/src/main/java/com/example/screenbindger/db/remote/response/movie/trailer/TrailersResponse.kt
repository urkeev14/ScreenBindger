package com.example.screenbindger.db.remote.response.movie.trailer

import com.google.gson.annotations.SerializedName

data class TrailersResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val list: List<TrailerDetails>
) {
}