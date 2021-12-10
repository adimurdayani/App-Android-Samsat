package com.kirana.samsat.network.presentase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kirana.samsat.R;
import com.kirana.samsat.network.model.Informasi;
import com.kirana.samsat.network.model.PajakModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class InformasiAdapter extends RecyclerView.Adapter<InformasiAdapter.HolderData> {
    private Context context;
    private ArrayList<Informasi> informasis;

    public InformasiAdapter(Context context, ArrayList<Informasi> informasis) {
        this.context = context;
        this.informasis = informasis;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_informasi, parent, false);
        return new HolderData(view);
    }

    @SuppressLint( "SetTextI18n" )
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Informasi informasi = informasis.get(position);
        holder.jadwal_buka.setText(informasi.getJadwal_buka() + " - " + informasi.getJam_buka());
        holder.jadwal_tutup.setText(informasi.getJadwal_tutup() + " - " + informasi.getJam_tutup());
    }

    @Override
    public int getItemCount() {
        return informasis.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView jadwal_buka, jadwal_tutup;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            jadwal_buka = itemView.findViewById(R.id.jadwal_buka);
            jadwal_tutup = itemView.findViewById(R.id.jadwal_tutup);
        }
    }
}
