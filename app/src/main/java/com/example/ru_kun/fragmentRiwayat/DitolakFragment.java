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
import com.example.ru_kun.adapter.DitolakAdapter;
import com.example.ru_kun.model.PeminjamanDitolak;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DitolakFragment extends Fragment {
    private RecyclerView recyclerView;
    private DitolakAdapter adapter;
    private List<PeminjamanDitolak> ditolakList;
    private String currentUserNim;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ditolak, container, false);

        recyclerView = view.findViewById(R.id.cardview_ditolak);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ditolakList = new ArrayList<>();
        adapter = new DitolakAdapter(ditolakList, getContext());
        recyclerView.setAdapter(adapter);

        // Mendapatkan NIM pengguna yang sedang login
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUserNim = sharedPreferences.getString("nim", "");

        // Fetch data from Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("PeminjamanDitolak");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ditolakList.clear();
                boolean nimFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PeminjamanDitolak ditolak = snapshot.getValue(PeminjamanDitolak.class);
                    if (ditolak != null && ditolak.getNim().equals(currentUserNim)) {
                        nimFound = true;
                        ditolakList.add(ditolak);
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
                Log.e("DitolakFragment", "Error fetching data", databaseError.toException());
            }
        });

        return view;
    }
}
