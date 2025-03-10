package com.guicarneirodev.hoopreel.feature.highlights.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PlayerAvatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
    size: Int = 48,
    borderWidth: Int = 2
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(borderWidth.dp, BasketballOrange, CircleShape)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Player image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size.dp)
        )
    }
}