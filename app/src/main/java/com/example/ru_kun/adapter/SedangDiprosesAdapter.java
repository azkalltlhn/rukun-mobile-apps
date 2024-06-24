package com.example.ru_kun.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ru_kun.DetailPeminjamanDiprosesActivity;
import com.example.ru_kun.R;
import com.example.ru_kun.model.AjuanPeminjaman;
import java.util.List;

public class SedangDiprosesAdapter extends RecyclerView.Adapter<SedangDiprosesAdapter.ViewHolder> {
    private List<AjuanPeminjaman> ajuanList;
    private Context context;

    public SedangDiprosesAdapter(List<AjuanPeminjaman> ajuanList, Context context) {
        this.ajuanList = ajuanList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AjuanPeminjaman ajuan = ajuanList.get(position);

        // Set data from AjuanPeminjaman
        holder.namaRuangan.setText(ajuan.getNamaRuangan());
        holder.tglRuangan.setText(ajuan.getTanggalPinjam());
        holder.wktMulai.setText(ajuan.getWaktuMulai());
        holder.wktSelesai.setText(ajuan.getWaktuSelesai());

        Glide.with(context)
                .load(ajuan.getImage())
                .into(holder.imgRuangan);

        // Set click listener untuk membuka DetailPeminjamanDiprosesActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menginisialisasi Intent
                Intent intent = new Intent(context, DetailPeminjamanDiprosesActivity.class);
                // Mengirim objek AjuanPeminjaman sebagai extra melalui Intent
                intent.putExtra("ajuan", ajuan);
                // Memulai activity baru
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ajuanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgRuangan;
        public TextView namaRuangan, tglRuangan, wktMulai, wktSelesai;

        public ViewHolder(View itemView) {
            super(itemView);
            imgRuangan = itemView.findViewById(R.id.img_ruangan);
            namaRuangan = itemView.findViewById(R.id.nama_ruangan);
            tglRuangan = itemView.findViewById(R.id.tgl_ruangan);
            wktMulai = itemView.findViewById(R.id.wkt_mulai);
            wktSelesai = itemView.findViewById(R.id.wkt_selesai);

        }
    }
}
