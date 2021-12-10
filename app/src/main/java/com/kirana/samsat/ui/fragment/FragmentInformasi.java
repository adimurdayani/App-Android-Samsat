package com.kirana.samsat.ui.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kirana.samsat.R;
import com.kirana.samsat.network.api.URLServer;
import com.kirana.samsat.network.model.AntrianModel;
import com.kirana.samsat.network.model.Informasi;
import com.kirana.samsat.network.presentase.InformasiAdapter;
import com.kirana.samsat.network.presentase.RiwayatAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentInformasi extends Fragment {
    private View view;
    private SwipeRefreshLayout sw_data;
    private RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Informasi> informasis = new ArrayList<>();
    private InformasiAdapter adapter;
    private StringRequest getData;

    public FragmentInformasi() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        init();
        setDisplay();
        return view;
    }

    private void setGetData(){
        sw_data.setRefreshing(true);
        getData = new StringRequest(Request.Method.GET, URLServer.GETINFORMASI, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject getData = data.getJSONObject(i);
                        Informasi getinformasi = new Informasi();
                        getinformasi.setJadwal_buka(getData.getString("jadwal_buka"));
                        getinformasi.setJadwal_tutup(getData.getString("jadwal_tutup"));
                        getinformasi.setJam_buka(getData.getString("jam_buka"));
                        getinformasi.setJam_tutup(getData.getString("jam_tutup"));
                        informasis.add(getinformasi);
                    }
                    adapter = new InformasiAdapter(getContext(), informasis);
                    rc_data.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    rc_data.setAdapter(adapter);
                } else {
                    koneksiError(object.getString("message"));
                }
            } catch (JSONException e) {
                koneksiError(e.toString());
            }
            sw_data.setRefreshing(false);
        }, error -> {
            sw_data.setRefreshing(false);
        });
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(getContext());
        koneksi.add(getData);
    }

    private void setDisplay() {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);
        sw_data.setOnRefreshListener(this::setGetData);
    }

    private void init() {
        sw_data = view.findViewById(R.id.sw_data);
        rc_data = view.findViewById(R.id.rc_data);
    }

    private void getRetryPolis() {
        getData.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 2000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                    koneksiError("Koneksi gagal");
                }
            }
        });
    }

    private void koneksiError(String pesan) {
        new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .setConfirmText("OK")
                .show();
    }

    @Override
    public void onResume() {
        setGetData();
        super.onResume();
    }
}
