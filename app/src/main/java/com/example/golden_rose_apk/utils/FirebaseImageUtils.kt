package com.example.golden_rose_apk.utils

import com.google.firebase.storage.FirebaseStorage

fun getFirebaseImageUrl(
    path: String,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    FirebaseStorage.getInstance()
        .reference
        .child(path)
        .downloadUrl
        .addOnSuccessListener { uri ->
            onSuccess(uri.toString())
        }
        .addOnFailureListener { onError(it) }
}
