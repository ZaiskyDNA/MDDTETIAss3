package com.example.minfest

import com.google.firebase.firestore.DocumentId

data class Menfess(
    @DocumentId
    var id: String? = null,
    var to: String = "",
    var from: String = "Anonymous",
    var message: String = ""
)