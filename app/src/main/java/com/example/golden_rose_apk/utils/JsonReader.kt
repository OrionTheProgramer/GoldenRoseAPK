package com.example.golden_rose_apk.utils

import android.content.Context
import com.example.golden_rose_apk.model.BlogPost
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun cargarBlogsDesdeAssets(context: Context, fileName: String = "blogs.json"): List<BlogPost> {
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<BlogPost>>() {}.type
    return Gson().fromJson(jsonString, listType)
}