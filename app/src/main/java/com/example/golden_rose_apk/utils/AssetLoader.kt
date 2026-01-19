package com.example.golden_rose_apk.utils

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun cargarImagenDeAssets(context: Context, path: String): ImageBitmap {
    val inputStream = context.assets.open(path)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()
    return bitmap.asImageBitmap()
}