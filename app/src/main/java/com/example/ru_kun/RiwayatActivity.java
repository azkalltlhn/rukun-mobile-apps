package com.example.ru_kun;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.ru_kun.adapter.MyViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class RiwayatActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.riwayat);  // Set default selected item

        // Menambahkan listener untuk menangani setiap item yang dipilih pada BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.riwayat) {
                    // Action when "profil" is clicked
                    Toast.makeText(RiwayatActivity.this, "Riwayat clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.home) {
                    // Navigasi ke halaman home
                    intent = new Intent(RiwayatActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.pinjam) {
                    // Navigasi ke halaman Pinjam
                    intent = new Intent(RiwayatActivity.this, PinjamActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.profil) {
                    // Navigasi ke halaman profil
                    intent = new Intent(RiwayatActivity.this, ProfilActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        //Fragment tab sedang diproses,selesai,ditolak
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 =findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
}