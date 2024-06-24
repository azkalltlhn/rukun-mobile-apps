package com.example.ru_kun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.example.ru_kun.adapter.NotificationAdapter;
import com.example.ru_kun.model.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String currentUserNim;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengarahkan ke halaman Home saat tombol back diklik
                Intent intent = new Intent(NotifActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Mendapatkan nim pengguna dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        currentUserNim = preferences.getString("nim", "");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.cardview_notif);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(notificationList, this);
        recyclerView.setAdapter(notificationAdapter);

        checkAjuanPeminjaman();
    }

    private void checkAjuanPeminjaman() {
        Query query = databaseReference.child("AjuanPeminjaman").orderByChild("nim").equalTo(currentUserNim);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationList.clear();
                Map<String, List<Notification>> groupedNotifications = new HashMap<>();

                for (DataSnapshot ajuanSnapshot : dataSnapshot.getChildren()) {
                    String idAjuan = ajuanSnapshot.child("idAjuan").getValue(String.class);
                    String namaRuangan = ajuanSnapshot.child("namaRuangan").getValue(String.class);
                    String timestamp = ajuanSnapshot.child("timestamp").getValue(String.class); // Ambil timestamp
                    if (idAjuan != null && namaRuangan != null) {
                        // Create the "Sedang Diproses" notification
                        String sedangDiprosesMessage = "Jangan khawatir, pengajuan " + namaRuangan + " sedang di proses oleh admin. Mohon ditunggu dan cek secara berkala, ya!";
                        Notification sedangDiprosesNotification = new Notification("Pengajuan Sedang Diproses",
                                sedangDiprosesMessage, R.drawable.icon_statusproses, timestamp); // Sertakan timestamp

                        // Initialize a list with the "Sedang Diproses" notification
                        List<Notification> notifications = new ArrayList<>();
                        notifications.add(sedangDiprosesNotification);

                        // Store the list in the map with idAjuan as the key
                        groupedNotifications.put(idAjuan, notifications);

                        // Check the status of the submission
                        checkPeminjamanStatus(idAjuan, groupedNotifications, namaRuangan);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void checkPeminjamanStatus(final String idAjuan, final Map<String, List<Notification>> groupedNotifications, final String namaRuangan) {
        Query query = databaseReference.child("Peminjaman").orderByChild("idAjuan").equalTo(idAjuan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String timestamp = ""; // Default value in case the snapshot doesn't have a timestamp
                    for (DataSnapshot peminjamanSnapshot : dataSnapshot.getChildren()) {
                        timestamp = peminjamanSnapshot.child("timestamp").getValue(String.class);
                    }
                    // Add notification for successful submission
                    String pengajuanBerhasilMessage = "Pengajuan peminjaman " + namaRuangan + " berhasil. Cek informasi detail pengajuanmu pada menu Riwayat.";
                    groupedNotifications.get(idAjuan).add(new Notification("Pengajuan Berhasil",
                            pengajuanBerhasilMessage, R.drawable.icon_checklist, timestamp));
                } else {
                    checkPeminjamanDitolakStatus(idAjuan, groupedNotifications, namaRuangan);
                }
                updateNotificationList(groupedNotifications);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void checkPeminjamanDitolakStatus(final String idAjuan, final Map<String, List<Notification>> groupedNotifications, final String namaRuangan) {
        Query query = databaseReference.child("PeminjamanDitolak").orderByChild("idAjuan").equalTo(idAjuan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String timestamp = ""; // Default value in case the snapshot doesn't have a timestamp
                    for (DataSnapshot peminjamanDitolakSnapshot : dataSnapshot.getChildren()) {
                        timestamp = peminjamanDitolakSnapshot.child("timestamp").getValue(String.class);
                    }
                    // Add notification for rejected submission
                    String pengajuanDitolakMessage = "Pengajuan peminjaman " + namaRuangan + " ditolak. Cek informasi detail pengajuanmu pada menu Riwayat.";
                    groupedNotifications.get(idAjuan).add(new Notification("Pengajuan Ditolak",
                            pengajuanDitolakMessage, R.drawable.icon_statusditolak, timestamp));
                }
                updateNotificationList(groupedNotifications);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void updateNotificationList(Map<String, List<Notification>> groupedNotifications) {
        // Clear existing notificationList
        notificationList.clear();

        // Create a list to hold all notifications
        List<Notification> allNotifications = new ArrayList<>();

        // Iterate through groupedNotifications and add all notifications to allNotifications
        for (Map.Entry<String, List<Notification>> entry : groupedNotifications.entrySet()) {
            allNotifications.addAll(entry.getValue());
        }

        // Sort allNotifications based on timestamp (assuming timestamp is in format of yyyy-MM-dd HH:mm:ss)
        Collections.sort(allNotifications, new Comparator<Notification>() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public int compare(Notification o1, Notification o2) {
                try {
                    Date date1 = dateFormat.parse(o1.getTimestamp());
                    Date date2 = dateFormat.parse(o2.getTimestamp());
                    return date2.compareTo(date1); // Sort in descending order (latest to oldest)
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        // Add sorted notifications to notificationList
        notificationList.addAll(allNotifications);

        // Notify adapter that data set has changed
        notificationAdapter.notifyDataSetChanged();
    }

}