package com.agusw.test_github_api.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class Repos(
    val id: Int = 0,
    val full_name: String? = "",
    val html_url: String? = "",
    val description: String? = "",
    val owner: ReposOwner? = null
)

@JsonClass(generateAdapter = true)
@Keep
data class ReposOwner(
    val id: Int = 0,
    val avatar_url: String? = null,
    val login: String? = ""
)

@JsonClass(generateAdapter = true)
@Keep
data class RepoResult(
    val items: List<Repos> = emptyList()
)