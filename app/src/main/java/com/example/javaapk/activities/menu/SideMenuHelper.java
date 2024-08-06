package com.example.javaapk.activities.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.javaapk.R;
import com.example.javaapk.activities.assessments.AssessmentsActivity;
import com.example.javaapk.activities.events.EventsActivity;
import com.example.javaapk.activities.timetable.TimetableActivity;
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
        initiateSideMenu(true);
    }

    public void initiateSideMenu(boolean finish){
        View headerView = navigationView.getHeaderView(0);
        TextView drawerHeader = headerView.findViewById(R.id.header_text);
        ActivityUtilities.updateTextViewWithRemoteString(drawerHeader, () -> selectedUser.getName());
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.side_menu_item_manage_profiles){
                Intent intent = new Intent(parent, ProfilesActivity.class);
                parent.startActivity(intent);
            }else if (id == R.id.side_menu_item_events){
                Intent intent = new Intent(parent, EventsActivity.class);
                parent.startActivity(intent);
            }else if (id == R.id.side_menu_item_manage_groups){
                Intent intent = new Intent(parent, ManageGroupsActivity.class);
                parent.startActivity(intent);
            }else if (id == R.id.side_menu_item_settings){
                Intent intent = new Intent(parent, SettingsActivity.class);
                parent.startActivity(intent);
            } else if(id == R.id.side_menu_item_assessments){
                Intent intent = new Intent(parent, AssessmentsActivity.class);
                parent.startActivity(intent);
            } else if(id == R.id.side_menu_item_timetable){
                Intent intent = new Intent(parent, TimetableActivity.class);
                parent.startActivity(intent);
            }
            else{
                return false;
            }

            if(finish){parent.finish();}
            return true;
        });
    }

}
