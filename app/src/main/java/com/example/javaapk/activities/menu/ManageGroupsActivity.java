package com.example.javaapk.activities.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.appServerClient.client.data.Group;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.util.ArrayList;
import java.util.List;

public class ManageGroupsActivity extends AppCompatActivity {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("GROUP_CHANGED")){
                refreshGroups(profile);
            }
        }
    };

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, new IntentFilter("GROUP_CHANGED"), Context.RECEIVER_NOT_EXPORTED);
        }else{
            registerReceiver(receiver, new IntentFilter("GROUP_CHANGED"));
        }

        if(!DataManager.getInstance().isProfileSelected()){
            ActivityUtilities.askToSelectProfile(this);
        }
        profile = DataManager.getInstance().getSelectedProfile();

        Button addNewGroupButton = findViewById(R.id.button_add_new_group);

        addNewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtilities.InputDialogHelper dialogHelper = new ActivityUtilities.InputDialogHelper(getResources().getString(R.string.enter_name), getResources().getString(R.string.name), ManageGroupsActivity.this);
                dialogHelper.initDialog((dialog, which) -> ActivityUtilities.runNetworkOperation(() -> {
                    dialog.dismiss();
                    try {
                        String name = dialogHelper.input.getText().toString();
                        if(name.isEmpty()){return;}
                        profile.mfGradeBookHandler.createGroup(name);
                        sendBroadcast(new Intent("GROUP_CHANGED"));
                    } catch (RequestFailedException | HttpErrorStatusException e) {
                        //TODO: handle unable to create group;
                    }}));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.group_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SideMenuHelper menuHelper = new SideMenuHelper(findViewById(R.id.sideMenu), profile, this);
        menuHelper.initiateSideMenu();

        refreshGroups(profile);
    }

    void refreshGroups(Profile profile){
        RecyclerView recyclerView = findViewById(R.id.group_list_recycler_view);
        ActivityUtilities.runNetworkOperation(() -> {
            int[] ids = profile.mfGradeBookHandler.getGroupIds();

            ArrayList<Group> groups = new ArrayList<>();
            for(int id: ids){
                try {
                    Group g = profile.mfGradeBookHandler.memoryManager.getGroup(id);
                    groups.add(g);
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
            }

            ActivityUtilities.runOnMainThread(()->{
                GroupEntryAdapter adapter = new GroupEntryAdapter(groups, ManageGroupsActivity.this);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    static class GroupEntryAdapter extends RecyclerView.Adapter<GroupEntryAdapter.ViewHolder>{
        List<Group> groups;
        Context context;
        GroupEntryAdapter(List<Group> groups, Context context){
            this.groups = groups;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_entry, parent, false);
            return new ViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Group g = groups.get(position);
            holder.groupNameView.setText(g.cachedName);
            holder.initListener(g.id);

        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        public Group getGroupAtPosition(int position){
            return groups.get(position);
        }
        static class ViewHolder extends RecyclerView.ViewHolder{
            TextView groupNameView;
            View itemView;
            Context context;
            public ViewHolder(@NonNull View itemView, Context context){
                super(itemView);
                groupNameView = itemView.findViewById(R.id.group_entry_name);
                this.itemView = itemView;
                this.context = context;
            }
            public void initListener(int groupId){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ManageSingleGroupActivity.class);
                        intent.putExtra("groupId", groupId);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}