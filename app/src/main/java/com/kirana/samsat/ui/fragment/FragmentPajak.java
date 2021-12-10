package com.kirana.samsat.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kirana.samsat.R;
import com.kirana.samsat.network.api.URLServer;
import com.kirana.samsat.network.model.PajakModel;
import com.kirana.samsat.network.presentase.PajakAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentPajak extends Fragment {
    private View view;
    private RecyclerView rc_data;
    private SwipeRefreshLayout sw_data;
    private RecyclerView.LayoutManager layoutManager;
    private StringRequest getPajak;
    public static ArrayList<PajakModel> datapajak;
    private SharedPreferences preferences;
    private SearchView searchView;
    private PajakAdapter adapter;
    private ImageView btn_setting;
    private FragmentManager manager;

    public FragmentPajak() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pajak, container, false);
        init();
        return view;
    }

    private void init() {
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        rc_data = view.findViewById(R.id.rc_data);
        sw_data = view.findViewById(R.id.sw_data);
        searchView = view.findViewById(R.id.search);
        btn_setting = view.findViewById(R.id.btn_setting);

        btn_setting.setOnClickListener(v -> {
            manager = getFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .replace(R.id.frm_home, new FragmentSetting())
                    .commit();
        });

        rc_data.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

//        sw_data.setOnRefreshListener(this::setGetPajak);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rc_data.setVisibility(View.VISIBLE);
                adapter.getSearchData().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void setGetPajak() {
        datapajak = new ArrayList<>();
        sw_data.setRefreshing(true);

        getPajak = new StringRequest(Request.Method.GET, URLServer.GETPAJAK, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject getData = data.getJSONObject(i);
                        PajakModel getdatapajak = new PajakModel();
                        getdatapajak.setNopol(getData.getString("nopol"));
                        getdatapajak.setJenis(getData.getString("jenis"));
                        getdatapajak.setTipe(getData.getString("tipe"));
                        getdatapajak.setMerk(getData.getString("merk"));
                        getdatapajak.setTh_buat(getData.getString("th_buat"));
                        getdatapajak.setPkb_pokok(getData.getInt("pkb_pokok"));
                        getdatapajak.setPkb_denda(getData.getInt("pkb_denda"));
                        getdatapajak.setJr_pokok(getData.getInt("jr_pokok"));
                        getdatapajak.setJr_denda(getData.getInt("jr_denda"));
                        getdatapajak.setPnbp(getData.getInt("pnbp"));
                        getdatapajak.setTotal(getData.getInt("total"));
                        getdatapajak.setMasa_pajak(getData.getString("masa_pajak"));
                        getdatapajak.setMasa_stnk(getData.getString("masa_stnk"));
                        datapajak.add(getdatapajak);

                    }
                    adapter = new PajakAdapter(getContext(), datapajak);
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
            koneksiError(error.toString());
        });
        getPajak.setRetryPolicy(new RetryPolicy() {
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
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(requireActivity());
        koneksi.add(getPajak);
    }

    private void getRetryPolis() {
        getPajak.setRetryPolicy(new RetryPolicy() {
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
        setGetPajak();
        super.onResume();
    }
}
