package com.example.myapplication3.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreInstance {
    val getInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}