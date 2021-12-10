package com.kirana.samsat.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.kirana.samsat.ui.activity.HomeActivity;
import com.kirana.samsat.ui.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FramgentLogin extends Fragment {
    private View view;
    private CardView btn_login;
    private TextView btn_register;
    private FragmentManager manager;
    private EditText e_username, e_password;
    private String username, password;
    private StringRequest loginUser;
    private SharedPreferences session_data;
    private SharedPreferences.Editor editor;

    public FramgentLogin() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return view;
    }

    private void init() {
        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);
        e_password = view.findViewById(R.id.e_password);
        e_username = view.findViewById(R.id.e_username);

        btn_login.setOnClickListener(v -> {
            if (validasi()) {
                login();
            }
        });

        btn_register.setOnClickListener(v -> {
            manager = getFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .replace(R.id.frm_login, new FragmentRegister())
                    .commit();
        });
    }

    private void login() {
        SweetAlertDialog dialog = new SweetAlertDialog(requireContext(),SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
        loginUser = new StringRequest(Request.Method.POST, URLServer.LOGIN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    session_data = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    editor = session_data.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putInt("id_kostumer", data.getInt("id_kostumer"));
                    editor.putString("nama", data.getString("nama"));
                    editor.putString("username", data.getString("username"));
                    editor.putString("email", data.getString("email"));
                    editor.putString("no_hp", data.getString("no_hp"));
                    editor.putString("user_id", data.getString("user_id"));
                    editor.putString("token_id", data.getString("token_id"));
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    startActivity(new Intent(getContext(), HomeActivity.class));
                    ((LoginActivity) requireContext()).finish();
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
                String token_generate = FirebaseInstanceId.getInstance().getToken();
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                map.put("token_id", token_generate);
                return map;
            }
        };
        getRetryPolis();
        RequestQueue koneksi = Volley.newRequestQueue(requireContext());
        koneksi.add(loginUser);
    }

    private void getRetryPolis() {
        loginUser.setRetryPolicy(new RetryPolicy() {
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

    public void getinputtext() {
        username = e_username.getText().toString().trim();
        password = e_password.getText().toString().trim();
    }

    private boolean validasi() {
        getinputtext();
        if (username.isEmpty()) {
            e_username.setError("Username tidak boleh kosong!");
            return false;
        }
        if (password.isEmpty()) {
            e_password.setError("Password tidak boleh kosong!");
            return false;
        } else if (password.length() < 6) {
            e_password.setError("Password tidak boleh kurang dari 6 karakter!");
            return false;
        }
        return true;
    }
}
