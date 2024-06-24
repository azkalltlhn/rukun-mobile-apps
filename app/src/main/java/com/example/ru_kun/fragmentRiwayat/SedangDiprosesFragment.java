package com.example.ru_kun.fragmentRiwayat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ru_kun.R;
import com.example.ru_kun.model.AjuanPeminjaman;
import com.example.ru_kun.model.Peminjaman;
import com.example.ru_kun.model.PeminjamanDitolak;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ru_kun.adapter.SedangDiprosesAdapter;
import java.util.ArrayList;
import java.util.List;

public class SedangDiprosesFragment extends Fragment {
    private RecyclerView recyclerView;
    private SedangDiprosesAdapter adapter;
    private List<AjuanPeminjaman> ajuanList;
    private String currentUserNim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sedang_diproses, container, false);

        recyclerView = view.findViewById(R.id.cardview_diproses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ajuanList = new ArrayList<>();

        adapter = new SedangDiprosesAdapter(ajuanList, getContext());
        recyclerView.setAdapter(adapter);

        // Mendapatkan NIM pengguna yang sedang login
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUserNim = sharedPreferences.getString("nim", "");

        // Mengambil data dari Firebase
        DatabaseReference ajuanRef = FirebaseDatabase.getInstance().getReference("AjuanPeminjaman");
        ajuanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ajuanList.clear();

                for (DataSnapshot ajuanSnapshot : dataSnapshot.getChildren()) {
                    AjuanPeminjaman ajuan = ajuanSnapshot.getValue(AjuanPeminjaman.class);
                    if (ajuan != null && ajuan.getNim().equals(currentUserNim)) {
                        String idAjuan = ajuan.getIdAjuan();

                        // Cek keberadaan idAjuan di tabel Peminjaman
                        DatabaseReference peminjamanRef = FirebaseDatabase.getInstance().getReference("Peminjaman");
                        peminjamanRef.orderByChild("idAjuan").equalTo(idAjuan).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean idAjuanFoundInPeminjaman = false;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Peminjaman peminjaman = snapshot.getValue(Peminjaman.class);
                                    if (peminjaman != null && peminjaman.getIdAjuan().equals(idAjuan)) {
                                        idAjuanFoundInPeminjaman = true;
                                        break;
                                    }
                                }

                                // Jika idAjuan tidak ditemukan di Peminjaman, lanjut ke PeminjamanDitolak
                                if (!idAjuanFoundInPeminjaman) {
                                    DatabaseReference peminjamanDitolakRef = FirebaseDatabase.getInstance().getReference("PeminjamanDitolak");
                                    peminjamanDitolakRef.orderByChild("idAjuan").equalTo(idAjuan).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            boolean idAjuanFoundInPeminjamanDitolak = false;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                PeminjamanDitolak ditolak = snapshot.getValue(PeminjamanDitolak.class);
                                                if (ditolak != null && ditolak.getIdAjuan().equals(idAjuan)) {
                                                    idAjuanFoundInPeminjamanDitolak = true;
                                                    break;
                                                }
                                            }

                                            // Jika idAjuan tidak ditemukan di PeminjamanDitolak, tambahkan ke ajuanList
                                            if (!idAjuanFoundInPeminjamanDitolak) {
                                                ajuanList.add(ajuan);
                                                adapter.notifyDataSetChanged();
                                            }

                                            // Cek jika ajuanList kosong setelah menambahkan data
                                            if (ajuanList.isEmpty()) {
                                                recyclerView.setVisibility(View.GONE);
                                            } else {
                                                recyclerView.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("SedangDiprosesFragment", "Error checking PeminjamanDitolak", databaseError.toException());
                                        }
                                    });
                                } else {
                                    // Jika idAjuan ditemukan di Peminjaman, cek jika ajuanList kosong
                                    if (ajuanList.isEmpty()) {
                                        recyclerView.setVisibility(View.GONE);
                                    } else {
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("SedangDiprosesFragment", "Error checking Peminjaman", databaseError.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SedangDiprosesFragment", "Error fetching AjuanPeminjaman", databaseError.toException());
            }
        });

        return view;
    }
}
