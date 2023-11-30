package com.example.analyzenutrisi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.analyzenutrisi.FoodActivity;
import com.example.analyzenutrisi.KalkulatorActivity;
import com.example.analyzenutrisi.LoginActivity;
import com.example.analyzenutrisi.PuasaActivity;
import com.example.analyzenutrisi.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences namaSP, waktuSP, hargaSP;
    ListView plan;
    private List<PlanModel> planList;
    private PlanAdapter planAdapter;
    private boolean pembelianSudahDilakukan = false;

    TextView namauser, status_plan;

    ImageView nav_calc, nav_menu, btnprofile;
    Button puasa;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUserData();


        if (!getLoginStatus()) {
            // Jika belum login, pindahkan ke halaman login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
//        Mulai Puasa
        puasa = findViewById(R.id.start_puasa);
        puasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                boolean isTimerRunning = sharedPreferences.getBoolean("waktu_berjalan", false);

                // Menentukan ke mana harus diarahkan sesuai dengan status waktu
                Class<?> destinationClass = isTimerRunning ? schedule_start14.class : schedule_page.class;
                Intent intent = new Intent(MainActivity.this, destinationClass);
                startActivity(intent);
            }
        });

//        Menu
        nav_menu = findViewById(R.id.nav_menu);
        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan User dari SharedPreferences
                SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
                Gson gson = new Gson();
                String userJson = preferences.getString("user", "");
                User user = gson.fromJson(userJson, User.class);

                // Mendapatkan status plan dari User
                Integer statusPlan = user.getStatus_plan();

// Melakukan pengecekan status plan
                if (statusPlan == null || statusPlan == 0 || statusPlan == 1 || statusPlan == 3) {
                    Toast.makeText(MainActivity.this, "Anda harus membeli premium plan terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else if (statusPlan == 2) {
                    Intent gomenu = new Intent(MainActivity.this, FoodActivity.class);
                    startActivity(gomenu);
                } else {
                    // This block will be executed if statusPlan is not 0, 1, 2, or 3
                    // You can add additional handling here if needed
                    Toast.makeText(MainActivity.this, "Anda harus membeli premium plan terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        Kalkulator
        nav_calc = findViewById(R.id.nav_kalkulator);
        nav_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gocalc = new Intent(MainActivity.this, KalkulatorActivity.class);
                startActivity(gocalc);
            }

        });
//        Profile
        btnprofile = findViewById(R.id.profile_menu);
        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile  = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profile);
            }
        });
        ListView listView = findViewById(R.id.listView);

        // Initialize the data list and adapter
        planList = new ArrayList<>();
        planAdapter = new PlanAdapter(this, R.layout.list_item, planList);

        // Set the adapter to the ListView
        listView.setAdapter(planAdapter);
        fetchDataAndPopulateListView();

        plan = findViewById(R.id.listView);
        plan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Mendapatkan User dari SharedPreferences
                SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
                Gson gson = new Gson();
                String userJson = preferences.getString("user", "");
                User user = gson.fromJson(userJson, User.class);
                PlanModel selectedPlan = planList.get(position);

                Integer statusp = user.getStatus_plan();
                Log.d("response","nilai status:" + statusp);

                // Now you can access the data of the selected plan
                String namaPlan = selectedPlan.getNama();
                String periode = selectedPlan.getDesc();

                // Mendapatkan status plan dari User
                if (pembelianSudahDilakukan) {
                    Toast.makeText(MainActivity.this, "Pembelian sudah dilakukan. Tidak dapat melakukan pembelian lagi.", Toast.LENGTH_SHORT).show();
                } else {

                    // Mendapatkan status plan dari User
                    int statusPlan = user.getStatus_plan();
                    if (statusPlan == 0) {
                        tambahTransaksi(namaPlan,user.getUsername(),periode);
                        user.setStatus_plan(3);
                        pembelianSudahDilakukan = true; // Setelah pembelian, nonaktifkan klik
                        Toast.makeText(MainActivity.this, "Saat ini pembayaran sedang dialihkan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://fastplanner.rf.gd/premium"));
                        startActivity(intent);
                    } if (statusPlan == 1){
                        Toast.makeText(MainActivity.this, "Pembayaran sedang dalam konfirmasi admin", Toast.LENGTH_SHORT).show();
                    } if (statusPlan == 2){
                        Toast.makeText(MainActivity.this, "Anda dalam masa aktif plan", Toast.LENGTH_SHORT).show();
                    }   if (statusPlan == 3){
                        Toast.makeText(MainActivity.this, "Transaksi galat, silahkan lakukan ulang di website kami", Toast.LENGTH_SHORT).show();
                    }
                    // ... Logika pembelian lainnya
                }
            }
        });
    }

    // Cek login

    // Method untuk menyimpan status login di SharedPreferences
    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    // Method untuk mendapatkan status login dari SharedPreferences
    private void logoutUser() {
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
    private void fetchDataAndPopulateListView() {
        String urlEndpoint = "https://ap-southeast-1.aws.data.mongodb-api.com/app/fastplanner-app-ekutp/endpoint/getAllPlan";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsPlan", "onResponse: "+response);
                getJsonResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private  void tambahTransaksi(String nama_plan, String username, String periode) {
        String urlEndpoint = "https://asia-south1.gcp.data.mongodb-api.com/app/application-2023-hckdc/endpoint/addPlan";
        String bukti = "Transaksi Pengguna Mobile";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
                Gson gson = new Gson();
                String userJson = preferences.getString("user", "");
                User user = gson.fromJson(userJson, User.class);

                user.setStatus_plan(3);

                // Simpan kembali objek User yang diperbarui ke SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", gson.toJson(user));
                editor.apply();
                Toast.makeText(MainActivity.this, "Untuk mencegah pembatalan, silahkan lanjutkan transaksi di website", Toast.LENGTH_SHORT).show();
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

                map.put("nama_plan",nama_plan);
                map.put("username",username);
                map.put("bukti_pembayaran", bukti);
                map.put("periode",periode);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getJsonResponse(String jsonResponse) {
        try {
            JSONArray jsonArrayRes = new JSONArray(jsonResponse);
            planList.clear(); // Clear the existing data if needed

            for (int i = 0; i < jsonArrayRes.length(); i++) {
                JSONObject jsonObject = jsonArrayRes.getJSONObject(i);
                String nama = jsonObject.getString("nama");
                String desc = jsonObject.getString("deskripsi");
                String harga = jsonObject.getString("harga");

                namaSP = getSharedPreferences("plan",MODE_PRIVATE);
                SharedPreferences.Editor eNamaSP = namaSP.edit().putString("nama plan", nama);

                PlanModel planModel = new PlanModel(nama, desc, harga);
                planList.add(planModel);
            }

            // Notify the adapter that the data has changed
            planAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void checkUserData() {
        SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
        Gson gson = new Gson();

        // Mendapatkan nilai JSON yang disimpan
        String userJson = preferences.getString("user", "");

        if (userJson.isEmpty()) {
            // Data tidak ada, tampilkan toast dan alihkan ke halaman login
            Toast.makeText(this, "Data gagal dimuat, Anda akan dialihkan ke halaman login", Toast.LENGTH_SHORT).show();

            // Clear objek class User
            clearUserData();

            // Intent ke halaman login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish current activity to prevent back navigation
        } else {
            // Data ada, lanjutkan dengan logika di atas
            User user = gson.fromJson(userJson, User.class);
            namauser = findViewById(R.id.nama_user);
            status_plan = findViewById(R.id.status_user);
            namauser.setText(user.getNama());
            Integer statusp = user.getStatus_plan();

            if (statusp == 0) {
                String nilai_statu = "Dietisien Biasa";
                status_plan.setText("status : "+ nilai_statu);
            }if (statusp == 1) {
                String nilai_statu = "Menunggu Konfirmasi";
                status_plan.setText("status : "+ nilai_statu);
                status_plan.setTextColor(getResources().getColor(R.color.warna_kuning));
            }if (statusp == 2) {
                String nilai_statu = "Dietisien Pro";
                status_plan.setText("status : "+ nilai_statu);
                status_plan.setTextColor(getResources().getColor(R.color.warna_hijau));
            }if (statusp == 3) {
                String nilai_statu = "Galat";
                status_plan.setText("status : "+ nilai_statu);
                status_plan.setTextColor(getResources().getColor(R.color.warna_merah));
            }
            else {
                String nilai_statu = "Dietisien Biasa";
                status_plan.setText("status : " + nilai_statu);
            }
        }
    }

    private void clearUserData() {
        // Clear objek class User
        SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user");
        editor.apply();
    }

}