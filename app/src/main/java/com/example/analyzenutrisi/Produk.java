package com.example.analyzenutrisi;

public class Produk {
    String id,nama_produk, kalori, lemak, protein, bahan;
    int gambar;


    public Produk(String id, String nama_produk, String kalori, String lemak, String protein, String bahan, int gambar) {
        this.id = id;
        this.nama_produk = nama_produk;
        this.kalori = kalori;
        this.lemak = lemak;
        this.protein = protein;
        this.bahan = bahan;
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public String getKalori() {
        return kalori;
    }

    public String getLemak() {
        return lemak;
    }

    public String getProtein() {
        return protein;
    }

    public String getBahan() {
        return bahan;
    }

    public int getGambar() {
        return gambar;
    }

}
