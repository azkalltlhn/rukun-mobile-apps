package com.example.ru_kun.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ru_kun.DetailRuanganActivity;
import com.example.ru_kun.FormPinjamActivity;
import com.example.ru_kun.R;
import com.example.ru_kun.model.Ruangan;
import java.util.ArrayList;
import java.util.List;

public class RuanganAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int VIEW_TYPE_HOME = 0;
    private static final int VIEW_TYPE_PINJAM = 1;

    private List<Ruangan> ruanganList;
    private List<Ruangan> ruanganListFull; // List yang menyimpan semua data ruangan
    private Context context;
    private int viewType;

    public RuanganAdapter(Context context, List<Ruangan> ruanganList, int viewType) {
        this.context = context;
        this.ruanganList = ruanganList;
        this.ruanganListFull = new ArrayList<>(ruanganList);
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HOME) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cardview_home, parent, false);
            return new RuanganHomeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cardview_pinjam, parent, false);
            return new RuanganPinjamViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ruangan ruangan = ruanganList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_HOME) {
            RuanganHomeViewHolder homeHolder = (RuanganHomeViewHolder) holder;
            homeHolder.namaRuangan.setText(ruangan.getNama());
            homeHolder.kondisiRuangan.setText(ruangan.getKondisi());
            homeHolder.tglRuangan.setText(ruangan.getTanggal());

            Glide.with(context)
                    .load(ruangan.getImage())
                    .into(homeHolder.imgRuangan);

            homeHolder.buttonLihatDetail.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailRuanganActivity.class);
                intent.putExtra("ruangan", ruangan);
                context.startActivity(intent);
            });
        } else {
            RuanganPinjamViewHolder pinjamHolder = (RuanganPinjamViewHolder) holder;
            pinjamHolder.namaRuangan.setText(ruangan.getNama());
            pinjamHolder.tglRuangan.setText(ruangan.getTanggal());
            pinjamHolder.waktuTersedia.setText(ruangan.getWaktuTersedia());

            Glide.with(context)
                    .load(ruangan.getImage())
                    .into(pinjamHolder.imgRuangan);

            pinjamHolder.buttonPinjam.setOnClickListener(v -> {
                Intent intent = new Intent(context, FormPinjamActivity.class);
                intent.putExtra("ruangan", ruangan);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return ruanganList.size();
    }

    public static class RuanganHomeViewHolder extends RecyclerView.ViewHolder {
        TextView namaRuangan, kondisiRuangan, tglRuangan;
        ImageView imgRuangan;
        Button buttonLihatDetail;

        public RuanganHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            namaRuangan = itemView.findViewById(R.id.nama_ruangan);
            kondisiRuangan = itemView.findViewById(R.id.kondisi_ruangan);
            tglRuangan = itemView.findViewById(R.id.tgl_ruangan);
            imgRuangan = itemView.findViewById(R.id.img_ruangan);
            buttonLihatDetail = itemView.findViewById(R.id.ButtonLihatDetail);
        }
    }

    public static class RuanganPinjamViewHolder extends RecyclerView.ViewHolder {
        TextView namaRuangan, tglRuangan, waktuTersedia;
        ImageView imgRuangan;
        Button buttonPinjam;

        public RuanganPinjamViewHolder(@NonNull View itemView) {
            super(itemView);
            namaRuangan = itemView.findViewById(R.id.nama_ruangan);
            tglRuangan = itemView.findViewById(R.id.tgl_ruangan);
            waktuTersedia = itemView.findViewById(R.id.waktu_tersedia);
            imgRuangan = itemView.findViewById(R.id.img_ruangan);
            buttonPinjam = itemView.findViewById(R.id.buttonPinjam);
        }
    }

    @Override
    public Filter getFilter() {
        return ruanganFilter;
    }

    private Filter ruanganFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Ruangan> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ruanganListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Ruangan ruangan : ruanganListFull) {
                    if (ruangan.getNama().toLowerCase().contains(filterPattern)) {
                        filteredList.add(ruangan);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ruanganList.clear();
            ruanganList.addAll((List<Ruangan>) results.values);
            notifyDataSetChanged();
        }
    };
}