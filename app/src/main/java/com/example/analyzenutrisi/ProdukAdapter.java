package com.example.analyzenutrisi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

public class ProdukAdapter extends BaseAdapter {
    private Context context;
    private List<Produk> produkList;

    public ProdukAdapter(Context context, List<Produk> produkList) {
        this.context = context;
        this.produkList = produkList;
    }

    @Override
    public int getCount() {
        return produkList.size();
    }

    @Override
    public Object getItem(int position) {
        return produkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        Produk produk = produkList.get(position);


        TextView namaProduk = convertView.findViewById(R.id.nama_prd);
        Button btnPesan = convertView.findViewById(R.id.btn_pesan);
        ImageView gambarProduk = convertView.findViewById(R.id.image_prd);

        namaProduk.setText(produk.nama_produk);
        gambarProduk.setImageResource(produk.gambar);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_produk = produk.nama_produk;
                Integer gambar = produk.gambar;

                // Mendapatkan User dari SharedPreferences
                SharedPreferences preferences = context.getSharedPreferences("userModel", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String userJson = preferences.getString("user", "");
                User user = gson.fromJson(userJson, User.class);

                // Mengecek apakah nama_produk sudah ada dalam keranjang
                if (user.getNama_produk() != null && user.getNama_produk().equals(nama_produk)) {
                    // Nama_produk sudah ada, tampilkan pesan
                    Log.d("testproduk","ada gaa" + user);
                    Toast.makeText(context, "Anda sudah menambahkan produk lain, silahkan periksa ke keranjang untuk mengkonfirmasi pembelian.", Toast.LENGTH_SHORT).show();
                } else {
                    // Menyimpan informasi produk ke dalam User
                    user.setNama_produk(nama_produk);
                    user.setGambar(gambar);

                    // Menyimpan User yang telah diupdate ke dalam SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user", gson.toJson(user));
                    editor.apply();

                }
            }

            });

        return convertView;
    }
}
