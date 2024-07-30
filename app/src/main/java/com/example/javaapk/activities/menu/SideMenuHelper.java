package com.example.javaapk.activities.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.javaapk.R;
import com.example.javaapk.activities.events.EventsActivity;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class SideMenuHelper {
    NavigationView navigationView;
    Activity parent;
    Profile selectedUser;

    public SideMenuHelper(NavigationView navigationView, @NotNull Profile selectedUser, Activity parent){
        this.navigationView = navigationView;
        this.parent = parent;
        this.selectedUser = selectedUser;
    }
    public void initiateSideMenu(){
        View headerView = navigationView.getHeaderView(0);
        TextView drawerHeader = headerView.findViewById(R.id.header_text);
        ActivityUtilities.updateTextViewWithRemoteString(drawerHeader, () -> {return selectedUser.getName();});
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.side_menu_item_manage_profiles){
                    Intent intent = new Intent(parent, ProfilesActivity.class);
                    parent.startActivity(intent);
                    parent.finish();
                    return true;
                }else if (id == R.id.side_menu_item_events){
                    Intent intent = new Intent(parent, EventsActivity.class);
                    parent.startActivity(intent);
                    parent.finish();
                }

                return false;
            }
        });
    }

}
