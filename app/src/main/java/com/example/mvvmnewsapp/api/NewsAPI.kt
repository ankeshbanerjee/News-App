package com.example.mvvmnewsapp.api

import com.example.mvvmnewsapp.models.NewsResponse
import com.example.mvvmnewsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

public interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")countryCode: String = "us",
        @Query("page")pageNo: Int = 1,
        @Query("apiKey")apiKey: String = API_KEY
        ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")searchQuery: String,
        @Query("page")pageNo: Int = 1,
        @Query("apiKey")apiKey: String = API_KEY
    ): Response<NewsResponse>
}