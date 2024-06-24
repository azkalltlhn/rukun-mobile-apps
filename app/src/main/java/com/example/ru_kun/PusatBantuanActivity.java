package com.example.ru_kun;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;

public class PusatBantuanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pusat_bantuan);

        // Menambahkan listener pada ImageButton untuk kembali ke halaman Profil
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke halaman Profil
                Intent intent = new Intent(PusatBantuanActivity.this, ProfilActivity.class);
                startActivity(intent);
                // Tutup halaman PusatBantuan
                finish();
            }
        });
    }
}
