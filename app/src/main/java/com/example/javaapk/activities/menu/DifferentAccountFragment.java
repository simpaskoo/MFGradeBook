package com.example.javaapk.activities.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.javaapk.R;

public class DifferentAccountFragment extends Fragment {

    public DifferentAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_different_account, container, false);
        ImageButton backBtn = view.findViewById(R.id.back_to_login_btn);
        backBtn.setOnClickListener(v -> {
            // Go back to the Main Activity by finishing the current activity
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
        return view;
    }
}