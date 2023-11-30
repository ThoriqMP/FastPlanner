package com.example.analyzenutrisi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class PesananActivity extends AppCompatActivity {
    TextView status;
    Button selesai;
    ImageView back;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        back = findViewById(R.id.buttonKembali);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(PesananActivity.this, FoodActivity.class);
                startActivity(back);
            }
        });

        // initialise the layout
        webView = findViewById(R.id.webvidew);

        // enable the javascript to load the url
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // add the url of gif
        webView.loadUrl("https://giphy.com/embed/tXL4FHPSnVJ0A");

        SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = preferences.getString("user", "");
        User user = gson.fromJson(userJson, User.class);

        // Check the status_pemesanan and update the TextView accordingly
        int statusPesanan = user.getStatus_pesanan();
        updateStatusTextView(statusPesanan);

    }

    private void updateStatusTextView(int statusPesanan) {
        status = findViewById(R.id.informasi);
        selesai = findViewById(R.id.selesaikan_pesanan);
        switch (statusPesanan) {
            case 0:
                status.setText("Belum ada pesanan. Silakan pesan produk.");
                selesai.setVisibility(View.GONE);
                break;
            case 1:
                status.setText("Pesanan sedang dalam konfirmasi mitra.");
                selesai.setVisibility(View.GONE);
                break;
            case 2:
                status.setText("Pesanan sedang dibuat.");
                selesai.setVisibility(View.GONE);
                break;
            case 3:
                status.setText("Pesanan sudah dikirim.");
                selesai.setVisibility(View.VISIBLE);
                selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PesananActivity.this, "Selesaikan pesanan", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                status.setText("Status pesanan tidak valid.");
                break;
        }
    }
}