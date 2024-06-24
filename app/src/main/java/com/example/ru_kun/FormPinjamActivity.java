package com.example.ru_kun;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ru_kun.model.AjuanPeminjaman;
import com.example.ru_kun.model.Ruangan;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormPinjamActivity extends AppCompatActivity {
    private TextInputEditText textNama, textNIM, textNamaRuangan, textTanggal, textWktMulai, textWktSelesai, textNamaKegiatan;
    private ImageButton buttonBack;
    private TextView suratKegiatan;
    private ImageView imageUpload;
    private Uri fileUri;
    private static final int REQUEST_CODE_FILE_PICKER = 123;
    private Button buttonAjukan;
    private TextView idAjuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pinjam);

        textNama = findViewById(R.id.textNama);
        textNIM = findViewById(R.id.textNIM);
        textNamaRuangan = findViewById(R.id.textNamaRuangan);
        textTanggal = findViewById(R.id.textTanggal);
        textWktMulai = findViewById(R.id.textWktMulai);
        textWktSelesai = findViewById(R.id.textWktSelesai);
        buttonBack = findViewById(R.id.buttonBack);
        suratKegiatan = findViewById(R.id.SuratKegiatan);
        imageUpload = findViewById(R.id.imageUpload);
        buttonAjukan = findViewById(R.id.buttonAjukan);
        idAjuan = findViewById(R.id.idAjuan);
        textNamaKegiatan = findViewById(R.id.NamaKegiatan);

        // Mengambil data pengguna dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String userNim = preferences.getString("nim", "");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserPinjam");
        Query query = userRef.orderByChild("nim").equalTo(userNim);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("nama").getValue(String.class);
                        String nim = userSnapshot.child("nim").getValue(String.class);
                        if (name != null) {
                            textNama.setText(name);
                        }
                        if (nim != null) {
                            textNIM.setText(nim);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Mengambil data ruangan dari Intent
        Intent intent = getIntent();
        Ruangan ruangan = (Ruangan) intent.getSerializableExtra("ruangan");

        if (ruangan != null) {
            textNamaRuangan.setText(ruangan.getNama());
            textTanggal.setText(ruangan.getTanggal());
            textWktMulai.setText(ruangan.getWaktuMulai());
            textWktSelesai.setText(ruangan.getWaktuSelesai());
        }
        // Memanggil metode setIdAjuan
        setIdAjuan();

        // Menambahkan listener pada tombol "Kembali"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke halaman PinjamActivity
                Intent intent = new Intent(FormPinjamActivity.this, PinjamActivity.class);
                startActivity(intent);
                // Tutup halaman Form Pinjam
                finish();
            }
        });

        // Menambahkan listener pada tombol "Ajukan"
        buttonAjukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input
                if (validateInput()) {
                    // Lakukan aksi ketika tombol "Ajukan" ditekan
                    uploadFileToFirebaseStorage();
                }
            }
        });
    }

    // Metode untuk mengatur idAjuan
    private void setIdAjuan() {
        DatabaseReference ajuanRef = FirebaseDatabase.getInstance().getReference("AjuanPeminjaman");
        ajuanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Menghitung jumlah child (idAjuan yang sudah ada)
                    long count = dataSnapshot.getChildrenCount() + 1;
                    String idAjuanText = String.format(Locale.getDefault(), "idajuan%05d", count);
                    idAjuan.setText(idAjuanText);
                } else {
                    // Jika belum ada data sama sekali
                    String idAjuanText = "idajuan00001";
                    idAjuan.setText(idAjuanText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    // Metode untuk menerima URI file yang diunggah dari aktivitas lain
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILE_PICKER) {
            if (data != null) {
                fileUri = data.getData();
                // Dapatkan nama file dari URI
                String fileName = getFileName(fileUri);
                // Perbarui teks TextView dengan nama file
                suratKegiatan.setText(fileName);
                // Sembunyikan ImageView
                imageUpload.setVisibility(View.GONE);

                // Simpan file ke Firebase Storage
                uploadFileToFirebaseStorage();
            }
        }
    }

    // Metode untuk validasi input sebelum mengajukan peminjaman
    private boolean validateInput() {
        String namaKegiatan = textNamaKegiatan.getText().toString().trim();
        String fileName = suratKegiatan.getText().toString().trim();

        if (namaKegiatan.isEmpty()) {
            Toast.makeText(this, "Nama kegiatan harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fileName.isEmpty()) {
            Toast.makeText(this, "File belum dipilih", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Metode untuk mengunggah file ke Firebase Storage
    private void uploadFileToFirebaseStorage() {
        if (fileUri != null) {
            String fileName = getFileName(fileUri);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads").child(fileName);

            // Upload file ke Firebase Storage
            storageRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Dapatkan URL download file setelah berhasil diunggah
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String fileUrl = uri.toString();
                            // Simpan fileUrl bersama data lainnya ke Firebase Realtime Database
                            saveAjuanPeminjamanToDatabase(fileUrl);
                        }).addOnFailureListener(e -> {
                            // Gagal mendapatkan URL download
                            Toast.makeText(FormPinjamActivity.this, "Gagal mengunggah file. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Gagal mengunggah file
                        Toast.makeText(FormPinjamActivity.this, "Gagal mengunggah file. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(FormPinjamActivity.this, "File belum dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    // Metode untuk menyimpan data peminjaman ke Firebase Realtime Database
    private void saveAjuanPeminjamanToDatabase(String fileUrl) {
        String nama = textNama.getText().toString().trim();
        String nim = textNIM.getText().toString().trim();
        String namaRuangan = textNamaRuangan.getText().toString().trim();
        String tanggalPinjam = textTanggal.getText().toString().trim();
        String waktuMulai = textWktMulai.getText().toString().trim();
        String waktuSelesai = textWktSelesai.getText().toString().trim();
        String namaKegiatan = textNamaKegiatan.getText().toString().trim();
        String idAjuanText = idAjuan.getText().toString().trim();
        String timestamp = getCurrentTimestamp(); // Mendapatkan timestamp saat ini

        // Mendapatkan URL gambar dari Ruangan
        DatabaseReference ruanganRef = FirebaseDatabase.getInstance().getReference("Ruangan");
        Query query = ruanganRef.orderByChild("nama").equalTo(namaRuangan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ruanganSnapshot : dataSnapshot.getChildren()) {
                        String image = ruanganSnapshot.child("image").getValue(String.class);
                        // Membuat objek AjuanPeminjaman
                        AjuanPeminjaman ajuan = new AjuanPeminjaman(idAjuanText, nama, nim, namaRuangan, tanggalPinjam, waktuMulai, waktuSelesai, namaKegiatan, fileUrl, image, timestamp);

                        // Simpan ke Firebase Realtime Database
                        DatabaseReference ajuanRef = FirebaseDatabase.getInstance().getReference("AjuanPeminjaman").child(idAjuanText);
                        ajuanRef.setValue(ajuan)
                                .addOnSuccessListener(aVoid -> {
                                    // Perbarui status ruangan menjadi "Tidak Tersedia"
                                    updateRuanganStatus(namaRuangan, tanggalPinjam, waktuMulai, waktuSelesai);

                                    // Tampilkan dialog konfirmasi berhasil
                                    showSuccessDialog(namaRuangan, tanggalPinjam, namaKegiatan);
                                })
                                .addOnFailureListener(e -> {
                                    // Gagal menyimpan data
                                    Toast.makeText(FormPinjamActivity.this, "Gagal mengajukan peminjaman. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(FormPinjamActivity.this, "Ruangan tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(FormPinjamActivity.this, "Gagal mengambil data ruangan.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Metode untuk memperbarui status ruangan di Firebase
    private void updateRuanganStatus(String namaRuangan, String tanggal, String waktuMulai, String waktuSelesai) {
        DatabaseReference ruanganRef = FirebaseDatabase.getInstance().getReference("Ruangan");
        Query query = ruanganRef.orderByChild("nama").equalTo(namaRuangan);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ruanganSnapshot : dataSnapshot.getChildren()) {
                    // Memperbarui nilai "kondisi" menjadi "Tidak Tersedia"
                    ruanganSnapshot.getRef().child("kondisi").setValue("Tidak Tersedia");
                    ruanganSnapshot.getRef().child("tanggal").setValue(tanggal);
                    ruanganSnapshot.getRef().child("waktuMulai").setValue(waktuMulai);
                    ruanganSnapshot.getRef().child("waktuSelesai").setValue(waktuSelesai);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(FormPinjamActivity.this, "Gagal memperbarui status ruangan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode untuk menampilkan dialog konfirmasi berhasil
    private void showSuccessDialog(String nmRuangan, String tgl, String nmKegiatan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm_peminjaman, null);
        builder.setView(dialogView);

        TextView textNamaRuangan = dialogView.findViewById(R.id.namaRuangan);
        TextView textTanggal = dialogView.findViewById(R.id.Tanggal);
        TextView textNamaKegiatan = dialogView.findViewById(R.id.NamaKegiatan);

        textNamaRuangan.setText(nmRuangan);
        textTanggal.setText(tgl);
        textNamaKegiatan.setText(nmKegiatan);

        Button buttonNo = dialogView.findViewById(R.id.button_no);
        Button buttonYes = dialogView.findViewById(R.id.button_yes);

        AlertDialog dialog = builder.create();

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke halaman Pinjam
                Intent intent = new Intent(FormPinjamActivity.this, PinjamActivity.class);
                startActivity(intent);
                dialog.dismiss();
                // Tutup halaman Form Pinjam
                finish();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pergi ke halaman Riwayat
                Intent intent = new Intent(FormPinjamActivity.this, RiwayatActivity.class);
                startActivity(intent);
                dialog.dismiss();
                // Tutup halaman Form Pinjam
                finish();
            }
        });

        dialog.show();
    }

    // Metode untuk mendapatkan nama file dari URI
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    // Metode yang dipanggil saat TextView "SuratKegiatan" diklik
    public void onSuratKegiatanClick(View view) {
        // Membuka pemilih file
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_FILE_PICKER);
    }

    // Metode untuk mendapatkan timestamp saat ini
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}