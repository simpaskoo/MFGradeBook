package com.example.javaapk.activities.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.Task;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateNewEventActivity extends AppCompatActivity {
    Calendar selectedDatetime = Calendar.getInstance();
    int[] userIds = new int[0];

    //Dynamic price
    public LinearLayout cenaLiner;
    public TextView cenaText;
    public TextView zacatekText;
    public TextView ukonceniText;
    public LinearLayout zacatekLinear;
    public LinearLayout ukonceniLinear;
    private Spinner mySpinner;
    private List<String> optionsList;
    //Dynamic price

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aapridat_udalost);

        //Price
        EditText editText = findViewById(R.id.editTextPrice);
        FloatingActionButton button = findViewById(R.id.submit_create_event_button);

        // Set onClickListener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the number from EditText
                String numberInput = editText.getText().toString();

                // Check if the input is not empty
                if (!numberInput.isEmpty()) {
                    // Parse the string to an integer (you can use float or double as per requirement)
                    int numberToSend = Integer.parseInt(numberInput);

                    // Create an Intent to start the SecondActivity
                    Intent intent = new Intent(CreateNewEventActivity.this, EventsActivity.class);

                    // Add the number to the Intent
                    intent.putExtra("number_key", numberToSend);

                    // Start the second activity
                    startActivity(intent);
                }
            }
        });
        //Price


        //Dynamic price
        // Find the Spinner and TextView by their IDs
        cenaLiner = findViewById(R.id.cena_linear);
        cenaText = findViewById(R.id.cena);
        zacatekText = findViewById(R.id.zacatek);
        ukonceniText = findViewById(R.id.ukonceni);
        zacatekLinear = findViewById(R.id.zacatek_info);
        ukonceniLinear = findViewById(R.id.ukonceni_info);
        mySpinner = findViewById(R.id.spinner_select_task_type);

        // Initialize the list of options for the Spinner
        optionsList = new ArrayList<>();
        optionsList.add("None");
        optionsList.add("Task");
        optionsList.add("Homework");
        optionsList.add("Test");
        optionsList.add("Payment");  // The item we want to trigger visibility
        optionsList.add("Event");

        // Set up the ArrayAdapter for the Spinner
        ArrayAdapter<String> adapterr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, optionsList);
        mySpinner.setAdapter(adapterr);

        // Set up the listener for the Spinner
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Show the TextView only when "Payment" is selected

                if (optionsList.get(position).equals("Payment")) {
                    cenaText.setVisibility(View.VISIBLE);
                    cenaLiner.setVisibility(View.VISIBLE);// Show the TextView
                    zacatekText.setVisibility(View.GONE);
                    zacatekLinear.setVisibility(View.GONE);
                } else {
                    cenaText.setVisibility(View.GONE);
                    cenaLiner.setVisibility(View.GONE);// Hide the TextView for other selections
                    zacatekText.setVisibility(View.VISIBLE);
                    zacatekLinear.setVisibility(View.VISIBLE);
                    if (optionsList.get(position).equals("Homework")) {
                        zacatekText.setVisibility(View.GONE);
                        zacatekLinear.setVisibility(View.GONE);
                    } else {
                        zacatekText.setVisibility(View.VISIBLE);
                        zacatekLinear.setVisibility(View.VISIBLE);
                        if (optionsList.get(position).equals("Task")) {
                            zacatekText.setVisibility(View.GONE);
                            zacatekLinear.setVisibility(View.GONE);
                        } else {
                            zacatekText.setVisibility(View.VISIBLE);
                            zacatekLinear.setVisibility(View.VISIBLE);
                        }
                    }
                }

                /*if (optionsList.get(position).equals("Task")) {
                    zacatekText.setVisibility(View.GONE);
                    zacatekLinear.setVisibility(View.GONE);
                } else {
                    zacatekText.setVisibility(View.VISIBLE);
                    zacatekLinear.setVisibility(View.VISIBLE);
                }

                if (optionsList.get(position).equals("Homework")) {
                    zacatekText.setVisibility(View.GONE);
                    zacatekLinear.setVisibility(View.GONE);
                } else {
                    zacatekText.setVisibility(View.VISIBLE);
                    zacatekLinear.setVisibility(View.VISIBLE);
                }*/

                /*if (optionsList.get(position).equals("Test")) {
                    cenaText.setVisibility(View.VISIBLE);
                    cenaLiner.setVisibility(View.VISIBLE);// Show the TextView
                } else {
                    cenaText.setVisibility(View.GONE);
                    cenaLiner.setVisibility(View.GONE);// Hide the TextView for other selections
                }*/



                /*if (optionsList.get(position).equals("Event")) {
                    cenaText.setVisibility(View.VISIBLE);
                    cenaLiner.setVisibility(View.VISIBLE);// Show the TextView
                } else {
                    cenaText.setVisibility(View.GONE);
                    cenaLiner.setVisibility(View.GONE);// Hide the TextView for other selections
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed here
            }
        });
        //Dynamic price

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

        TextView endDateView = findViewById(R.id.new_event_end_date_text_view);
        TextView endTimeView = findViewById(R.id.new_event_end_time_text_view);
        TextView startDateView = findViewById(R.id.new_event_start_date_text_view);
        TextView startTimeView = findViewById(R.id.new_event_start_time_text_view);
        FloatingActionButton submitButton = findViewById(R.id.submit_create_event_button);
        Button addUsersButton = findViewById(R.id.new_event_select_users_button);

        endDateView.setOnClickListener(v -> {
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

        endTimeView.setOnClickListener(v -> {
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


        startDateView.setOnClickListener(v -> {
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

        startTimeView.setOnClickListener(v -> {
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



        /*addUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectUsersActivity.class);
            intent.putExtra("selectedUserIds", userIds);
            launcher.launch(intent);

        });*/

        Spinner spinnerSelectTaskType = findViewById(R.id.spinner_select_task_type);
        ImageButton clearButton = findViewById(R.id.clear_btn);
        TextView popis = findViewById(R.id.popis);
        EditText newEventDescribe = findViewById(R.id.new_event_description_edit_text);
        TextView doSkupiny = findViewById(R.id.do_skupiny);
        Spinner selectTask = findViewById(R.id.spinner_select_task_group);
        TextView ucastnici = findViewById(R.id.ucastnici);
        TextView pocetUcastniku = findViewById(R.id.pocet_ucastniku);
        Button selectUsersBtn = findViewById(R.id.new_event_select_users_button);
        TextView ukonceni = findViewById(R.id.ukonceni);
        LinearLayout ukonceniInfo = findViewById(R.id.ukonceni_info);
        TextView cena = findViewById(R.id.cena);
        LinearLayout cenaInfo = findViewById(R.id.cena_linear);
        FloatingActionButton submitNewEvent = findViewById(R.id.submit_create_event_button);

        Toolbar toolbar = findViewById(R.id.add_users_toolbar);
        View vieww = findViewById(R.id.vieww);
        ImageButton imageButton = findViewById(R.id.clear_btn5);

        addUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vieww.getVisibility() == View.GONE) {
                    vieww.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);

                    spinnerSelectTaskType.setEnabled(false);
                    clearButton.setEnabled(false);
                    popis.setEnabled(false);
                    newEventDescribe.setEnabled(false);
                    doSkupiny.setEnabled(false);
                    selectTask.setEnabled(false);
                    ucastnici.setEnabled(false);
                    pocetUcastniku.setEnabled(false);
                    selectUsersBtn.setEnabled(false);
                    ukonceni.setEnabled(false);
                    ukonceniInfo.setEnabled(false);
                    cena.setEnabled(false);
                    cenaInfo.setEnabled(false);
                    submitNewEvent.setEnabled(false);



                } else {
                    vieww.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                }
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vieww.getVisibility() == View.VISIBLE) {
                    vieww.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);

                    spinnerSelectTaskType.setEnabled(true);
                    clearButton.setEnabled(true);
                    popis.setEnabled(true);
                    newEventDescribe.setEnabled(true);
                    doSkupiny.setEnabled(true);
                    selectTask.setEnabled(true);
                    ucastnici.setEnabled(true);
                    pocetUcastniku.setEnabled(true);
                    selectUsersBtn.setEnabled(true);
                    ukonceni.setEnabled(true);
                    ukonceniInfo.setEnabled(true);
                    cena.setEnabled(true);
                    cenaInfo.setEnabled(true);
                    submitNewEvent.setEnabled(true);
                } else {
                    vieww.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }

            }
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

                long startTimestamp = dueTimestamp;
                String name = description;

                ActivityUtilities.runNetworkOperation(() -> {
                    try {

                        Task t = Task.requestCreateTask(profile.mfGradeBookHandler.memoryManager.getClient().getToken(), dueTimestamp, startTimestamp, name,description, taskType, userIds, DataManager.REMOTE_SERVER);
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

        ImageButton back = findViewById(R.id.clear_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void refreshDatetimeView(){
        TextView endDateView = findViewById(R.id.new_event_end_date_text_view);
        TextView endTimeView = findViewById(R.id.new_event_end_time_text_view);
        TextView startDateView = findViewById(R.id.new_event_start_date_text_view);
        TextView startTimeView = findViewById(R.id.new_event_start_time_text_view);

        String date = DateFormat.getDateInstance().format(selectedDatetime.getTime());
        String time = DateFormat.getTimeInstance().format(selectedDatetime.getTime());

        endDateView.setText(date);
        endTimeView.setText(time);
        startDateView.setText(date);
        startTimeView.setText(time);
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