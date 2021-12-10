package com.kirana.samsat.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;

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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditActivity extends AppCompatActivity {
    private ImageView btn_kembali;
    private EditText e_username, e_phone, e_email, e_nama, e_alamat;
    private String nama, email, phone, username, id_kostumer, alamat;
    private SharedPreferences preferences;
    private CardView btn_simpan;
    private ProgressDialog dialog;
    private StringRequest updateUser;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
    }

    private void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        e_username = findViewById(R.id.e_username);
        e_email = findViewById(R.id.e_email);
        e_nama = findViewById(R.id.e_nama);
        e_phone = findViewById(R.id.e_nohp);
        btn_simpan = findViewById(R.id.btn_simpan);
        e_alamat = findViewById(R.id.e_alamat);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        id_kostumer = String.valueOf(preferences.getInt("id_kostumer", 0));

        btn_simpan.setOnClickListener(v -> {
            if (validasi()) {
                updateProfil();
            }
        });

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }

    private void updateProfil() {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
        updateUser = new StringRequest(Request.Method.POST, URLServer.UBAHPROFILE, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    editor = preferences.edit();
                    editor.putString("nama", data.getString("nama"));
                    editor.putString("email", data.getString("email"));
                    editor.putString("email", data.getString("email"));
                    editor.putString("username", data.getString("username"));
                    editor.putString("no_hp", data.getString("no_hp"));
                    editor.putString("alamat", data.getString("alamat"));
                    editor.apply();
                    showSukses();
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
                map.put("id_kostumer", id_kostumer);
                map.put("nama", nama);
                map.put("email", email);
                map.put("username", username);
                map.put("no_hp", phone);
                map.put("alamat", alamat);
                return map;
            }
        };
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(updateUser);
    }

    private void showSukses(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Booking Sukses!")
                .setContentText("Anda telah berhasil melakukan booking nomor antrian, apakah anda ingin kembali!")
                .setConfirmText("Iya, kembali!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        onBackPressed();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("Tutup")
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    private void getRetryPolis() {
        updateUser.setRetryPolicy(new RetryPolicy() {
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
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .setConfirmText("OK")
                .show();
    }

    public void gettexinput() {
        nama = e_nama.getText().toString().trim();
        username = e_username.getText().toString().trim();
        email = e_email.getText().toString().trim();
        phone = e_phone.getText().toString().trim();
        alamat = e_alamat.getText().toString().trim();
    }

    private boolean validasi() {
        gettexinput();
        if (nama.isEmpty()) {
            e_nama.setError("Username tidak boleh kosong!");
            return false;
        }
        if (email.isEmpty()) {
            e_email.setError("Kolom email tidak boleh kosong!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e_email.setError("Format email salah!. Contoh: gunakan @example.com");
            return false;
        }
        if (phone.isEmpty()) {
            e_phone.setError("Kolom phone tidak boleh kosong!");
            return false;
        }
        if (username.isEmpty()) {
            e_username.setError("Kolom username tidak boleh kosong!");
            return false;
        }
        return true;
    }
}