package com.kirana.samsat.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kirana.samsat.R;
import com.kirana.samsat.network.api.URLServer;
import com.kirana.samsat.ui.activity.BantuanActivity;
import com.kirana.samsat.ui.activity.EditActivity;
import com.kirana.samsat.ui.activity.LoginActivity;
import com.kirana.samsat.ui.activity.TentangActivity;
import com.kirana.samsat.ui.activity.UbahPassword;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentSetting extends Fragment {
    private View view;
    private LinearLayout btn_edit;
    private TextView txt_nama, txt_email;
    private SharedPreferences preferences;
    private RelativeLayout btn_tentang, btn_ubahpassword, btn_bantuan, btn_logout;
    private StringRequest prosesLogout;

    public FragmentSetting() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        return view;
    }

    private void init() {
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        txt_email = view.findViewById(R.id.email);
        txt_nama = view.findViewById(R.id.nama);
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_bantuan = view.findViewById(R.id.btn_bantuan);
        btn_tentang = view.findViewById(R.id.btn_tentang);
        btn_ubahpassword = view.findViewById(R.id.btn_ubahpass);
        btn_logout = view.findViewById(R.id.btn_logout);

        btn_edit.setOnClickListener(v -> {
            Intent editprofile = new Intent(getContext(), EditActivity.class);
            startActivity(editprofile);
        });

        btn_ubahpassword.setOnClickListener(v -> {
            Intent ubahpassword = new Intent(getContext(), UbahPassword.class);
            startActivity(ubahpassword);
        });
        btn_tentang.setOnClickListener(v -> {
            Intent tentang = new Intent(getContext(), TentangActivity.class);
            startActivity(tentang);
        });
        btn_bantuan.setOnClickListener(v -> {
            Intent bantuan = new Intent(getContext(), BantuanActivity.class);
            startActivity(bantuan);
        });
        btn_logout.setOnClickListener(v -> {
            showDialog();
        });
    }

    private void logout() {
        SweetAlertDialog dialog = new SweetAlertDialog(requireContext(),SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
        prosesLogout = new StringRequest(Request.Method.GET, URLServer.LOGOUT, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(preferences.getString("token", ""));
                    editor.remove(String.valueOf(preferences.getInt("id_regis", 0)));
                    editor.remove(preferences.getString("nama", ""));
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                }
            } catch (JSONException e) {
                koneksiError(e.toString());
            }
            dialog.dismiss();
        }, error -> {
            dialog.dismiss();
            koneksiError(error.toString());
        });
        getRetryPolis();
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        queue.add(prosesLogout);
    }

    private void getRetryPolis() {
        prosesLogout.setRetryPolicy(new RetryPolicy() {
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

    private void koneksiError(String pesan){
        new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .setConfirmText("OK")
                .show();
    }

    private void showDialog(){
        new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("yakin ingin keluar dari aplikasi!")
                .setConfirmText("Iya!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        logout();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("Tutup")
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        txt_nama.setText(preferences.getString("nama", ""));
        txt_email.setText(preferences.getString("email", ""));
    }
}
