package com.kirana.samsat.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.kirana.samsat.network.presentase.RiwayatAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentHome extends Fragment {
    private View view;
    private ImageView btn_setting;
    private RecyclerView.LayoutManager layoutManager;
    private StringRequest getPengaduan;
    public static RecyclerView rc_data;
    public static ArrayList<AntrianModel> dataAntrian;
    private SwipeRefreshLayout sw_data;
    private FragmentManager manager;
    private SharedPreferences preferences;
    private SearchView searchView;
    private RiwayatAdapter adapter;

    public FragmentHome() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }

    private void init() {
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_setting = view.findViewById(R.id.btn_setting);
        rc_data = view.findViewById(R.id.rc_data);
        sw_data = view.findViewById(R.id.sw_data);
        searchView = view.findViewById(R.id.search);

        btn_setting.setOnClickListener(v -> {
            manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frm_home, new FragmentSetting())
                    .commit();
        });

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

        sw_data.setOnRefreshListener(this::setGetPengaduan);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getSearchData().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setGetPengaduan();
                return false;
            }
        });
    }

    public void setGetPengaduan() {
        dataAntrian = new ArrayList<>();
        sw_data.setRefreshing(true);

        getPengaduan = new StringRequest(Request.Method.GET, URLServer.GETANTRIAN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject getData = data.getJSONObject(i);
                        AntrianModel getAntrian = new AntrianModel();
                        getAntrian.setId_antrian(getData.getInt("id_antrian"));
                        getAntrian.setNama(getData.getString("nama"));
                        getAntrian.setCreated_at(getData.getString("created_at"));
                        getAntrian.setNo_antrian(getData.getString("no_antrian"));
                        getAntrian.setPanggil_antrian(getData.getString("panggil_antrian"));
                        dataAntrian.add(getAntrian);

                    }
                    adapter = new RiwayatAdapter(getContext(), dataAntrian);
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String generate_token = preferences.getString("token_id", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("token_generate", generate_token);
                return map;
            }
        };
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(getContext());
        koneksi.add(getPengaduan);
    }

    private void getRetryPolis() {
        getPengaduan.setRetryPolicy(new RetryPolicy() {
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
        super.onResume();
        setGetPengaduan();
    }
}
