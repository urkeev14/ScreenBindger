package com.example.screenbindger.model.domain.review
import com.google.gson.annotations.SerializedName

data class ReviewEntity(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("author_details")
    val authorDetails: ReviewAuthorEntity? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("url")
    val url: String? = null
)