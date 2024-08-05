package com.example.javaapk.activities.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.activities.menu.SelectUsersActivity;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.Task;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateNewEventActivity extends AppCompatActivity {
    Calendar selectedDatetime = Calendar.getInstance();
    int[] userIds = new int[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!DataManager.getInstance().isProfileSelected()){
            finish();
            return;
        }
        selectedDatetime.set(Calendar.SECOND, 0);

        Profile profile = DataManager.getInstance().getSelectedProfile();
        Spinner taskTypeSpinner = findViewById(R.id.spinner_select_task_type);
        Spinner groupSpinner = findViewById(R.id.spinner_select_task_group);

        ActivityUtilities.runNetworkOperation(() -> {
            int[] groupIds = profile.mfGradeBookHandler.getGroupIds();
            ArrayList<EntryWithId> groups = new ArrayList<>();
            groups.add(new EntryWithId(-1, getResources().getString(R.string.no_group)));
            for(int id : groupIds){
                try {
                    groups.add(new EntryWithId(id, profile.mfGradeBookHandler.memoryManager.getGroup(id).cachedName));
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
            }

            ActivityUtilities.runOnMainThread(() -> {
                ArrayAdapter<EntryWithId> adapter = new ArrayAdapter<EntryWithId>(this, android.R.layout.simple_spinner_dropdown_item, groups.toArray(new EntryWithId[0]));
                groupSpinner.setAdapter(adapter);
            });

        });

        ArrayList<EntryWithId> taskTypes = new ArrayList<>();
        for(Task.TaskType type : Task.TaskType.values()){
            if(type.type >= 0){
                taskTypes.add(new EntryWithId(type.type, getResources().getString(type.textId)));
            }
        }

        ArrayAdapter<EntryWithId> adapter = new ArrayAdapter<>(CreateNewEventActivity.this, android.R.layout.simple_spinner_dropdown_item, taskTypes.toArray(new EntryWithId[0]));
        taskTypeSpinner.setAdapter(adapter);

        ActivityResultLauncher<Intent>  launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getData() != null){
                int[] selectedUserIds = result.getData().getIntArrayExtra("selectedUserIds");
                if(selectedUserIds != null){
                    this.userIds = selectedUserIds;
                }
            }
        });

        TextView dateView = findViewById(R.id.new_event_date_text_view);
        TextView timeView = findViewById(R.id.new_event_time_text_view);
        Button submitButton = findViewById(R.id.submit_create_event_button);
        Button addUsersButton = findViewById(R.id.new_event_select_users_button);

        dateView.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDatetime.set(year, month, dayOfMonth);
                        refreshDatetimeView();
                    },
                    selectedDatetime.get(Calendar.YEAR),
                    selectedDatetime.get(Calendar.MONTH),
                    selectedDatetime.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        timeView.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        selectedDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDatetime.set(Calendar.MINUTE, minute);
                        refreshDatetimeView();
                    },
                    selectedDatetime.get(Calendar.HOUR_OF_DAY),
                    selectedDatetime.get(Calendar.MINUTE),
                    true);
            timePickerDialog.show();

        });

        addUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectUsersActivity.class);
            intent.putExtra("selectedUserIds", userIds);
            launcher.launch(intent);

        });

        submitButton.setOnClickListener(v -> {
            EntryWithId taskTypeEntry = (EntryWithId) taskTypeSpinner.getSelectedItem();
            EntryWithId groupEntry = (EntryWithId) groupSpinner.getSelectedItem();
            EditText descriptionText = findViewById(R.id.new_event_description_edit_text);

            if(taskTypeEntry != null && groupEntry != null){
                int taskType = taskTypeEntry.id;
                int groupId = groupEntry.id;
                String description = descriptionText.getText().toString();
                long dueTimestamp = selectedDatetime.getTimeInMillis();

                ActivityUtilities.runNetworkOperation(() -> {
                    try {

                        Task t = Task.requestCreateTask(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), dueTimestamp, description, taskType, userIds, DataManager.REMOTE_SERVER);
                        if(groupId >= 0){
                            t.requestSetGroupId(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), groupId, DataManager.REMOTE_SERVER);
                        }

                        sendBroadcast(new Intent("EVENT_CHANGED"));
                        ActivityUtilities.runOnMainThread(this::finish);
                    } catch (RequestFailedException | HttpErrorStatusException ignored) {}
                });

            }
        });
        refreshDatetimeView();
    }

    public void refreshDatetimeView(){
        TextView dateView = findViewById(R.id.new_event_date_text_view);
        TextView timeView = findViewById(R.id.new_event_time_text_view);

        String date = DateFormat.getDateInstance().format(selectedDatetime.getTime());
        String time = DateFormat.getTimeInstance().format(selectedDatetime.getTime());

        dateView.setText(date);
        timeView.setText(time);
    }

    public static class EntryWithId {
        int id;
        String name;

        public EntryWithId(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return name == null ? "" : name;
        }
    }
}