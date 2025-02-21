package com.guicarneirodev.hoopreel.core.network.youtube

import com.guicarneirodev.hoopreel.core.BuildConfig
import com.guicarneirodev.hoopreel.core.network.youtube.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("videos")
    suspend fun getVideos(
        @Query("part") part: String = "snippet,statistics",
        @Query("id") id: String,
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): VideoResponse

    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("videoDuration") videoDuration: String = "medium",
        @Query("relevanceLanguage") relevanceLanguage: String = "en",
        @Query("safeSearch") safeSearch: String = "moderate",
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("order") order: String = "viewCount",
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): VideoResponse
}