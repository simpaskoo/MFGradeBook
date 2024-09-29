package com.example.javaapk.activities.events;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javaapk.R;
import com.example.javaapk.activities.menu.ProfilesActivity;
import com.example.javaapk.activities.menu.SideMenuHelper;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import net.anax.appServerClient.client.data.ID;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.Task;
import net.anax.appServerClient.client.data.TaskAssignment;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EventsActivity extends AppCompatActivity {

    private NavigationView sideMenu;
    private boolean isMenuOpen = false;  // To track whether the menu is open or not
    private float menuWidth;  // To store the width of the menu
    private static final String TAG = "MainActivity";

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Objects.equals(intent.getAction(), "EVENT_CHANGED")){
                refresh();
            }
        }
    };

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaudalosti_design);


        //Side menu
        sideMenu = findViewById(R.id.sideMenu);
        sideMenu.post(() -> {
            menuWidth = sideMenu.getWidth();
            sideMenu.setTranslationX(-menuWidth);  // Initially hide the side menu by moving it off screen
        });

        // Button to toggle the side menu
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });
        //Side menu

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!DataManager.getInstance().isProfileSelected()){
            ActivityUtilities.askToSelectProfile(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, new IntentFilter("EVENT_CHANGED"), Context.RECEIVER_NOT_EXPORTED);
        }else{
            registerReceiver(receiver, new IntentFilter("EVENT_CHANGED"));
        }

        profile = DataManager.getInstance().getSelectedProfile();

        // no navigation view inside aaudalosti_design.xml, cannot create side menu.
        SideMenuHelper sideMenuHelper = new SideMenuHelper(sideMenu, profile, this);
        sideMenuHelper.initiateSideMenu();

        FloatingActionButton createNewEventButton = findViewById(R.id.button_create_new_event);

        createNewEventButton.setOnClickListener(V -> {
            Intent intent = new Intent(this, CreateNewEventActivity.class);
            startActivity(intent);
        });

        if(!DataManager.getInstance().isProfileSelected()){
            Intent intent = new Intent(this, ProfilesActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
        Profile profile = DataManager.getInstance().getSelectedProfile();





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton timeTableIcon = findViewById(R.id.time_table_icon);
        ImageButton eventsIcon = findViewById(R.id.events_icon);
        ImageButton assessmentsIcon = findViewById(R.id.assessments_icon);

        // Set default fragment
        //loadFragment(new TimeTableFragment());

        // Set up listeners for your toolbar buttons
        timeTableIcon.setOnClickListener(v -> loadFragment(new TimeTableFragment(EventsActivity.this)));
        eventsIcon.setOnClickListener(v -> returnToMainContent());
        assessmentsIcon.setOnClickListener(v -> loadFragment(new AssessmentsFragment()));

        //slideToActivity();
        refresh();
    }

    // Method to load fragments into the container
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainActivity, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Return to MainActivity content (no fragment)
    private void returnToMainContent() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @SuppressLint("ResourceType")
    private void slideToActivity() {

        //View contentView = findViewById(android.R.id.content);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.timetable_fragment);
        if (fragment instanceof AssessmentsFragment){

            ImageButton timeTableIcon = findViewById(R.id.time_table_icon);
            timeTableIcon.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {


                    Fragment timeTableFragment = new TimeTableFragment(EventsActivity.this);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.setCustomAnimations(
                            R.anim.slide_in_left,  // enter animation
                            R.anim.slide_out_right, // exit animation
                            R.anim.slide_in_left,  // pop enter animation (when back is pressed)
                            R.anim.slide_out_right // pop exit animation (when back is pressed)
                    );

                    fragmentTransaction.replace(R.id.mainActivity, timeTableFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            ImageButton assessmentsIcon = findViewById(R.id.assessments_icon);
            assessmentsIcon.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {


                    Fragment assessmentsFragment = new AssessmentsFragment();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.setCustomAnimations(
                            R.anim.slide_in_left,  // enter animation
                            R.anim.slide_out_right, // exit animation
                            R.anim.slide_in_left,  // pop enter animation (when back is pressed)
                            R.anim.slide_out_right // pop exit animation (when back is pressed)
                    );

                    fragmentTransaction.replace(R.id.mainActivity, assessmentsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

        }

















        /*timeTableIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentTimeTable = new TimeTableFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity, fragmentTimeTable).commit();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });*/

        /*timeTableIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Navigation", "Timetable Icon Clicked");
                Intent intent = new Intent(EventsActivity.this, TimetableActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });*/

        /*ImageButton assessmentsIcon = findViewById(R.id.assessments_icon);
        assessmentsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Navigation", "Timetable Icon Clicked");
                Intent intent = new Intent(EventsActivity.this, AssessmentsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });*/
    }

    //Side menu
    private void toggleMenu() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
        isMenuOpen = !isMenuOpen;
    }

    private void openMenu() {
        sideMenu.animate().translationX(0).setDuration(300);  // Slide the menu in
    }

    private void closeMenu() {
        sideMenu.animate().translationX(-menuWidth).setDuration(300);  // Slide the menu out
    }
    //Side menu





    public void refresh(){
        ActivityUtilities.runNetworkOperation(() -> {
            TaskAssignment[] assignments = profile.getTaskAssignments();
            ArrayList<Integer> taskIds = new ArrayList<>();
            for(TaskAssignment assignment : assignments){
                if(assignment.userId == profile.getId()){
                    taskIds.add(assignment.taskId);
                }
            }

            ArrayList<Task> tasks = new ArrayList<>();

            for(Integer id : taskIds){
                Task task = null;
                try {
                    task = profile.mfGradeBookHandler.memoryManager.getTask(id);
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                if(task != null){
                    tasks.add(task);
                }
            }

            System.out.println("taskids: " + taskIds);

            ActivityUtilities.runOnMainThread(() -> {
                LinearLayout eventsLayout = findViewById(R.id.events_linear_layout);

                eventsLayout.removeAllViews();

                int marginTopPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
                int marginBottomPx = marginTopPx;

                NumberFormat format = NumberFormat.getNumberInstance();
                format.setMaximumFractionDigits(2);

                for(Task task : tasks){

                    View view = getLayoutInflater().inflate(R.layout.aadynamic_idk, eventsLayout, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0 , marginTopPx, 0, marginBottomPx);
                    view.setLayoutParams(params);

                    TextView description = view.findViewById(R.id.text_view_event_description);
                    TextView date = view.findViewById(R.id.text_view_event_date);
                    TextView groupName = view.findViewById(R.id.text_view_group);
                    TextView amount = view.findViewById(R.id.text_view_event_amount);

                    description.setText(task.cachedDescription);
                    date.setText(DateFormat.getDateTimeInstance().format(new Date(task.cachedDueTimestamp * 1000)));
                    groupName.setText("group name");

                    boolean isPayment = task.cachedType == Task.TaskType.PAYMENT.type;

                    //Price
                    Intent intent = getIntent();

                    // Retrieve the number passed from MainActivity (default to 0 if no number found)
                    int receivedNumber = intent.getIntExtra("number_key", 0);

                    // Set the number to the TextView
                    //textView.setText(String.valueOf(receivedNumber));

                    //Price




                    if(isPayment && task.cachedAmount >= 0) {
                        amount.setText(format.format(task.cachedAmount / 100f) + String.valueOf(receivedNumber) + " ,-");
                        //amount.setText(receivedText + " ,-");
                    } else if (isPayment && task.cachedAmount == ID.NONE.id){
                        amount.setText("not set");
                    } else if (isPayment && task.cachedAmount == ID.UNKNOWN.id){
                        amount.setText("not known");
                    } else{
                        amount.setVisibility(View.INVISIBLE);
                    }

                    eventsLayout.addView(view);

                }
            });

        });
    }
}