package com.example.minfest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MenfessRepository) : ViewModel() {

    private val _menfessList = MutableLiveData<List<Menfess>>()
    val menfessList: LiveData<List<Menfess>> = _menfessList

    private var listenerRegistration: ListenerRegistration? = null

    init {
        // Langsung "mendengarkan" perubahan data saat ViewModel dibuat
        listenToMenfessUpdates()
    }

    private fun listenToMenfessUpdates() {
        listenerRegistration = repository.getAllMenfess().addSnapshotListener { snapshots, error ->
            if (error != null) {
                // Di aplikasi nyata, Anda bisa menangani error ini, misal dengan LiveData lain
                return@addSnapshotListener
            }
            val menfess = snapshots?.toObjects(Menfess::class.java)
            _menfessList.value = menfess
        }
    }

    // Fungsi untuk dipanggil dari UI untuk menambah data
    fun addMenfess(menfess: Menfess) = viewModelScope.launch {
        repository.addMenfess(menfess)
    }

    // Fungsi untuk dipanggil dari UI untuk update data
    fun updateMenfess(menfess: Menfess) = viewModelScope.launch {
        repository.updateMenfess(menfess)
    }

    // Fungsi untuk dipanggil dari UI untuk hapus data
    fun deleteMenfess(menfessId: String) = viewModelScope.launch {
        repository.deleteMenfess(menfessId)
    }

    // Membersihkan listener saat ViewModel tidak lagi digunakan untuk mencegah memory leak
    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}