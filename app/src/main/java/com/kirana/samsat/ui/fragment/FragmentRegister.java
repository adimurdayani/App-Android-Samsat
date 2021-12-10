package com.kirana.samsat.ui.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.kirana.samsat.R;
import com.kirana.samsat.network.api.URLServer;
import com.kirana.samsat.network.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentRegister extends Fragment {
    private View view;
    private ImageView btn_kembali;
    private CardView btn_register;
    private EditText e_nama, e_username, e_password, e_konfir_pass, e_email;
    private TextView txt_gettoken;
    private String nama, email, username, password, konfirmasi_password, getToken;
    private FragmentManager manager;
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^" +
            "(?=.*[1-9])" + //harus menggunakan satu angka
            "(?=.*[a-z])" + //harus menggunakan abjad
            "(?=.*[A-Z])" + //harus menggunakan huruf kapital
            "(?=.*[@#$%^&+=])" + //harus menggunakan sepesial karakter
            "(?=\\S+$)" + // tidak menggunakan spasi
            ".{6,}" + //harus lebih dari 6 karakter
            "$"
    );
    private StringRequest registerUser;

    public FragmentRegister() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        init();
        return view;
    }

    private void init() {
        btn_kembali = view.findViewById(R.id.btn_kembali);
        btn_register = view.findViewById(R.id.btn_register);
        e_email = view.findViewById(R.id.e_email);
        e_username = view.findViewById(R.id.e_username);
        e_password = view.findViewById(R.id.e_password);
        e_konfir_pass = view.findViewById(R.id.e_konfir_password);
        e_nama = view.findViewById(R.id.e_nama);
        txt_gettoken = view.findViewById(R.id.getToken);

        txt_gettoken.setText(FirebaseInstanceId.getInstance().getToken());

        btn_register.setOnClickListener(v -> {
            if (validasi()) {
                register();
            }
        });
        btn_kembali.setOnClickListener(v -> {
            manager = getFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .replace(R.id.frm_login, new FramgentLogin())
                    .commit();
        });
    }

    private void register() {
        SweetAlertDialog dialog = new SweetAlertDialog(requireContext(),SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
        registerUser = new StringRequest(Request.Method.POST, URLServer.REGISTER, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    UserModel postUser = new UserModel();
                    postUser.setNama(data.getString("nama"));
                    postUser.setUsername(data.getString("username"));
                    postUser.setEmail(data.getString("email"));
                    postUser.setPassword(data.getString("password"));
                    postUser.setToken_id(data.getString("token_id"));
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("nama", nama);
                map.put("email", email);
                map.put("username", username);
                map.put("password", password);
                map.put("token_id", getToken);
                return map;
            }
        };
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(requireContext());
        koneksi.add(registerUser);
    }

    private void getRetryPolis() {
        registerUser.setRetryPolicy(new RetryPolicy() {
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

    private void showDialog(){
        new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Register Sukses!")
                .setContentText("Klik ok untuk login!")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        FragmentManager manager = getFragmentManager();
                        assert manager != null;
                        manager.beginTransaction().replace(R.id.frm_login, new FramgentLogin())
                                .commit();
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void koneksiError(String pesan){
        new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .setConfirmText("OK")
                .show();
    }

    public void getinputtext() {
        nama = e_nama.getText().toString().trim();
        email = e_email.getText().toString().trim();
        username = e_username.getText().toString().trim();
        password = e_password.getText().toString().trim();
        konfirmasi_password = e_konfir_pass.getText().toString().trim();
        getToken = txt_gettoken.getText().toString().trim();
    }

    private boolean validasi() {
        getinputtext();
        if (nama.isEmpty()) {
            e_nama.setError("Kolom nama tidak boleh kosong!");
            return false;
        }
        if (email.isEmpty()) {
            e_email.setError("Kolom email tidak boleh kosong!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e_email.setError("Format email salah!. Contoh: gunakan @example.com");
            return false;
        }
        if (username.isEmpty()) {
            e_username.setError("Kolom username tidak boleh kosong!");
            return false;
        }
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
            e_konfir_pass.setError("Kolom konfirmasi password tidak boleh kosong!");
            return false;
        } else if (konfirmasi_password.length() < 6) {
            e_konfir_pass.setError("Konfirmasi password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(konfirmasi_password).matches()) {
            e_konfir_pass.setError("Konfirmasi password sangat lemah!");
            return false;
        } else if (!konfirmasi_password.matches(password)) {
            e_konfir_pass.setError("Konfirmasi password tidak sama dengan password!");
            return false;
        }
        return true;
    }
}
