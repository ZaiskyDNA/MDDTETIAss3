package com.example.minfest

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MenfessRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    // Menentukan koleksi mana yang akan digunakan di Firestore
    private val collection = firestore.collection("menfess")

    // CREATE: Menambah dokumen menfess baru
    suspend fun addMenfess(menfess: Menfess) {
        collection.add(menfess).await()
    }

    // READ: Mendapatkan semua data menfess secara real-time
    fun getAllMenfess() = collection.orderBy("to", Query.Direction.ASCENDING)

    // UPDATE: Memperbarui dokumen menfess yang sudah ada
    suspend fun updateMenfess(menfess: Menfess) {
        menfess.id?.let { // Hanya update jika ID tidak null
            collection.document(it).set(menfess).await()
        }
    }

    // DELETE: Menghapus dokumen menfess berdasarkan ID-nya
    suspend fun deleteMenfess(menfessId: String) {
        collection.document(menfessId).delete().await()
    }
}