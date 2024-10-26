package com.example.javaapk.activities.events;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javaapk.R;
import com.example.javaapk.activities.aboutApp.AboutAppActivity;
import com.example.javaapk.activities.menu.ManageGroupsActivity;
import com.example.javaapk.activities.menu.ProfilesActivity;
import com.example.javaapk.activities.menu.SettingsActivity;
import com.example.javaapk.activities.menu.TreasureActivity;
import com.example.javaapk.activities.menu.UdalostInfo;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private ConstraintLayout sideMenu;
    private boolean isMenuOpen = false;

    //private NavigationView sideMenu;
    //private boolean isMenuOpen = false;  // To track whether the menu is open or not
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

        //Intent intenttt = new Intent(EventsActivity.this, NavigationFragment.class);
        //startActivity(intenttt);

        /*NavigationFragment navigationFragment = new NavigationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, navigationFragment);
        transaction.addToBackStack(null); // Optional: allows users to navigate back
        transaction.commit();*/

        // ++side menu

        sideMenu = findViewById(R.id.side_menu_constraint_layout);
        TextView menuButton = findViewById(R.id.side_menu_button);
        ImageButton exitButton = findViewById(R.id.exit_btn);
        View sideMenuBg = findViewById(R.id.bg_view);



        // Button to toggle the menu (external)
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
                //sideMenuBg.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the TextView
                        sideMenuBg.setVisibility(View.VISIBLE);
                    }
                }, 0);
            }
        });

        // Button to close the menu (inside the side menu)
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideOutMenu();
                //sideMenuBg.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the TextView
                        sideMenuBg.setVisibility(View.GONE);
                    }
                }, 0);
                isMenuOpen = false; // Update the menu state
            }
        });

        sideMenuBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideOutMenu();
                //sideMenuBg.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the TextView
                        sideMenuBg.setVisibility(View.GONE);
                    }
                }, 0);
                isMenuOpen = false; // Update the menu state
            }
        });

        // --side menu

        // ++menuButtons

        android.widget.Toolbar spravaToolbar = findViewById(R.id.sprava_skupin_toolbar);
        ImageView spravaImgView = findViewById(R.id.sprava_skupin_imgview);
        TextView spravaTextView = findViewById(R.id.sprava_skupin_textview);

        spravaToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, ManageGroupsActivity.class);
                startActivity(intent);
            }
        });
        spravaImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, ManageGroupsActivity.class);
                startActivity(intent);
            }
        });
        spravaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, ManageGroupsActivity.class);
                startActivity(intent);
            }
        });

        android.widget.Toolbar pokladnikToolbar = findViewById(R.id.pokladnik_toolbar);
        ImageView pokladnikImgView = findViewById(R.id.pokladnik_imgview);
        TextView pokladnikTextView = findViewById(R.id.pokladnik_textview);

        pokladnikToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, TreasureActivity.class);
                startActivity(intent);
            }
        });
        pokladnikImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, TreasureActivity.class);
                startActivity(intent);
            }
        });
        pokladnikTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, TreasureActivity.class);
                startActivity(intent);
            }
        });

        android.widget.Toolbar infoToolbar = findViewById(R.id.info_toolbar);
        ImageView infoImgView = findViewById(R.id.info_imgview);
        TextView infoTextView = findViewById(R.id.info_textview);

        infoToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, AboutAppActivity.class);
                startActivity(intent);
            }
        });
        infoImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, AboutAppActivity.class);
                startActivity(intent);
            }
        });
        infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, AboutAppActivity.class);
                startActivity(intent);
            }
        });

        android.widget.Toolbar spravaProfiluToolbar = findViewById(R.id.sprava_profilu_toolbar);
        ImageView spravaProfiluImgView = findViewById(R.id.sprava_profilu_imgview);
        TextView spravaProfiluTextView = findViewById(R.id.sprava_profilu_textview);

        spravaProfiluToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, ProfilesActivity.class);
                startActivity(intent);
            }
        });
        spravaProfiluImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, ProfilesActivity.class);
                startActivity(intent);
            }
        });
        spravaProfiluTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, ProfilesActivity.class);
                startActivity(intent);
            }
        });

        android.widget.Toolbar nastaveniToolbar = findViewById(R.id.nastaveni_toolbar);
        ImageView nastaveniImgView = findViewById(R.id.nastaveni_imageview);
        TextView nastaveniTextView = findViewById(R.id.nastaveni_textview);

        nastaveniToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        nastaveniImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        nastaveniTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // --menuButtons




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
        //SideMenuHelper sideMenuHelper = new SideMenuHelper(sideMenu, profile, this);
        //sideMenuHelper.initiateSideMenu();

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

        // ++Filter btn


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton timeTableIcon = findViewById(R.id.time_table_icon);
        ImageButton eventsIcon = findViewById(R.id.events_icon);
        ImageButton assessmentsIcon = findViewById(R.id.assessments_icon);


        Button filterBtn = findViewById(R.id.filter_button);
        ConstraintLayout filterLayout = findViewById(R.id.filter_udalosti);
        TextView menuBtn = findViewById(R.id.side_menu_button);
        ScrollView udalostiScrollView = findViewById(R.id.scrollView2);
        FloatingActionButton createEventBtn = findViewById(R.id.button_create_new_event);

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);
                    filterBtn.setEnabled(false);
                    menuBtn.setEnabled(false);
                    udalostiScrollView.setEnabled(false);
                    createEventBtn.setEnabled(false);
                    timeTableIcon.setEnabled(false);
                    eventsIcon.setEnabled(false);
                    assessmentsIcon.setEnabled(false);
                }else {
                    filterLayout.setVisibility(View.GONE);
                    filterBtn.setEnabled(true);
                    menuBtn.setEnabled(true);
                    udalostiScrollView.setEnabled(true);
                    createEventBtn.setEnabled(true);
                    timeTableIcon.setEnabled(true);
                    eventsIcon.setEnabled(true);
                    assessmentsIcon.setEnabled(true);
                }
            }
        });

        ConstraintLayout confirmFilterLayout = findViewById(R.id.close_filter_layout);
        ImageButton closeFilterLayout = findViewById(R.id.close_filter_btn);

        closeFilterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.GONE);
                    filterBtn.setEnabled(true);
                    menuBtn.setEnabled(true);
                    udalostiScrollView.setEnabled(true);
                    createEventBtn.setEnabled(true);
                    timeTableIcon.setEnabled(true);
                    eventsIcon.setEnabled(true);
                    assessmentsIcon.setEnabled(true);
                }else {
                    filterLayout.setVisibility(View.VISIBLE);
                    filterBtn.setEnabled(false);
                    menuBtn.setEnabled(false);
                    udalostiScrollView.setEnabled(false);
                    createEventBtn.setEnabled(false);
                    timeTableIcon.setEnabled(false);
                    eventsIcon.setEnabled(false);
                    assessmentsIcon.setEnabled(false);
                }
            }
        });

        // --Filter btn

        // Set default fragment
        //loadFragment(new TimeTableFragment());


        timeTableIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragmentToLeft(new TimeTableFragment(EventsActivity.this));

                timeTableIcon.setEnabled(false);
                eventsIcon.setEnabled(true);
                assessmentsIcon.setEnabled(true);
            }
        });

        eventsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMainContent();

                timeTableIcon.setEnabled(true);
                assessmentsIcon.setEnabled(true);
            }
        });

        assessmentsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragmentToRight(new AssessmentsFragment());
                //loadFragment(new AssessmentsFragment());
                assessmentsIcon.setEnabled(false);
                timeTableIcon.setEnabled(true);
                eventsIcon.setEnabled(true);
            }
        });

        // Set up listeners for your toolbar buttons
        //timeTableIcon.setOnClickListener(v -> loadFragmentToLeft(new TimeTableFragment(EventsActivity.this)));
        //eventsIcon.setOnClickListener(v -> returnToMainContent());
        //assessmentsIcon.setOnClickListener(v -> loadFragmentToRight(new AssessmentsFragment()));

        //slideToActivity();
        refresh();
    }

    // ++sideMenu
    // Convert dp to pixels
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // Toggle menu open/close
    private void toggleMenu() {
        if (isMenuOpen) {
            slideOutMenu();
        } else {
            slideInMenu();
        }
        isMenuOpen = !isMenuOpen;
    }

    // Slide the menu in (make it visible)
    private void slideInMenu() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(sideMenu, "translationX", dpToPx(-450), 0);
        animator.setDuration(300); // Duration in milliseconds
        animator.start();
    }

    // Slide the menu out (hide it)
    private void slideOutMenu() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(sideMenu, "translationX", 0, dpToPx(-450));
        animator.setDuration(300); // Duration in milliseconds
        animator.start();
    }

    // --sideMenu

    // Method to load fragments into the container
    private void loadFragmentToRight(Fragment fragment) {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // Enter animation
                R.anim.slide_out_left,  // Exit animation
                R.anim.slide_in_left,   // Pop enter animation (when returning)
                R.anim.slide_out_right  // Pop exit animation
        );
        transaction.replace(R.id.mainActivity, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void loadFragmentToLeft(Fragment fragment) {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_left,  // Enter animation
                R.anim.slide_out_right,  // Exit animation
                R.anim.slide_in_right,   // Pop enter animation (when returning)
                R.anim.slide_out_left  // Pop exit animation
        );
        transaction.replace(R.id.mainActivity, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Return to MainActivity content (no fragment)
    private void returnToMainContent() {
        FragmentManager fragmentManager = getSupportFragmentManager();


        // Optional: If you want an animation for returning to the main content
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,  // Pop enter animation
                R.anim.slide_out_left// Pop exit animation
        );
        //fragmentManager.popBackStackImmediate();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //@SuppressLint("ResourceType") Fragment mainFragment = new Fragment(R.id.mainActivity);
        //transaction.replace(R.id.mainActivity, mainFragment);
        // Commit the transaction for the animation to take effect
        transaction.commit();
    }

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

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent2 = new Intent(EventsActivity.this, UdalostInfo.class);
                            intent.putExtra("task_description", task.cachedDescription);
                            startActivity(intent2);
                            System.out.println("fakči");
                        }
                    });

                    eventsLayout.addView(view);

                }
            });

        });
    }
}