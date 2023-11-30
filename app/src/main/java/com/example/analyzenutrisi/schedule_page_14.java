package com.example.analyzenutrisi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class schedule_page_14 extends AppCompatActivity {
    private TextView waktu;
    private TextView waktu2;
    ImageView back;
    private Calendar selectedDateTime = Calendar.getInstance();
    private Calendar selectedEndDateTime = Calendar.getInstance(); // Menyimpan waktu selesai puasa
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_page14);

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(schedule_page_14.this,schedule_page.class);
                startActivity(back);
            }
        });

        Button btnStartPuasa = findViewById(R.id.btn_startf);
        btnStartPuasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScheduleStart14Activity();
            }
        });

        Button btnEditJadwal = findViewById(R.id.btn_editjadwal);
        btnEditJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        waktu = findViewById(R.id.waktu);
        waktu2 = findViewById(R.id.waktu2);
    }

    private void startScheduleStart14Activity() {
        Intent intent = new Intent(schedule_page_14.this, schedule_start14.class);

        // Mengambil waktu pertama yang dipilih
        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE));
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDateTime.get(Calendar.DAY_OF_MONTH), selectedDateTime.get(Calendar.MONTH) + 1, selectedDateTime.get(Calendar.YEAR));
        String dateTime = selectedDate + " " + selectedTime;

        // Mengambil waktu kedua dari selectedEndDateTime
        String endTime = String.format(Locale.getDefault(), "%02d:%02d", selectedEndDateTime.get(Calendar.HOUR_OF_DAY), selectedEndDateTime.get(Calendar.MINUTE));
        String endDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedEndDateTime.get(Calendar.DAY_OF_MONTH), selectedEndDateTime.get(Calendar.MONTH) + 1, selectedEndDateTime.get(Calendar.YEAR));
        String endDateTime = endDate + " " + endTime;

        // Menambahkan kedua waktu ke Intent
        intent.putExtra("selectedDateTime", dateTime);
        intent.putExtra("selectedEndDateTime", endDateTime);

        startActivity(intent);
    }


    // Implementasikan method untuk menampilkan dialog pemilihan tanggal dan waktu
    private void showDateTimePicker() {
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDateTime.set(Calendar.YEAR, year);
            selectedDateTime.set(Calendar.MONTH, month);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Setelah memilih tanggal, tampilkan dialog untuk memilih waktu
            showTimePicker();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedDateTime.set(Calendar.MINUTE, minute);

            // Simpan waktu yang dipilih ke dalam variabel lain untuk dihitung waktu selesai puasa
            selectedEndDateTime.setTimeInMillis(selectedDateTime.getTimeInMillis());
            selectedEndDateTime.add(Calendar.HOUR_OF_DAY, 14);

            // Update TextView waktu dengan waktu yang dipilih
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE));
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDateTime.get(Calendar.DAY_OF_MONTH), selectedDateTime.get(Calendar.MONTH) + 1, selectedDateTime.get(Calendar.YEAR));

            String displayText = selectedDate + " " + selectedTime;
            waktu.setText(displayText);

            // Lakukan sesuatu dengan waktu selesai puasa yang didapat
            String endTime = String.format(Locale.getDefault(), "%02d:%02d", selectedEndDateTime.get(Calendar.HOUR_OF_DAY), selectedEndDateTime.get(Calendar.MINUTE));
            String endDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedEndDateTime.get(Calendar.DAY_OF_MONTH), selectedEndDateTime.get(Calendar.MONTH) + 1, selectedEndDateTime.get(Calendar.YEAR));

            String displayEndTime = endDate + " " + endTime;
            waktu2.setText(displayEndTime);

            // Atur ulang handler untuk update setiap 14 jam
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedEndDateTime.add(Calendar.HOUR_OF_DAY, 14);
                    String endTime = String.format(Locale.getDefault(), "%02d:%02d", selectedEndDateTime.get(Calendar.HOUR_OF_DAY), selectedEndDateTime.get(Calendar.MINUTE));
                    String endDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedEndDateTime.get(Calendar.DAY_OF_MONTH), selectedEndDateTime.get(Calendar.MONTH) + 1, selectedEndDateTime.get(Calendar.YEAR));

                    String displayEndTime = endDate + " " + endTime;
                    waktu2.setText(displayEndTime);

                    // Atur ulang handler untuk update selanjutnya setelah 14 jam
                    handler.postDelayed(this, 14 * 60 * 60 * 1000);
                }
            }, 14 * 60 * 60 * 1000); // Pertama kali dijalankan setelah 14 jam
        }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }
}
