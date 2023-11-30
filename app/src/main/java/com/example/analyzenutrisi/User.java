package com.example.analyzenutrisi;


import java.io.Serializable;

public class User implements Serializable {
    
    String id;
    String nama;
    String username;
    String email;
    String jenis_kelamin;
    String no_telepon;
    String nama_produk;

    Integer status_pesanan;


    public Integer getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(Integer status_pesanan) {
        this.status_pesanan = status_pesanan;
    }


    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public Integer getGambar() {
        return gambar;
    }

    public void setGambar(Integer gambar) {
        this.gambar = gambar;
    }

    Integer status_plan, berat_badan, tinggi_badanl, usia,gambar;

    public User(String id,String nama, String username, String email, String jenis_kelamin, String no_telepon, Integer status_plan, Integer berat_badan, Integer tinggi_badanl, Integer usia,Integer status_pesanan) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.email = email;
        this.jenis_kelamin = jenis_kelamin;
        this.no_telepon = no_telepon;
        this.status_plan = status_plan;
        this.berat_badan = berat_badan;
        this.tinggi_badanl = tinggi_badanl;
        this.usia = usia;
        this.status_pesanan = status_pesanan;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public void setStatus_plan(Integer status_plan) {
        this.status_plan = status_plan;
    }

    public void setBerat_badan(Integer berat_badan) {
        this.berat_badan = berat_badan;
    }

    public void setTinggi_badanl(Integer tinggi_badanl) {
        this.tinggi_badanl = tinggi_badanl;
    }

    public void setUsia(Integer usia) {
        this.usia = usia;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public Integer getStatus_plan() {
        return status_plan;
    }

    public Integer getBerat_badan() {
        return berat_badan;
    }

    public Integer getTinggi_badanl() {
        return tinggi_badanl;
    }

    public Integer getUsia() {
        return usia;
    }

}
