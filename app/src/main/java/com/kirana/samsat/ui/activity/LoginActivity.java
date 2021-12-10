package com.kirana.samsat.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.kirana.samsat.R;
import com.kirana.samsat.ui.fragment.FragmentAwal;

public class LoginActivity extends AppCompatActivity {
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frm_login, new FragmentAwal()).commit();
    }
}