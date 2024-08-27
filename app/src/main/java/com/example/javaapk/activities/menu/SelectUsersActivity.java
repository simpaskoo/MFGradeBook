package com.example.javaapk.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapk.R;
import com.example.javaapk.data.DataManager;
import com.example.javaapk.data.Profile;
import com.example.javaapk.util.ActivityUtilities;

import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.User;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import java.util.ArrayList;
import java.util.HashSet;

public class SelectUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aadynamic_add_user_layout);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        if(!DataManager.getInstance().isProfileSelected()){
            finish();
            return;
        }

        Profile profile = DataManager.getInstance().getSelectedProfile();

        Intent startingIntent = getIntent();
        int[] preselectedUserIds = startingIntent.getIntArrayExtra("selectedUserIds");

        if(preselectedUserIds == null){
            preselectedUserIds = new int[0];
        }
        final int[] finalPreselectedUserIds = preselectedUserIds;

        ActivityUtilities.runNetworkOperation(() -> {
            int[] groupIds = profile.mfGradeBookHandler.getGroupIds();
            HashSet<Integer> knownUserIds = new HashSet<>();
            ArrayList<User> knownUsers = new ArrayList<>();

            for(int groupId : groupIds){
                try {
                    ArrayList<Integer> userIds = profile.mfGradeBookHandler.memoryManager.getGroup(groupId).userIds;
                    knownUserIds.addAll(userIds);
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}
            }
            for(Integer userId : knownUserIds){
                try {
                    User u = profile.mfGradeBookHandler.memoryManager.getUser(userId);
                    knownUsers.add(u);
                } catch (RequestFailedException | HttpErrorStatusException ignored) {}

            }

            ActivityUtilities.runOnMainThread(() -> {
                RecyclerView recyclerView = findViewById(R.id.user_list_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(SelectUsersActivity.this));

                //GridLayout gridLayout = findViewById(R.id.grid_layout2);

                UserEntryAdapter adapter = new UserEntryAdapter(knownUsers, finalPreselectedUserIds);
                recyclerView.setAdapter(adapter);

                Button submitButton = findViewById(R.id.add_users);
                submitButton.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    int[] userIds = adapter.selectedUserIds.stream().mapToInt(i -> i).toArray();
                    ;System.out.println("adding this many user ids: " + adapter.selectedUserIds.size());
                    intent.putExtra("selectedUserIds", userIds);
                    setResult(RESULT_OK, intent);
                    finish();
                });

            });
        });

    }

    static class UserEntryAdapter extends RecyclerView.Adapter<UserEntryAdapter.ViewHolder>{
        ArrayList<User> users;
        HashSet<Integer> selectedUserIds = new HashSet<>();


        public UserEntryAdapter(ArrayList<User> users, int[] preselectedUsers) {
            this.users = users;
            for(int id : preselectedUsers){
                selectedUserIds.add(id);
            }
        }
        @NonNull
        @Override
        public UserEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_entry_selectable, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull UserEntryAdapter.ViewHolder holder, int position) {
            holder.userId = users.get(position).getId();
            holder.userName.setText(users.get(position).cachedName);

            holder.checkBox.setClickable(false);

            if(selectedUserIds.contains(holder.userId)){
                holder.checkBox.setChecked(true);
            }

            holder.itemView.setOnClickListener(v -> {
                holder.checkBox.setChecked(!selectedUserIds.contains(holder.userId));

                if(holder.checkBox.isChecked()){
                    selectedUserIds.add(holder.userId);
                }else{
                    selectedUserIds.remove(holder.userId);
                }
            });
        }
        @Override
        public int getItemCount() {
            return users.size();
        }
        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView userName;
            CheckBox checkBox;
            int userId;
            View itemView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.user_name_text_view);
                checkBox = itemView.findViewById(R.id.is_user_selected_checkbox);
                this.itemView = itemView;
            }
        }
    }

}