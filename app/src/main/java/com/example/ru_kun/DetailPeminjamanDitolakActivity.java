package com.example.ru_kun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.ru_kun.model.PeminjamanDitolak;
import com.google.android.material.textfield.TextInputEditText;

public class DetailPeminjamanDitolakActivity extends AppCompatActivity {

    private TextInputEditText namaUser, nim, tglRuangan, namaRuangan1, wktMulai, wktSelesai, namaKegiatan, catatan,tglDitolak, namaAdmin;
    private ImageView imgRuangan;
    private TextView idDitolak, namaRuangan, suratKegiatan;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_peminjaman_ditolak);

        // Inisialisasi view
        idDitolak = findViewById(R.id.idditolak);
        namaRuangan = findViewById(R.id.nama_ruang);
        namaRuangan1 = findViewById(R.id.nama_ruangan);
        suratKegiatan = findViewById(R.id.SuratKegiatan);
        namaUser = findViewById(R.id.namauser);
        nim = findViewById(R.id.nim);
        tglRuangan = findViewById(R.id.tgl_ruangan);
        namaKegiatan = findViewById(R.id.nama_kegiatan);
        wktMulai = findViewById(R.id.wkt_mulai);
        wktSelesai = findViewById(R.id.wkt_selesai);
        imgRuangan = findViewById(R.id.img_ruangan);
        catatan = findViewById(R.id.catatan);
        tglDitolak = findViewById(R.id.tgl_ditolak);
        namaAdmin = findViewById(R.id.namaadmin);

        buttonBack = findViewById(R.id.buttonBack);

        // Menerima data dari Intent
        if (getIntent().hasExtra("ditolak")) {
            PeminjamanDitolak ditolak = (PeminjamanDitolak) getIntent().getSerializableExtra("ditolak");

            // Set data ke view
            idDitolak.setText(ditolak.getIdDitolak());
            namaRuangan.setText(ditolak.getNamaRuangan());
            namaRuangan1.setText(ditolak.getNamaRuangan());
            String fileUrl = ditolak.getFileUrl();
            suratKegiatan.setText(getFileName(fileUrl)); // Set hanya nama file
            namaUser.setText(ditolak.getNamaUser());
            nim.setText(ditolak.getNim());
            tglRuangan.setText(ditolak.getTanggalPinjam());
            namaKegiatan.setText(ditolak.getNamaKegiatan());
            wktMulai.setText(ditolak.getWaktuMulai());
            wktSelesai.setText(ditolak.getWaktuSelesai());
            catatan.setText(ditolak.getCatatan());
            tglDitolak.setText(ditolak.getTanggalDitolak());
            namaAdmin.setText(ditolak.getNamaAdmin());

            // Load image menggunakan Glide
            Glide.with(this)
                    .load(ditolak.getImage())
                    .into(imgRuangan);

            // Set OnClickListener untuk suratKegiatan
            suratKegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(fileUrl), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            });
        } else {
            // Jika data tidak ditemukan, lakukan sesuai kebutuhan aplikasi Anda
        }

        // Menambahkan listener pada tombol "Kembali"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke halaman Riwayat
                Intent intent = new Intent(DetailPeminjamanDitolakActivity.this, RiwayatActivity.class);
                startActivity(intent);
                // Tutup halaman Form Pinjam
                finish();
            }
        });
    }

    // Metode untuk mendapatkan nama file dari URL
    private String getFileName(String fileUrl) {
        if (fileUrl == null) {
            return "File tidak ditemukan";
        }
        return Uri.parse(fileUrl).getLastPathSegment();
    }
}
