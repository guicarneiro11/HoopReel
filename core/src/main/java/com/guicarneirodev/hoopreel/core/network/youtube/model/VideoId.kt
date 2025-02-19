package com.guicarneirodev.hoopreel.core.network.youtube.model

import com.google.gson.annotations.SerializedName

data class VideoId(
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("videoId")
    val videoId: String? = null
)
