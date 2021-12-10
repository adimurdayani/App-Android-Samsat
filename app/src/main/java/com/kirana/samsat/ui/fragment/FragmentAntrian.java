package com.kirana.samsat.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentAntrian extends Fragment {
    private View view;
    private ImageView btn_setting;
    private CardView btn_booking;
    private EditText e_nama, e_noantrian;
    public String no_antrian, id_kostumer, nama;
    private SharedPreferences preferences;
    private FragmentManager manager;
    private StringRequest kirimNoantrian, getNoantrian;

    public FragmentAntrian() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_antrian, container, false);
        init();
        return view;
    }

    private void init() {
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_booking = view.findViewById(R.id.btn_kirim);
        btn_setting = view.findViewById(R.id.btn_setting);
        e_nama = view.findViewById(R.id.e_nama);
        e_noantrian = view.findViewById(R.id.e_noantrian);

        e_nama.setText(preferences.getString("nama", ""));
        id_kostumer = String.valueOf(preferences.getInt("id_kostumer", 0));
        Log.d("Response", "ID: " + id_kostumer);

        btn_setting.setOnClickListener(v -> {
            manager = getFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .replace(R.id.frm_home, new FragmentSetting())
                    .commit();
        });

        btn_booking.setOnClickListener(v -> {
            setKirimNoantrian();
        });
    }

    private void setGetNoantrian() {
        getNoantrian = new StringRequest(Request.Method.GET, URLServer.GETNOANTRIAN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    e_noantrian.setText(object.getString("data"));
                } else {
                    koneksiError(object.getString("message"));
                }
            } catch (JSONException e) {
                koneksiError(e.toString());
            }
        }, error -> {
            koneksiError(error.toString());
        });
        getNoantrian.setRetryPolicy(new RetryPolicy() {
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
        RequestQueue koneksi = Volley.newRequestQueue(getContext());
        koneksi.add(getNoantrian);
    }

    public void getTextinput() {
        nama = e_nama.getText().toString().trim();
        no_antrian = e_noantrian.getText().toString().trim();
    }

    private void setKirimNoantrian() {
        SweetAlertDialog dialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
        kirimNoantrian = new StringRequest(Request.Method.POST, URLServer.POSTANTRIAN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    showDialog();
                } else {
                    koneksiError(object.getString("message"));
                }
            } catch (JSONException e) {
                koneksiError(e.toString());
            }
            dialog.dismiss();
        }, error -> {
            dialog.dismiss();
            error.printStackTrace();
            koneksiError(error.getMessage());
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("kostumer_id", id_kostumer);
                map.put("no_antrian", e_noantrian.getText().toString().trim());
                return map;
            }
        };
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(requireContext());
        koneksi.add(kirimNoantrian);
    }

    private void getRetryPolis() {
        kirimNoantrian.setRetryPolicy(new RetryPolicy() {
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

    private void showDialog() {
        new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Booking Sukses!")
                .setContentText("Anda telah berhasil melakukan booking nomor antrian!")
                .setConfirmText("OK")
                .show();
    }

    private void koneksiError(String pesan) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .setConfirmText("OK")
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setGetNoantrian();
        getTextinput();
    }
}
