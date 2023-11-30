package com.example.analyzenutrisi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class schedule_page extends AppCompatActivity {

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_page);
        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(schedule_page.this,MainActivity.class);
                startActivity(back);
            }
        });

        Button chooseButton = findViewById(R.id.btn_choose);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ketika tombol "Choose" ditekan, arahkan ke halaman schedule_page14.xml
                Intent intent = new Intent(schedule_page.this, schedule_page_14.class);
                startActivity(intent);
            }
        });

        Button chooseButton2 = findViewById(R.id.btn_choose2);
        chooseButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ketika tombol "Choose 2" ditekan, arahkan ke halaman selanjutnya
                Intent intent = new Intent(schedule_page.this, schedule_page_14.class); // Ganti dengan nama activity yang sesuai
                startActivity(intent);
            }
        });

        Button chooseButton3 = findViewById(R.id.btn_choose3);
        chooseButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ketika tombol "Choose 3" ditekan, arahkan ke halaman selanjutnya
                Intent intent = new Intent(schedule_page.this, schedule_page_14.class); // Ganti dengan nama activity yang sesuai
                startActivity(intent);
            }
        });

        // Mengambil referensi view dengan ID schedule_rectangle
        View scheduleRectangle = findViewById(R.id.schedule_rectangle);

        // Menambahkan listener untuk menangani klik pada view
        scheduleRectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kode yang akan dijalankan saat view diklik

                // Membuat Intent untuk berpindah ke aktivitas schedule14
                Intent intent = new Intent(getApplicationContext(), schedule_page_14.class);

                // Menjalankan Intent untuk berpindah ke Schedule14Activity
                startActivity(intent);
            }
        });

        View scheduleRectangle2 = findViewById(R.id.schedule_rectangle2);

        // Menambahkan listener untuk menangani klik pada view
        scheduleRectangle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kode yang akan dijalankan saat view diklik

                // Membuat Intent untuk berpindah ke aktivitas schedule14
                Intent intent = new Intent(getApplicationContext(), schedule_page_14.class);

                // Menjalankan Intent untuk berpindah ke Schedule14Activity
                startActivity(intent);
            }
        });

        View scheduleRectangle3 = findViewById(R.id.schedule_rectangle3);

        // Menambahkan listener untuk menangani klik pada view
        scheduleRectangle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kode yang akan dijalankan saat view diklik

                // Membuat Intent untuk berpindah ke aktivitas schedule14
                Intent intent = new Intent(getApplicationContext(), schedule_page_14.class);

                // Menjalankan Intent untuk berpindah ke Schedule14Activity
                startActivity(intent);
            }
        });

    }
}
