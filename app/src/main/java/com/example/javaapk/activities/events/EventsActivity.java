package com.example.javaapk.activities.events;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapk.R;
import com.example.javaapk.activities.menu.SideMenuHelper;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.Task;
import net.anax.appServerClient.client.data.TaskAssignment;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!DataManager.getInstance().isProfileSelected()){
            ActivityUtilities.askToSelectProfile(this);
        }

        RecyclerView recyclerView = findViewById(R.id.event_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Profile profile = DataManager.getInstance().getSelectedProfile();
        SideMenuHelper sideMenuHelper = new SideMenuHelper(findViewById(R.id.sideMenu), profile, this);
        sideMenuHelper.initiateSideMenu();

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
                EventEntryTaskAdapter adapter = new EventEntryTaskAdapter(tasks);
                recyclerView.setAdapter(adapter);
            });

        });
    }
    static class EventEntryTaskAdapter extends RecyclerView.Adapter<EventEntryTaskAdapter.ViewHolder> {
        List<Task> tasks;
        EventEntryTaskAdapter(List<Task> tasks){
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public EventEntryTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_entry, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventEntryTaskAdapter.ViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.descriptionView.setText(task.cachedDescription);
            holder.typeView.setText(Task.TaskType.getTextId(task.cachedType));
            holder.dateView.setText(DateFormat.getDateInstance().format(new Date(task.cachedDueTimestamp*1000)));
        }

        public void add(Task task){
            tasks.add(task);
            notifyItemInserted(tasks.size()-1);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView descriptionView;
            TextView dateView;
            TextView typeView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                descriptionView = itemView.findViewById(R.id.event_entry_task_description);
                dateView = itemView.findViewById(R.id.event_entry_task_due_date);
                typeView = itemView.findViewById(R.id.event_entry_task_type);
            }
        }
    }

}