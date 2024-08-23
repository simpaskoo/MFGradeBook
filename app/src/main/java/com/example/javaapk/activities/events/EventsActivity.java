package com.example.javaapk.activities.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.activities.menu.SideMenuHelper;
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
        //SideMenuHelper sideMenuHelper = new SideMenuHelper(findViewById(R.id.sideMenu), profile, this);
        //sideMenuHelper.initiateSideMenu();

        FloatingActionButton createNewEventButton = findViewById(R.id.button_create_new_event);

        createNewEventButton.setOnClickListener(V -> {
            Intent intent = new Intent(this, CreateNewEventActivity.class);
            startActivity(intent);
        });

        refresh();
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
                    TextView amount = view.findViewById(R.id.text_view_event_amount);

                    description.setText(task.cachedDescription);
                    date.setText(DateFormat.getDateTimeInstance().format(new Date(task.cachedDueTimestamp * 1000)));

                    boolean isPayment = task.cachedType == Task.TaskType.PAYMENT.type;

                    if(isPayment && task.cachedAmount >= 0) {
                        amount.setText(format.format(task.cachedAmount / 100f) + " ,-");
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