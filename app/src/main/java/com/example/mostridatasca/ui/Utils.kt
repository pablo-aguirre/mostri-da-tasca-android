package com.example.mostridatasca.ui

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
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
    val byteArray = Base64.decode(image, Base64.DEFAULT);
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    Image(
        bitmap = bitmap.asImageBitmap(),
        alignment = alignment,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}