package com.kirana.samsat.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kirana.samsat.R;
import com.kirana.samsat.ui.fragment.FragmentAntrian;
import com.kirana.samsat.ui.fragment.FragmentHome;
import com.kirana.samsat.ui.fragment.FragmentInformasi;
import com.kirana.samsat.ui.fragment.FragmentPajak;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager manager;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frm_home, new FragmentHome()).commit();

        bottomNavigationView = findViewById(R.id.navigasiButton);
        BottomNavigationView.OnNavigationItemSelectedListener navigasi = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new FragmentHome();
                        break;
                    case R.id.antrian:
                        fragment = new FragmentAntrian();
                        break;
                    case R.id.informasi:
                        fragment = new FragmentInformasi();
                        break;
                    case R.id.pajak:
                        fragment = new FragmentPajak();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frm_home, fragment).commit();
                return true;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navigasi);
    }
}