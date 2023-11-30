package com.example.analyzenutrisi;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Locale;

public class finish_14 extends AppCompatActivity {
    private TextView waktu;
    private ImageView back;
    private TextView waktu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish14);

        waktu = findViewById(R.id.waktu); // Sesuaikan dengan ID yang ada di layout Anda
        waktu2 = findViewById(R.id.waktu2); // Sesuaikan dengan ID yang ada di layout Anda

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(finish_14.this, MainActivity.class);
                startActivity(back);
            }
        });
        Button setButton = findViewById(R.id.btn_set);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finish_14.this, schedule_page.class);
                startActivity(intent);
            }
        });

        String selectedDateTime = getIntent().getStringExtra("selectedDateTime");
        String selectedEndDateTime = getIntent().getStringExtra("selectedEndDateTime");

        waktu.setText(selectedDateTime != null ? selectedDateTime : "No data");
        waktu2.setText(selectedEndDateTime != null ? selectedEndDateTime : "No data");

    }
}
