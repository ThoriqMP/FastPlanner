package com.example.analyzenutrisi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class FormDataDiri extends AppCompatActivity {
    TextInputEditText usiaEditText,jenisKelaminEditText,beratBadanEditText,tinggiBadanEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_data_diri);
        TextView login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(FormDataDiri.this, LoginActivity.class);
                startActivity(login);
            }
        });

        // Ambil nilai dari Intent
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String telepon = intent.getStringExtra("telepon");
        String password = intent.getStringExtra("password");
        Button daftar = findViewById(R.id.btn_next);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usiaEditText = findViewById(R.id.usiaEditText);
                String usia = usiaEditText.getText().toString();

                jenisKelaminEditText = findViewById(R.id.jenisKelaminEditText);
                String jenisKelamin = jenisKelaminEditText.getText().toString();

                beratBadanEditText = findViewById(R.id.beratBadanEditText);
                String beratBadan = beratBadanEditText.getText().toString();

                tinggiBadanEditText = findViewById(R.id.tinggiBadanEditText);
                String tinggiBadan = tinggiBadanEditText.getText().toString();
                DaftarkanPengguna(nama,usia,jenisKelamin,beratBadan,tinggiBadan,username,telepon,email,password);
                Intent login = new Intent(FormDataDiri.this, LoginActivity.class);
                startActivity(login);
            }
        });
    }

    private void DaftarkanPengguna(String nama, String usia, String jenisKelamin, String beratBadan, String tinggiBadan, String username, String telepon, String email, String password) {
        String datanama,datausia,datajenis,databerat,datatinggi,datauser,datatelepon,dataemail,datapw;
        datanama = nama;
        datausia = usia;
        datajenis = jenisKelamin;
        databerat = beratBadan;
        datatinggi = tinggiBadan;
        datauser = username;
        datatelepon = telepon;
        dataemail = email;
        datapw = BCrypt.withDefaults().hashToString(10, password.toCharArray());
        String urlEndpoint = "https://asia-south1.gcp.data.mongodb-api.com/app/application-2023-hckdc/endpoint/tambahPengguna";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(FormDataDiri.this, "Berhasil Mendaftar", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FormDataDiri.this, "Gagal mendaftar: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("nama", datanama);
                map.put("usia", datausia);
                map.put("jenis_kelamin", datajenis);
                map.put("berat_badan", databerat);
                map.put("tinggi_badan", datatinggi);
                map.put("username", datauser);
                map.put("telepon", datatelepon);
                map.put("email", dataemail);
                map.put("password", datapw);
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }


}