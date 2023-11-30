package com.example.analyzenutrisi;


public class PlanModel {
    String nama,desc, harga;

    // Perbaikan: Nama konstruktor yang benar

    public PlanModel(String nama, String desc, String harga) {
        this.nama = nama;
        this.desc = desc;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public String getDesc() {
        return desc;
    }

    public String getHarga() {
        return harga;
    }
}

