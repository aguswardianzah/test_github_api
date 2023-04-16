package com.agusw.test_github_api.api

import com.agusw.test_github_api.models.RepoResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/repositories")
    suspend fun searchRepo(@Query("q") query: String = ""): RepoResult
}