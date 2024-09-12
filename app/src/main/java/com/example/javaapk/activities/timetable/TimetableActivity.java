package com.example.javaapk.activities.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.activities.assessments.AssessmentsActivity;
import com.example.javaapk.activities.events.EventsActivity;
import com.example.javaapk.activities.menu.SideMenuHelper;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;
import com.google.android.material.navigation.NavigationView;

import net.anax.skolaOnlineScraper.data.timetable.DateOfDay;
import net.anax.skolaOnlineScraper.data.timetable.TimetableWeek;

import java.util.Calendar;

public class TimetableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        slideToActivity();
        if(!DataManager.getInstance().isProfileSelected()){
            //finish();
            return;
        }
        Profile profile = DataManager.getInstance().getSelectedProfile();

        NavigationView navigationView = findViewById(R.id.sideMenu);
        SideMenuHelper sideMenuHelper = new SideMenuHelper(navigationView, profile, this);
        sideMenuHelper.initiateSideMenu();

        if(!profile.hasSkolaOnline()){
            //finish();
            return;
        }

        TextView jsonTestView = findViewById(R.id.text_view_timetable_json_test);

        ActivityUtilities.runNetworkOperation(() -> {
            TimetableWeek timetableWeek = profile.skolaOnlineHandler.getTimetableWeekForDate(DateOfDay.fromCalendar(Calendar.getInstance()));
            if(timetableWeek != null){
                ActivityUtilities.runOnMainThread(() -> {
                    jsonTestView.setText(timetableWeek.getJsonObject().toJSONString());
                });
            }
        });

    }

    private void slideToActivity() {
        ImageButton eventsIcon = findViewById(R.id.events_icon1);
        eventsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Navigation", "Timetable Icon Clicked");
                Intent intent = new Intent(TimetableActivity.this, EventsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        ImageButton assessmentsIcon = findViewById(R.id.assessments_icon1);
        assessmentsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Navigation", "Timetable Icon Clicked");
                Intent intent = new Intent(TimetableActivity.this, AssessmentsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
}