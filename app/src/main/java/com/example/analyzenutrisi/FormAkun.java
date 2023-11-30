package com.example.analyzenutrisi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FormAkun extends AppCompatActivity {
    TextInputEditText namaEditText, usernameEditText, emailEditText, teleponEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_akun);
        TextView login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(FormAkun.this, LoginActivity.class);
                startActivity(login);
            }
        });

        // Inisialisasi EditText berdasarkan ID
        namaEditText = findViewById(R.id.namaEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        teleponEditText = findViewById(R.id.teleponEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // Tombol Selanjutnya
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lakukan aksi saat tombol Selanjutnya diklik
                String nama = namaEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String telepon = teleponEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Intent intent = new Intent(FormAkun.this, FormDataDiri.class);

                // PutExtra untuk mengirimkan data ke halaman FormDataDiri
                intent.putExtra("nama", nama);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("telepon", telepon);
                intent.putExtra("password", password);

                // Start Activity
                startActivity(intent);
            }
        });
    }
}