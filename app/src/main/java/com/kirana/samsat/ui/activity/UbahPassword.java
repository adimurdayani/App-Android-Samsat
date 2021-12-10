package com.kirana.samsat.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kirana.samsat.R;
import com.kirana.samsat.network.api.URLServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UbahPassword extends AppCompatActivity {
    private EditText e_password, e_konfrimasi_pass;
    private String password, konfirmasi_password, id_konsumen;
    private ImageView btn_kembali;
    private CardView btn_simpan;
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^" +
            "(?=.*[1-9])" + //harus menggunakan satu angka
            "(?=.*[a-z])" + //harus menggunakan abjad
            "(?=.*[A-Z])" + //harus menggunakan huruf kapital
            "(?=.*[@#$%^&+=])" + //harus menggunakan sepesial karakter
            "(?=\\S+$)" + // tidak menggunakan spasi
            ".{6,}" + //harus lebih dari 6 karakter
            "$"
    );
    private ProgressDialog dialog;
    private StringRequest updatePassword;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);
        init();
    }

    private void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_kembali = findViewById(R.id.btn_kembali);
        e_password = findViewById(R.id.e_password);
        e_konfrimasi_pass = findViewById(R.id.e_konfir_password);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        id_konsumen = String.valueOf(preferences.getInt("id_kostumer", 0));

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });

        btn_simpan.setOnClickListener(v -> {
            if (validasi()) {
                updatepassword();
            }
        });
    }

    private void updatepassword() {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
        updatePassword = new StringRequest(Request.Method.POST, URLServer.UBAHPASS, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(preferences.getString("token", ""));
                    editor.remove(String.valueOf(preferences.getInt("id_kostumer", 0)));
                    editor.remove(preferences.getString("nama", ""));
                    editor.clear();
                    editor.apply();

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
            koneksiError(error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_kostumer", id_konsumen);
                map.put("password", password);
                return map;
            }
        };
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(updatePassword);
    }

    private void getRetryPolis() {
        updatePassword.setRetryPolicy(new RetryPolicy() {
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
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Ubah Password Sukses!")
                .setContentText("Anda telah berhasil melakukan booking nomor antrian!")
                .setConfirmText("Ok")
                .setConfirmClickListener(sweetAlertDialog -> {
                    Toast.makeText(this, "Update password success!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();
    }

    private void koneksiError(String pesan) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .setConfirmText("OK")
                .show();
    }

    public void gettextinput() {
        password = e_password.getText().toString().trim();
        konfirmasi_password = e_konfrimasi_pass.getText().toString().trim();
    }

    private boolean validasi() {
        gettextinput();
        if (password.isEmpty()) {
            e_password.setError("Kolom password tidak boleh kosong!");
            return false;
        } else if (password.length() < 6) {
            e_password.setError("Password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(password).matches()) {
            e_password.setError("Password sangat lemah!. Contoh: @Jad123");
            return false;
        }
        if (konfirmasi_password.isEmpty()) {
            e_konfrimasi_pass.setError("Kolom konfirmasi password tidak boleh kosong!");
            return false;
        } else if (konfirmasi_password.length() < 6) {
            e_konfrimasi_pass.setError("Konfirmasi password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(konfirmasi_password).matches()) {
            e_konfrimasi_pass.setError("Konfirmasi password sangat lemah!");
            return false;
        } else if (!konfirmasi_password.matches(password)) {
            e_konfrimasi_pass.setError("Konfirmasi password tidak sama dengan password!");
            return false;
        }
        return true;
    }
}