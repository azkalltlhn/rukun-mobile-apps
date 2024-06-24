package com.example.ru_kun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    private CircleImageView imageProfile;
    private TextView textName, textNIM, textProdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        imageProfile = findViewById(R.id.imageProfile);
        textName = findViewById(R.id.textName);
        textNIM = findViewById(R.id.textNIM);
        textProdi = findViewById(R.id.textProdi);

        // Mendapatkan nim pengguna dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String nim = preferences.getString("nim", "");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserPinjam");
        Query query = userRef.orderByChild("nim").equalTo(nim);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("nama").getValue(String.class);
                        String nim = userSnapshot.child("nim").getValue(String.class);
                        String prodi = userSnapshot.child("prodi").getValue(String.class);
                        String photoUrl = userSnapshot.child("foto").getValue(String.class);

                        if (name != null) {
                            textName.setText(name);
                        }
                        if (nim != null) {
                            textNIM.setText(nim);
                        }
                        if (prodi != null) {
                            textProdi.setText(prodi);
                        }
                        if (photoUrl != null) {
                            Glide.with(ProfilActivity.this)
                                    .load(photoUrl)
                                    .into(imageProfile);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }

        });

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profil);  // Set default selected item

        // Menambahkan listener untuk menangani setiap item yang dipilih pada BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.profil) {
                    // Action when "profil" is clicked
                    Toast.makeText(ProfilActivity.this, "Profil clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.home) {
                    // Navigasi ke halaman home
                    intent = new Intent(ProfilActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.pinjam) {
                    // Navigasi ke halaman Pinjam
                    intent = new Intent(ProfilActivity.this, PinjamActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.riwayat) {
                    // Navigasi ke halaman riwayat
                    intent = new Intent(ProfilActivity.this, RiwayatActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        // Menangani klik pada tombol "Ubah Foto Profil"
        LinearLayout buttonEditProfile = findViewById(R.id.button_edit_profile);
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke halaman UbahProfil
                Intent intent = new Intent(ProfilActivity.this, UbahProfilActivity.class);
                startActivity(intent);
            }
        });

        // Menangani klik pada tombol "Pusat Bantuan"
        LinearLayout buttonPusatBantuan = findViewById(R.id.button_pusatbantuan);
        buttonPusatBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke halaman PusatBantuan
                Intent intent = new Intent(ProfilActivity.this, PusatBantuanActivity.class);
                startActivity(intent);
            }
        });

        // Menangani klik pada tombol "Keluar"
        LinearLayout buttonKeluar = findViewById(R.id.button_keluar);
        buttonKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate layout for dialog
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm_exit, null);

                // Build the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
                builder.setView(dialogView);

                // Find views in the dialog layout
                Button buttonYes = dialogView.findViewById(R.id.button_yes);
                Button buttonNo = dialogView.findViewById(R.id.button_no);

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                // Set click listeners for buttons in the dialog
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Close the dialog

                        // Clear nim from SharedPreferences (log out)
                        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                        preferences.edit().remove("nim").apply();

                        // Close the current activity
                        finishAffinity(); // Close all activities in the task stack
                    }
                });

                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Close the dialog
                    }
                });
            }
        });

    }
}
