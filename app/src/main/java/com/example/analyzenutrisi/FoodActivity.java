package com.example.analyzenutrisi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {
    Context context = this;
    ImageView nav_home,nav_calc,keranjang;
    private List<Produk> produkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        getAllProduk();

        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Jika belum login, pindahkan ke halaman login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        keranjang = findViewById(R.id.btn_view);
        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keranjang = new Intent(FoodActivity.this, KeranjangActivity.class);
                startActivity(keranjang);
            }
        });


        nav_home = findViewById(R.id.nav_home);
        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent beranda = new Intent(FoodActivity.this,MainActivity.class);
                startActivity(beranda);
            }
        });

        nav_calc = findViewById(R.id.nav_kalkulator);
        nav_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kalkulator = new Intent(FoodActivity.this,KalkulatorActivity.class);
                startActivity(kalkulator);
            }
        });
    }
    private void getAllProduk() {
        String urlEndpoint = "https://asia-south1.gcp.data.mongodb-api.com/app/application-2023-hckdc/endpoint/cariSemuaMakanan";
        StringRequest sr = new StringRequest(Request.Method.GET, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsse", "onResponse: "+response);
                populateData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Produkt", "onErrorResponse: "+ error);
                Toast.makeText(FoodActivity.this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(sr);
    }

    private void populateData(String response) {
        try{
            JSONArray jsonArray = new JSONArray(response);
            produkList = new ArrayList<>();

            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String namaProduk = jsonObject.getString("nama_produk");
                String kalori = jsonObject.getString("kalori");
                String lemak = jsonObject.getString("lemak");
                String protein = jsonObject.getString("protein");
                String bahan = jsonObject.getString("bahan");
                String gambar = jsonObject.getString("gambar");
                String id = jsonObject.getString("_id");


                int imgID = context.getResources().getIdentifier(gambar, "drawable", context.getPackageName());
//                int detailIMG = context.getResources().getIdentifier(detailImg,"drawable",context.getPackageName());

                Produk produk = new Produk(id,namaProduk, kalori, lemak, protein, bahan, imgID);
                produkList.add(produk);
            }
            updateUI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUI() {
        GridView katalogNutrisiGridView = findViewById(R.id.katalognutrisi);

        if (produkList != null && !produkList.isEmpty()) {
            ProdukAdapter nutrisiAdapter = new ProdukAdapter(this, produkList);
            katalogNutrisiGridView.setAdapter(nutrisiAdapter);

            katalogNutrisiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Produk selectedProduk = produkList.get(position);
                    Log.d("position", "onItemClick: " + selectedProduk);
                    // Handle item click, e.g., open detail page
                }
            });
        } else {
            // Handle the case when the product list is empty
            // For example, display a message or update UI accordingly
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
        }
    }


//    private List<Produk> filterByKategori() {
//
//    }
//
//    public void goToKeranjang(View view){
//        Produk userModel = (Produk) getIntent().getSerializableExtra("userModel");
//        Intent i = new Intent(FoodActivity.this, Keranjang.class);
//        i.putExtra("userModel", userModel);
//        startActivity(i);
//    }
//    public void backTo(View view){
//        Produk userModel = (Produk) getIntent().getSerializableExtra("userModel");
//        Intent i = new Intent(FoodActivity.this, Beranda.class);
//        i.putExtra("userModel", userModel);
//        startActivity(i);
//    }
}