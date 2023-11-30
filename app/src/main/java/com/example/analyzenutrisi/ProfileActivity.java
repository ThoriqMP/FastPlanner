package com.example.analyzenutrisi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {
    TextView ubah,save,usn,email,stats,telpon;
    EditText etUsn, etEmail, ettelpon;
    Button logout;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        etUsn = findViewById(R.id.et_usn);
        etEmail = findViewById(R.id.et_email);
        ettelpon = findViewById(R.id.et_telepon);
        usn = findViewById(R.id.t_usn);
        email = findViewById(R.id.t_email);
        telpon = findViewById(R.id.t_telepon);
        try {
            SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
            Gson gson = new Gson();

            // Mendapatkan nilai JSON yang disimpan
            String userJson = preferences.getString("user", "");
            User user = gson.fromJson(userJson, User.class);
            String nama = user.getNama();
            String emailp = user.getEmail();
            String telponJ = user.getNo_telepon();
            stats = findViewById(R.id.status_user);
            Integer statusp = user.getStatus_plan();
            Log.d("response","nilai status:" + statusp);
            if (statusp == 0) {
                String nilai_statu = "Dietisien Biasa";
                stats.setText("status : "+ nilai_statu);
            } else if (statusp == 1) {
                String nilai_statu = "Menunggu Konfirmasi";
                stats.setText("status : "+ nilai_statu);
                stats.setTextColor(getResources().getColor(R.color.warna_kuning));
            } else if (statusp == 2) {
                String nilai_statu = "Dietisien Pro";
                stats.setText("status : "+ nilai_statu);
                stats.setTextColor(getResources().getColor(R.color.warna_hijau));
            } else {
                stats.setText("status : Galat");
                stats.setTextColor(getResources().getColor(R.color.warna_merah));
            }

            etUsn = findViewById(R.id.et_usn);
            etEmail = findViewById(R.id.et_email);

            usn = findViewById(R.id.t_usn);
            email = findViewById(R.id.t_email);
            telpon.setText(telponJ);
            usn.setText(nama);
            email.setText(emailp);
            etUsn.setText(nama);
            etEmail.setText(emailp);
            ettelpon.setText(telponJ);

            ubah = findViewById(R.id.teks_ubah);
            ubah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etUsn.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.VISIBLE);
                    ettelpon.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);

                    usn.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    ubah.setVisibility(View.GONE);
                    telpon.setVisibility(View.GONE);
                }
            });

            save = findViewById(R.id.teks_save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input_nama = etUsn.getText().toString();
                    String input_email = etEmail.getText().toString();
                    String input_telepon = ettelpon.getText().toString();
                    user.setNama(input_nama);
                    user.setEmail(input_email);
                    user.setNo_telepon(input_telepon);

                    // Menyimpan kembali objek User yang telah diubah ke dalam SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user", gson.toJson(user));
                    editor.apply();

                    usn.setText(input_nama); // Update teks di tampilan dengan data baru
                    email.setText(input_email); // Update teks di tampilan dengan data baru
                    telpon.setText(input_telepon);

                    usn.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    telpon.setVisibility(View.VISIBLE);
                    ubah.setVisibility(View.VISIBLE);

                    etUsn.setVisibility(View.GONE);
                    etEmail.setVisibility(View.GONE);
                    ettelpon.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProfileActivity", "Error: " + e.getMessage());
            Toast.makeText(ProfileActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
        }

        // Cari id edit teks


        // Cari id teks view

        // Cari id tombol

        home = findViewById(R.id.btnBack);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(back);
            }
        });
        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }
    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user");
        editor.apply();
        // Hapus status login dari SharedPreferences
        clearLoginStatus();

        // Kembali ke halaman login setelah logout
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearLoginStatus() {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

    private boolean getLoginStatus() {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn", false);
    }

}