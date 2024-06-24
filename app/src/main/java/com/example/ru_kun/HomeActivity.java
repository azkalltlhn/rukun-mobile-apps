package com.example.ru_kun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ru_kun.adapter.RuanganAdapter;
import com.example.ru_kun.model.Ruangan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private CircleImageView imageProfile;
    private TextView userName;
    private RecyclerView recyclerView;
    private List<Ruangan> ruanganList;
    private List<Ruangan> ruanganListFull;
    private RuanganAdapter ruanganAdapter;
    private DatabaseReference ruanganRef;
    private String currentFilterText = "";
    private String currentSearchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi tampilan
        imageProfile = findViewById(R.id.imageProfile);
        userName = findViewById(R.id.userName);
        recyclerView = findViewById(R.id.cardview_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi adapter dan list
        ruanganList = new ArrayList<>();
        ruanganListFull = new ArrayList<>();
        ruanganAdapter = new RuanganAdapter(this, ruanganList, 0); // viewType 0 untuk HomeActivity
        recyclerView.setAdapter(ruanganAdapter);

        // Mendapatkan nim pengguna dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String nim = preferences.getString("nim", "");

        // Mengambil referensi Firebase untuk data pengguna
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserPinjam");
        Query query = userRef.orderByChild("nim").equalTo(nim);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("nama").getValue(String.class);
                        String[] parts = name.split(" ");
                        String firstName = parts[0];
                        userName.setText(firstName);

                        String photoUrl = userSnapshot.child("foto").getValue(String.class);
                        if (photoUrl != null) {
                            Glide.with(HomeActivity.this)
                                    .load(photoUrl)
                                    .into(imageProfile);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(HomeActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Mengambil referensi Firebase untuk data ruangan
        ruanganRef = FirebaseDatabase.getInstance().getReference("Ruangan");
        ruanganRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ruanganListFull.clear();
                ruanganList.clear();
                for (DataSnapshot ruanganSnapshot : dataSnapshot.getChildren()) {
                    Ruangan ruangan = ruanganSnapshot.getValue(Ruangan.class);
                    if (ruangan != null) { // Pastikan ruangan tidak null
                        ruanganListFull.add(ruangan);
                        ruanganList.add(ruangan);
                    }
                }
                ruanganAdapter.notifyDataSetChanged();
                applyFilters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(HomeActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    Toast.makeText(HomeActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.profil) {
                    intent = new Intent(HomeActivity.this, ProfilActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.pinjam) {
                    intent = new Intent(HomeActivity.this, PinjamActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.riwayat) {
                    intent = new Intent(HomeActivity.this, RiwayatActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        // Button Filter
        ImageButton buttonFilter = findViewById(R.id.button_filter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu(v);
            }
        });

        // SearchView
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText;
                applyFilters();
                return true;
            }
        });

        // Button Notifikasi
        ImageButton buttonNotification = findViewById(R.id.buttonNotification);
        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotifActivity.class);
                startActivity(intent);
            }
        });
    }

    // Menampilkan menu filter
    private void showFilterMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.filter_ruangan, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_fiklab) {
                    currentFilterText = "FIKLAB";
                } else if (item.getItemId() == R.id.menu_selasar) {
                    currentFilterText = "Selasar";
                } else if (item.getItemId() == R.id.menu_ruangfik) {
                    currentFilterText = "Ruang FIK";
                } else {
                    currentFilterText = "";
                }
                applyFilters();
                return true;
            }
        });

        popupMenu.show();
    }

    // Menerapkan filter dan pencarian
    private void applyFilters() {
        List<Ruangan> filteredList = new ArrayList<>();
        for (Ruangan ruangan : ruanganListFull) {
            boolean matchesFilter = currentFilterText.isEmpty() || ruangan.getJenis().equalsIgnoreCase(currentFilterText);
            boolean matchesSearch = currentSearchText.isEmpty() || ruangan.getNama().toLowerCase().contains(currentSearchText.toLowerCase());
            if (matchesFilter && matchesSearch) {
                filteredList.add(ruangan);
            }
        }
        ruanganList.clear();
        ruanganList.addAll(filteredList);
        ruanganAdapter.notifyDataSetChanged();
    }
}
