package com.kirana.samsat.network.presentase;

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
import com.kirana.samsat.network.model.PajakModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class PajakAdapter extends RecyclerView.Adapter<PajakAdapter.HolderData> {
    private Context context;
    private ArrayList<PajakModel> datapajak;

    public PajakAdapter(Context context, ArrayList<PajakModel> datapajak) {
        this.context = context;
        this.datapajak = datapajak;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pajak, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        PajakModel pajakModel = datapajak.get(position);

        String total = formatRupiah(Double.parseDouble(String.valueOf(pajakModel.getTotal())));
        String pkb_pokok = formatRupiah(Double.parseDouble(String.valueOf(pajakModel.getPkb_pokok())));
        String pkb_denda = formatRupiah(Double.parseDouble(String.valueOf(pajakModel.getPkb_denda())));
        String jr_pokok = formatRupiah(Double.parseDouble(String.valueOf(pajakModel.getJr_pokok())));
        String jr_denda = formatRupiah(Double.parseDouble(String.valueOf(pajakModel.getJr_denda())));
        String pnbp = formatRupiah(Double.parseDouble(String.valueOf(pajakModel.getPnbp())));

        holder.txt_nopol.setText(pajakModel.getNopol());
        holder.txt_merk.setText(pajakModel.getMerk());
        holder.txt_tipe.setText(pajakModel.getTipe());
        holder.txt_th_buat.setText(pajakModel.getTh_buat());
        holder.txt_pkbpokok.setText(pkb_pokok);
        holder.txt_pbkdenda.setText(pkb_denda);
        holder.txt_jrpokok.setText(jr_pokok);
        holder.txt_jrdenda.setText(jr_denda);
        holder.txt_pnbp.setText(pnbp);
        holder.txt_masapajak.setText(pajakModel.getMasa_pajak());
        holder.txt_masastnk.setText(pajakModel.getMasa_stnk());
        holder.txt_total.setText(total);
    }

    @Override
    public int getItemCount() {
        return datapajak.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PajakModel> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(datapajak);
            } else {
                for (PajakModel getPajak : datapajak) {
                    if (getPajak.getNopol().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        searchList.add(getPajak);
                    }else{
                        Log.d("RESPON", "eRROR not found");
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = searchList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datapajak.clear();
            datapajak.addAll((Collection<? extends PajakModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchData() {
        if (searchData == null){
            Toast.makeText(context, "Data null", Toast.LENGTH_SHORT).show();
        }
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder{
        private TextView txt_nopol, txt_merk, txt_tipe, txt_th_buat,
                txt_pkbpokok, txt_pbkdenda, txt_jrpokok, txt_jrdenda,
                txt_pnbp, txt_masapajak, txt_masastnk, txt_total;
        public HolderData(@NonNull View itemView) {
            super(itemView);

            txt_nopol = itemView.findViewById(R.id.nopol);
            txt_merk = itemView.findViewById(R.id.merk);
            txt_tipe = itemView.findViewById(R.id.tipe);
            txt_th_buat = itemView.findViewById(R.id.th_buat);
            txt_pkbpokok = itemView.findViewById(R.id.pkb_pokok);
            txt_pbkdenda = itemView.findViewById(R.id.pkb_denda);
            txt_jrpokok = itemView.findViewById(R.id.jr_pokok);
            txt_jrdenda = itemView.findViewById(R.id.jr_denda);
            txt_pnbp = itemView.findViewById(R.id.pnbp);
            txt_masapajak = itemView.findViewById(R.id.masa_pajak);
            txt_masastnk = itemView.findViewById(R.id.masa_stnk);
            txt_total = itemView.findViewById(R.id.total);
        }
    }

    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
