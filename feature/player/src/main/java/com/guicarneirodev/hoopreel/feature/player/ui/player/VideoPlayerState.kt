package com.guicarneirodev.hoopreel.feature.player.ui.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerState(
    private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null

    fun getOrCreatePlayer(): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context)
                .build()
        }
        return exoPlayer!!
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun prepareVideo(videoUrl: String) {
        val mediaItem = MediaItem.fromUri(videoUrl)
        getOrCreatePlayer().apply {
            setMediaItem(mediaItem)
            prepare()
        }
    }
}