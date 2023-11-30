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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class KalkulatorActivity extends AppCompatActivity {
    ImageView nav_home,nav_menu;
    EditText inputBahan;
    TextView tvResult,viewAyam,setAyam,setSalad,viewSalad;
    Button btnAnalisis;
    String namaBahan;
    private final String appid = "4082c5e0";
    private final String appkey = "404181e2ca23be658f1523847938a4fb";
    private final String url = "https://api.edamam.com/api/nutrition-data?app_id=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulator);tvResult = findViewById(R.id.tv_result);
        inputBahan = findViewById(R.id.et_analisis);
        btnAnalisis = findViewById(R.id.btn_analisis);
        viewAyam = findViewById(R.id.btn_nay);
        setAyam = findViewById(R.id.nut_ayam);
        setSalad = findViewById(R.id.sal_buah);
        viewSalad = findViewById(R.id.btn_sbh);
        nav_menu = findViewById(R.id.nav_menu);
        nav_home = findViewById(R.id.nav_home);
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Jika belum login, pindahkan ke halaman login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        viewAyam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setAyam.getVisibility() == View.GONE) {
                    setAyam.setVisibility(View.VISIBLE);
                    viewAyam.setText("Tutup Informasi Nutrisi");
                } else {
                    setAyam.setVisibility(View.GONE);
                    viewAyam.setText("Lihat Kalori Sekarang");
                }
            }
        });

        viewSalad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setSalad.getVisibility() == View.GONE) {
                    setSalad.setVisibility(View.VISIBLE);
                    viewSalad.setText("Tutup Informasi Nutrisi");
                } else {
                    setSalad.setVisibility(View.GONE);
                    viewSalad.setText("Lihat Kalori Sekarang");
                }
            }
        });

        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gomenu = new Intent(KalkulatorActivity.this, FoodActivity.class );
                startActivity(gomenu);
            }
        });

        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gohome = new Intent(KalkulatorActivity.this, MainActivity.class );
                startActivity(gohome);
            }
        });

        btnAnalisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaBahan = inputBahan.getText().toString();
                if (namaBahan.isEmpty()){
                    Toast.makeText(KalkulatorActivity.this, "Isi bahan-bahannya dulu yaa", Toast.LENGTH_SHORT).show();
                } else{
                    analisisbahan(namaBahan);
                }
                tvResult.setVisibility(View.VISIBLE);
            }
        });
    }

    private void analisisbahan(String namaBahan) {
        String edamam = url + appid + "&app_key=" + appkey + "&ingr=" + namaBahan;
        StringRequest getedamam = new StringRequest(Request.Method.GET, edamam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("inputnih",response);
                try {
                    // Ambil data nutrisi dari respons JSON

                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("response",response);

                    // akses objek total nutrisi
                    JSONObject totalNutrients = jsonResponse.getJSONObject("totalNutrients");

                    // Data Kalori
                    JSONObject cal = totalNutrients.getJSONObject("ENERC_KCAL");
                    Double qcal = cal.getDouble("quantity");
                    String ucal = cal.getString("unit");
                    Log.d("kalori","Total kalori :" + qcal + ucal);

                    // Data Lemak
                    JSONObject fat = totalNutrients.getJSONObject("FAT");
                    Double qfat = fat.getDouble("quantity");
                    String ufat = fat.getString("unit");
                    Log.d("lemak","Total lemak :" + qfat + ufat);

                    // Data Lemak Jenuh
                    JSONObject fasat = totalNutrients.getJSONObject("FASAT");
                    Double qfast = fasat.getDouble("quantity");
                    String ufast = fasat.getString("unit");
                    Log.d("lemak jenuh","Total lemak jenuh :" + qfast + ufast);

                    // Data Protein
                    JSONObject prot = totalNutrients.getJSONObject("PROCNT");
                    Double qprot = prot.getDouble("quantity");
                    String uprot = prot.getString("unit");
                    Log.d("protein","Total protein :" + qprot + uprot);

                    // Data Serat
                    JSONObject fib = totalNutrients.getJSONObject("FIBTG");
                    Double qfib = fib.getDouble("quantity");
                    String ufib = fib.getString("unit");
                    Log.d("serat","Total serat :" + qfib + ufib);

                    // Data Vitamin D
                    JSONObject vitD = totalNutrients.getJSONObject("FIBTG");
                    Double qvD = vitD.getDouble("quantity");
                    String uvD =vitD.getString("unit");
                    Log.d("vitamin","Vitamin D :" + qvD + uvD);

                    // Format nilai decimal
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    // Masukkan nilainya
                    String resultText = "Kalori: " + decimalFormat.format(qcal) + ucal + "\n"
                            + "Lemak: " + decimalFormat.format(qfat) + ufat + "\n"
                            + "Lemak Jenuh: " + decimalFormat.format(qfast) + ufast + "\n"
                            + "Protein: " + decimalFormat.format(qprot) + uprot + "\n"
                            + "Serat: " + decimalFormat.format(qfib) + ufib + "\n"
                            + "Vitamin D: " + decimalFormat.format(qvD) + uvD;

                    tvResult.setText(resultText);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getedamam);
    }

}