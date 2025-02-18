package com.guicarneirodev.hoopreel.core.network.youtube

import com.guicarneirodev.hoopreel.core.network.youtube.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("videos")
    suspend fun getVideos(
        @Query("part") part: String = "snippet,statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("maxResults") maxResults: Int = 50,
        @Query("key") apiKey: String
    ): VideoResponse

    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 50,
        @Query("key") apiKey: String
    ): VideoResponse
}