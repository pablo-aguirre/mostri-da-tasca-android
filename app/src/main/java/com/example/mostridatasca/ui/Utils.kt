package com.example.mostridatasca.ui

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale


@Composable
fun ImageFromBase64(
    image: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit
) {
    val bitmap = try {
        val byteArray = Base64.decode(image, Base64.DEFAULT);
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } catch (e: Exception) {
        null
    }
    if (bitmap == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
        )
    } else {
        Image(
            bitmap = bitmap.asImageBitmap(),
            alignment = alignment,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}