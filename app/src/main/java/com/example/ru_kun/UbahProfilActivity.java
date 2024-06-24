package com.example.ru_kun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

public class UbahProfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private CircleImageView imageProfile;
    private TextView textName, textNIM, textProdi;
    private Uri imageUri;
    private DatabaseReference userRef;
    private StorageReference storageReference;
    private String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        imageProfile = findViewById(R.id.imageProfile);
        textName = findViewById(R.id.nama);
        textNIM = findViewById(R.id.nim);
        textProdi = findViewById(R.id.prodi);

        // Mendapatkan nim pengguna dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        nim = preferences.getString("nim", "");

        userRef = FirebaseDatabase.getInstance().getReference("UserPinjam");
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

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
                            Glide.with(UbahProfilActivity.this)
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

        // Menambahkan listener pada ImageButton untuk membuka galeri
        ImageButton buttonCamera = findViewById(R.id.button_Camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Menambahkan listener pada Button untuk menyimpan perubahan
        Button buttonSimpan = findViewById(R.id.ButtonSimpan);
        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImageToFirebase(imageUri);
                } else {
                    Toast.makeText(UbahProfilActivity.this, "Pilih foto terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke halaman ProfilActivity
                finish();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child(nim + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        updateDatabaseWithImageUrl(downloadUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UbahProfilActivity.this, "Gagal mengupload gambar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDatabaseWithImageUrl(String imageUrl) {
        Query query = userRef.orderByChild("nim").equalTo(nim);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userSnapshot.getRef().child("foto").setValue(imageUrl);
                        Toast.makeText(UbahProfilActivity.this, "Foto profil berhasil diupdate", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}

