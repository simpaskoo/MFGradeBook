package com.example.javaapk.activities.events;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;

public class TimeTableFragment extends Fragment {
    Activity parent;

    public TimeTableFragment(Activity parent) {
        this.parent = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        if(DataManager.getInstance().isProfileSelected()){
            Profile profile = DataManager.getInstance().getSelectedProfile();
            //NavigationView sideMenu = view.findViewById(R.id.sideMenu);
            //SideMenuHelper helper = new SideMenuHelper(sideMenu, profile, parent);
            //helper.initiateSideMenu(false);
        }

        return view;
    }
}