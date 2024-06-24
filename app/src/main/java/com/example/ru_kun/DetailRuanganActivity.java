package com.example.ru_kun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.ru_kun.model.Ruangan;

public class DetailRuanganActivity extends AppCompatActivity {
    private ImageView imageRuangan;
    private TextView textNamaRuangan, textBadge, textDeskripsiRuangan, textTanggal, textWktMulai, textWktSelesai;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ruangan);

        imageRuangan = findViewById(R.id.imageRuangan);
        textNamaRuangan = findViewById(R.id.textNamaRuangan);
        textBadge = findViewById(R.id.textBadge);
        textDeskripsiRuangan = findViewById(R.id.textDeskripsiRuangan);
        textTanggal = findViewById(R.id.textTanggal);
        textWktMulai = findViewById(R.id.textWktMulai);
        textWktSelesai = findViewById(R.id.textWktSelesai);
        buttonBack = findViewById(R.id.buttonBack);

        Ruangan ruangan = (Ruangan) getIntent().getSerializableExtra("ruangan");

        if (ruangan != null) {
            textNamaRuangan.setText(ruangan.getNama());
            textBadge.setText(ruangan.getKondisi());
            textDeskripsiRuangan.setText(ruangan.getDeskripsi());
            textTanggal.setText(ruangan.getTanggal());
            textWktMulai.setText(ruangan.getWaktuMulai());
            textWktSelesai.setText(ruangan.getWaktuSelesai());

            Glide.with(this)
                    .load(ruangan.getImage())
                    .into(imageRuangan);

            // Set background based on room condition
            if ("Tersedia".equals(ruangan.getKondisi())) {
                textBadge.setBackgroundResource(R.drawable.badge_available);
            } else {
                textBadge.setBackgroundResource(R.drawable.badge_not_available);
            }
        }

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailRuanganActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optionally, call finish() to remove the current activity from the back stack
        });
    }
}
