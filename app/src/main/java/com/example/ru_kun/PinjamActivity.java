package com.example.ru_kun;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ru_kun.adapter.RuanganAdapter;
import com.example.ru_kun.model.Ruangan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PinjamActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPinjam;
    private List<Ruangan> ruanganList;
    private List<Ruangan> ruanganListFull;
    private RuanganAdapter ruanganAdapter;
    private DatabaseReference ruanganRef;
    private String currentSearchText = "";
    private String currentFilterText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam);

        recyclerViewPinjam = findViewById(R.id.cardview_pinjam);
        recyclerViewPinjam.setLayoutManager(new LinearLayoutManager(this));

        ruanganList = new ArrayList<>();
        ruanganListFull = new ArrayList<>();
        ruanganAdapter = new RuanganAdapter(this, ruanganList, 1); // Menggunakan viewType 1 untuk PinjamActivity
        recyclerViewPinjam.setAdapter(ruanganAdapter);

        // Mengambil data ruangan dari Firebase
        ruanganRef = FirebaseDatabase.getInstance().getReference("Ruangan");
        ruanganRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ruanganListFull.clear();
                ruanganList.clear();
                for (DataSnapshot ruanganSnapshot : dataSnapshot.getChildren()) {
                    Ruangan ruangan = ruanganSnapshot.getValue(Ruangan.class);
                    if (ruangan != null && "Tersedia".equals(ruangan.getKondisi())) {
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
            }
        });

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.pinjam);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.pinjam) {
                    Toast.makeText(PinjamActivity.this, "Pinjam clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.profil) {
                    intent = new Intent(PinjamActivity.this, ProfilActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.home) {
                    intent = new Intent(PinjamActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.riwayat) {
                    intent = new Intent(PinjamActivity.this, RiwayatActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        // Filter button
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
    }

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
