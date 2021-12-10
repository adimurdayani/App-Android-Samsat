package com.kirana.samsat.network.presentase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kirana.samsat.R;
import com.kirana.samsat.network.model.AntrianModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.HolderData> {
    private Context context;
    private ArrayList<AntrianModel> dataRiwayat;

    public RiwayatAdapter(Context context, ArrayList<AntrianModel> dataRiwayat) {
        this.context = context;
        this.dataRiwayat = dataRiwayat;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_antrian,
                parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        AntrianModel antrianModel = dataRiwayat.get(position);

        holder.txt_tanggal.setText(antrianModel.getCreated_at());
        holder.txt_noantrian.setText(antrianModel.getNo_antrian());
        holder.txt_status.setText(antrianModel.getPanggil_antrian());

        int warna = context.getColor(R.color.orange2);
        if(Objects.equals(antrianModel.getPanggil_antrian(), "MENUNGGU")){
            warna = context.getColor(R.color.orange2);
        }else if (Objects.equals(antrianModel.getPanggil_antrian(), "TERPANGGIL")){
            warna = context.getColor(R.color.green);
        }
        holder.txt_status.setTextColor(warna);
    }

    @Override
    public int getItemCount() {
        return dataRiwayat.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<AntrianModel> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(dataRiwayat);
            } else {
                for (AntrianModel getantrian : dataRiwayat) {
                    if (getantrian.getNama().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getantrian.getNo_antrian().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getantrian.getCreated_at().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        searchList.add(getantrian);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = searchList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataRiwayat.clear();
            dataRiwayat.addAll((Collection<? extends AntrianModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchData() {
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView txt_noantrian, txt_tanggal, txt_status;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            txt_noantrian = itemView.findViewById(R.id.no_antrian);
            txt_tanggal = itemView.findViewById(R.id.tanggal);
            txt_status = itemView.findViewById(R.id.status);
        }
    }
}
