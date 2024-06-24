package com.example.ru_kun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextNIM, editTextPassword;
    Button btnLogin;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNIM = findViewById(R.id.nim);
        editTextPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_Login);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserPinjam");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nim = editTextNIM.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(nim)) {
                    Toast.makeText(LoginActivity.this, "Masukkan NIM/NIP", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Masukkan Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(nim, password);
            }
        });
    }

    private void loginUser(String nim, String password) {
        databaseReference.orderByChild("nim").equalTo(nim).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String dbPassword = userSnapshot.child("password").getValue(String.class);
                        if (dbPassword.equals(password)) {
                            // Login successful
                            // Simpan nim pengguna di SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("nim", nim);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    // If no matching password
                    Toast.makeText(LoginActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                } else {
                    // If no matching NIM
                    Toast.makeText(LoginActivity.this, "NIM tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
