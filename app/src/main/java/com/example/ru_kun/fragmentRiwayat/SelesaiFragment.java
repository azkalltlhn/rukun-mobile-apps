package com.example.ru_kun.fragmentRiwayat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ru_kun.R;
import com.example.ru_kun.adapter.PeminjamanAdapter;
import com.example.ru_kun.model.Peminjaman;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SelesaiFragment extends Fragment {
    private RecyclerView recyclerView;
    private PeminjamanAdapter adapter;
    private List<Peminjaman> peminjamanList;
    private String currentUserNim;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selesai, container, false);

        recyclerView = view.findViewById(R.id.cardview_selesai);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        peminjamanList = new ArrayList<>();
        adapter = new PeminjamanAdapter(getContext(), peminjamanList);
        recyclerView.setAdapter(adapter);

        // Mendapatkan NIM pengguna yang sedang login
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUserNim = sharedPreferences.getString("nim", "");

        // Fetch data from Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Peminjaman");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                peminjamanList.clear();
                boolean nimFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Peminjaman peminjaman = snapshot.getValue(Peminjaman.class);
                    if (peminjaman != null && peminjaman.getNim().equals(currentUserNim)) {
                        nimFound = true;
                        peminjamanList.add(peminjaman);
                    }
                }
                if (nimFound) {
                    adapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SelesaiFragment", "Error fetching data", databaseError.toException());
            }
        });

        return view;
    }
}
