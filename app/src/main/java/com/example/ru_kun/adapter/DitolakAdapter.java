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
import com.example.ru_kun.DetailPeminjamanDitolakActivity;
import com.example.ru_kun.R;
import com.example.ru_kun.model.PeminjamanDitolak;
import java.util.List;

public class DitolakAdapter extends RecyclerView.Adapter<DitolakAdapter.ViewHolder> {
    private List<PeminjamanDitolak> ditolakList;
    private Context context;

    public DitolakAdapter(List<PeminjamanDitolak> ditolakList, Context context) {
        this.ditolakList = ditolakList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_riwayat_ditolak, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PeminjamanDitolak ditolak = ditolakList.get(position);

        // Set data from AjuanPeminjaman
        holder.namaRuangan.setText(ditolak.getNamaRuangan());
        holder.tglRuangan.setText(ditolak.getTanggalPinjam());
        holder.wktMulai.setText(ditolak.getWaktuMulai());
        holder.wktSelesai.setText(ditolak.getWaktuSelesai());
        holder.catatan.setText(ditolak.getCatatan());

        Glide.with(context)
                .load(ditolak.getImage())
                .into(holder.imgRuangan);

        // Set click listener untuk membuka DetailPeminjamanDiprosesActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menginisialisasi Intent
                Intent intent = new Intent(context, DetailPeminjamanDitolakActivity.class);
                // Mengirim objek AjuanPeminjaman sebagai extra melalui Intent
                intent.putExtra("ditolak", ditolak);
                // Memulai activity baru
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ditolakList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgRuangan;
        public TextView namaRuangan, tglRuangan, wktMulai, wktSelesai, catatan;

        public ViewHolder(View itemView) {
            super(itemView);
            imgRuangan = itemView.findViewById(R.id.img_ruangan);
            namaRuangan = itemView.findViewById(R.id.nama_ruangan);
            tglRuangan = itemView.findViewById(R.id.tgl_ruangan);
            wktMulai = itemView.findViewById(R.id.wkt_mulai);
            wktSelesai = itemView.findViewById(R.id.wkt_selesai);
            catatan = itemView.findViewById(R.id.catatan);
        }
    }
}
