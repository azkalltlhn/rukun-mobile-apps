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
import com.example.ru_kun.DetailPeminjamanSelesaiActivity;
import com.example.ru_kun.R;
import com.example.ru_kun.model.Peminjaman;

import java.util.List;

public class PeminjamanAdapter extends RecyclerView.Adapter<PeminjamanAdapter.PeminjamanViewHolder> {
    private Context context;
    private List<Peminjaman> peminjamanList;

    public PeminjamanAdapter(Context context, List<Peminjaman> peminjamanList) {
        this.context = context;
        this.peminjamanList = peminjamanList;
    }

    @NonNull
    @Override
    public PeminjamanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardview_riwayat_selesai, parent, false);
        return new PeminjamanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeminjamanViewHolder holder, int position) {
        Peminjaman peminjaman = peminjamanList.get(position);

        // Set data to your views in card_layout
        holder.namaRuangan.setText(peminjaman.getNamaRuangan());
        holder.tglRuangan.setText(peminjaman.getTanggalPinjam());

        holder.wktMulai.setText(peminjaman.getWaktuMulai());
        holder.wktSelesai.setText(peminjaman.getWaktuSelesai());

        Glide.with(context)
                .load(peminjaman.getImage())
                .into(holder.imgRuangan);

        // Set click listener untuk membuka DetailPeminjamanSelesaiActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menginisialisasi Intent
                Intent intent = new Intent(context, DetailPeminjamanSelesaiActivity.class);
                // Mengirim objek Peminjaman sebagai extra melalui Intent
                intent.putExtra("peminjaman", peminjaman);
                // Memulai activity baru
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peminjamanList.size();
    }

    public static class PeminjamanViewHolder extends RecyclerView.ViewHolder {

        TextView namaRuangan, tglRuangan, wktMulai, wktSelesai;
        ImageView imgRuangan; // Example ImageView

        public PeminjamanViewHolder(@NonNull View itemView) {
            super(itemView);

            namaRuangan = itemView.findViewById(R.id.nama_ruangan);
            tglRuangan = itemView.findViewById(R.id.tgl_ruangan);
            wktMulai = itemView.findViewById(R.id.wkt_mulai);
            wktSelesai = itemView.findViewById(R.id.wkt_selesai);
            imgRuangan = itemView.findViewById(R.id.img_ruangan); // Example ImageView
        }
    }
}
