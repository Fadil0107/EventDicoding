package com.dicoding.eventdicoding.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.eventdicoding.data.response.DetailData
import com.dicoding.eventdicoding.databinding.ActivityDetailBinding
import android.text.Html
import com.dicoding.eventdicoding.data.database.Event
import com.dicoding.eventdicoding.data.database.EventDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var detailData: DetailData? = null // Nullable DetailData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data DetailData dari Intent
        detailData = intent.getParcelableExtra("EXTRA_DETAIL")

        // Null check jika data tidak ditemukan
        detailData?.let {
            displayEventDetails(it) // Tampilkan data jika tidak null
        } ?: run {
            Toast.makeText(this, "Data acara tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish() // Kembali jika data tidak ada
            return
        }

        // Listener untuk tombol "Daftar"
        binding.filledButton.setOnClickListener {
            openEventUrl(detailData?.link)
        }

        // Listener untuk tombol favorit
        binding.favoriteButton.setOnClickListener {
            detailData?.let { data ->
                saveEventToFavorites(data)
            } ?: run {
                Toast.makeText(this, "Tidak dapat menyimpan data yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayEventDetails(detailData: DetailData) {
        // Menghitung sisa kuota dengan memeriksa null
        val sisaKuota = (detailData.quota ?: 0) - (detailData.registrans ?: 0)

        // Menampilkan data di TextView
        binding.name.text = detailData.name
        binding.ownerName.text = detailData.ownerName
        binding.quota.text = sisaKuota.toString() // Menampilkan sisa kuota
        binding.beginTime.text = detailData.beginTime
        binding.description.text = detailData.summary

        // Menggunakan let untuk menampilkan deskripsi panjang dengan HTML yang dirapikan
        binding.descriptionLong.text = detailData.description?.let { fromHtml(it) } ?: ""

        // Memuat gambar dengan Glide
        Glide.with(this)
            .load(detailData.imageLogo)
            .into(binding.image)
    }

    // Fungsi untuk membuka URL pendaftaran di browser
    private fun openEventUrl(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(it)
            }
            startActivity(intent)
        } ?: run {
            // Tampilkan pesan jika URL tidak tersedia
            Toast.makeText(this, "URL Pendaftaran tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk menyimpan acara ke favorit
    // Fungsi untuk menyimpan acara ke favorit
    private fun saveEventToFavorites(detailData: DetailData) {
        val event = Event(
            id = null, // Set id ke null agar auto-generate
            name = detailData.name,
            ownerName = detailData.ownerName,
            quota = detailData.quota,
            registrans = detailData.registrans,
            beginTime = detailData.beginTime,
            summary = detailData.summary,
            description = detailData.description,
            imageLogo = detailData.imageLogo,
            link = detailData.link,
            isFavorite = detailData.isFavorite
        )

        // Simpan event ke database menggunakan Coroutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val eventDao = EventDatabase.getDatabase(applicationContext).eventDao()
                eventDao.insert(event) // Simpan ke database
                runOnUiThread {
                    Toast.makeText(this@DetailActivity, "Acara ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@DetailActivity, "Gagal menambahkan acara ke favorit", Toast.LENGTH_SHORT).show()
                    e.printStackTrace() // Tambahkan ini untuk melihat error yang terjadi
                }
            }
        }
    }

    // Fungsi untuk merapikan teks HTML
    private fun fromHtml(html: String): CharSequence {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }
}