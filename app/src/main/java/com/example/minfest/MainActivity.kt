package com.example.minfest // Sesuaikan dengan package Anda

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.minfest.databinding.ActivityMainBinding
import com.example.minfest.databinding.DialogAddEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var menfessAdapter: MenfessAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Mengobservasi perubahan data dari ViewModel
        viewModel.menfessList.observe(this) { list ->
            menfessAdapter.submitList(list)
        }

        // Menangani klik pada tombol tambah
        binding.fabAdd.setOnClickListener {
            showAddEditDialog(null) // null berarti mode Tambah
        }
    }

    private fun setupRecyclerView() {
        menfessAdapter = MenfessAdapter(
            onEditClick = { menfess ->
                showAddEditDialog(menfess) // mode Edit
            },
            onDeleteClick = { menfess ->
                // Meminta konfirmasi sebelum menghapus
                AlertDialog.Builder(this)
                    .setTitle("Hapus Pesan")
                    .setMessage("Apakah Anda yakin ingin menghapus pesan ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        menfess.id?.let { viewModel.deleteMenfess(it) }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )
        binding.recyclerView.adapter = menfessAdapter
    }

    private fun showAddEditDialog(menfess: Menfess?) {
        val dialogBinding = DialogAddEditBinding.inflate(LayoutInflater.from(this))

        // Jika mode edit, isi field dengan data yang ada
        menfess?.let {
            dialogBinding.etTo.setText(it.to)
            dialogBinding.etFrom.setText(it.from)
            dialogBinding.etMessage.setText(it.message)
        }

        AlertDialog.Builder(this)
            .setTitle(if (menfess == null) "Kirim Pesan Baru" else "Edit Pesan")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { _, _ ->
                val to = dialogBinding.etTo.text.toString().trim()
                val from = dialogBinding.etFrom.text.toString().trim()
                val message = dialogBinding.etMessage.text.toString().trim()

                // Buat atau update objek Menfess
                val newMenfess = menfess?.copy(to = to, from = from, message = message)
                    ?: Menfess(to = to, from = from, message = message)

                if (menfess == null) {
                    viewModel.addMenfess(newMenfess)
                } else {
                    viewModel.updateMenfess(newMenfess)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}