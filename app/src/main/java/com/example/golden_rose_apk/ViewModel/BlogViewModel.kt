package com.example.golden_rose_apk.ViewModel

import androidx.lifecycle.ViewModel
import com.example.golden_rose_apk.model.BlogPost as Blog
import com.example.golden_rose_apk.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BlogViewModel : ViewModel() {
    private val _blogs = MutableStateFlow<List<Blog>>(emptyList())
    val blogs: StateFlow<List<Blog>> = _blogs

    init {
        fetchBlogs()
    }

    private fun fetchBlogs() {
        _blogs.value = BlogRepository.getBlogs()
    }

    fun getBlogById(id: Int): Blog? {
        return _blogs.value.find { it.id == id }
    }
}
