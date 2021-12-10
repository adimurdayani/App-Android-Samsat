package com.kirana.samsat.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kirana.samsat.R;

public class FragmentAwal extends Fragment {
    private View view;
    private LinearLayout btn_lanjut;
    private FragmentManager manager;

    public FragmentAwal() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_awal, container, false);
        init();
        return view;
    }

    private void init() {
        btn_lanjut = view.findViewById(R.id.btn_lanjut);

        btn_lanjut.setOnClickListener(v -> {
            manager = getFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .replace(R.id.frm_login, new FramgentLogin())
                    .commit();
        });
    }
}
