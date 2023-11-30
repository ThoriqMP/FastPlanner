package com.example.analyzenutrisi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class KeranjangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);
        ImageView imageViewGambarProduk = findViewById(R.id.imageViewGambarProduk);
        TextView textViewNamaProduk = findViewById(R.id.textViewNamaProduk);
        ImageView buttonKembali = findViewById(R.id.buttonKembali);

        // Mendapatkan data dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = preferences.getString("user", "");
        User user = gson.fromJson(userJson, User.class);
        Integer status = user.getStatus_pesanan();
        if(status == 0){
            if (user.getNama_produk() == null || user.getGambar() == 0) {
                // Jika tidak ada data produk, tampilkan pesan
                TextView pesanTextView = findViewById(R.id.pesanTextView);
                pesanTextView.setVisibility(View.VISIBLE);
                pesanTextView.setText("Silahkan ambil pesanan Anda di halaman menu.");

                // Sembunyikan tombol konfirmasi pesanan dan ganti pesanan
                Button pesanButton = findViewById(R.id.konfirmasi_pesanan);
                pesanButton.setVisibility(View.GONE);

                textViewNamaProduk.setVisibility(View.GONE);
                imageViewGambarProduk.setVisibility(View.GONE);

                Button gantiPesanButton = findViewById(R.id.ganti_pesanan);
                gantiPesanButton.setVisibility(View.GONE);
            } else {
                // Jika ada data produk, tampilkan data dan tombol
                textViewNamaProduk.setText(user.getNama_produk());
                imageViewGambarProduk.setImageResource(user.getGambar());
            }
        } else {
            Intent gopesan = new Intent(KeranjangActivity.this, PesananActivity.class);
            startActivity(gopesan);
        }
        Log.d("Response","Tampil ga nih:" + status);

        // Menampilkan data produk pada tampilan


        // Menangani klik tombol kembali
        buttonKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke FoodActivity
                Intent intent = new Intent(KeranjangActivity.this, FoodActivity.class);
                startActivity(intent);
                finish(); // Menutup activity saat ini
            }
        });
        SharedPreferences preferencess = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferencess.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Jika belum login, pindahkan ke halaman login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        Button pesan = findViewById(R.id.konfirmasi_pesanan);
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_produk = user.getNama_produk();
                Integer gambar = user.getGambar();
                String status_pesanan = "1";
                String username = user.getUsername();
                updateProduk(nama_produk,gambar,username);
                simpanDataTransaksi(nama_produk,username);
                Toast.makeText(KeranjangActivity.this, "Pesanan dikonfirmasi, cek halaman ini secara berkala", Toast.LENGTH_SHORT).show();
            }
        });

        Button gantipesan = findViewById(R.id.ganti_pesanan);
        gantipesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setNama_produk(null);
                user.setGambar(0);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", gson.toJson(user));
                editor.apply();

                // Pindahkan ke halaman FoodActivity
                Intent intent = new Intent(KeranjangActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateProduk(String namaProduk, Integer gambar_produk, String username_pembeli) {
        String nama_produk = namaProduk;
        Integer gambar = gambar_produk;
        Integer status_pemesanan = 1;
        String username = username_pembeli;
        SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = preferences.getString("user", "");
        User user = gson.fromJson(userJson, User.class);
        user.setStatus_pesanan(status_pemesanan);
        user.setNama_produk(nama_produk);
        Integer status = user.getStatus_pesanan();
        Log.d("Response","Masuk ga nih:" + status);

        try {
            // ... (kode sebelumnya)

            // Buat objek JSON
            JSONObject updateData = new JSONObject();
            updateData.put("nama_produk", nama_produk);
            updateData.put("status_pemesanan", status_pemesanan);

            // Buat URL dengan parameter
            String url = "https://asia-south1.gcp.data.mongodb-api.com/app/application-2023-hckdc/endpoint/updateStatusPemesananPengguna?username=" + username + "&status_pemesanan=" + status_pemesanan +  "&nama_produk=" + nama_produk;

            // Inisialisasi RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, updateData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(KeranjangActivity.this, "Pesanan Sedang Diproses", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(KeranjangActivity.this, "Tunggu sampai mitra menkonfirmasi", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Tambahkan request ke dalam queue
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e("EditData", "Error creating JSON object", e);
            Toast.makeText(KeranjangActivity.this, "Terjadi kesalahan dalam pembuatan JSON", Toast.LENGTH_SHORT).show();
        }
    }
    public void simpanDataTransaksi(String namaProduk,String username_pembeli){
        String nama_produk = namaProduk;
        String status_pemesanan = "1";
        String username = username_pembeli;
        String urlEndpoint = "https://asia-south1.gcp.data.mongodb-api.com/app/application-2023-hckdc/endpoint/tambahMakananKePesanan";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
                Map map = new HashMap();
                map.put("nama_produk",nama_produk);
                map.put("username",username);
                map.put("status_pemesanan",status_pemesanan);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}