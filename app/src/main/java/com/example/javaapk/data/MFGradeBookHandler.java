package com.example.javaapk.data;

import net.anax.appServerClient.client.data.ClientUser;
import net.anax.appServerClient.client.data.MemoryManager;
import net.anax.appServerClient.client.data.TaskAssignment;

import org.json.simple.JSONObject;

import java.util.HashSet;

public class MFGradeBookHandler {
    String username;
    public MemoryManager memoryManager;
    public MFGradeBookHandler(String username, ClientUser user) {
        memoryManager = new MemoryManager(user);
        this.username = username;
    }

    public String getName(){
        return memoryManager.getClient().cachedName;
    }
    public TaskAssignment[] getTaskIds(){
        HashSet<TaskAssignment> set = memoryManager.getClient().cachedTaskAssignments;
        TaskAssignment[] ret = new TaskAssignment[set.size()];
        set.toArray(ret);
        return ret;
    }
    public JSONObject toJson(){
        JSONObject data = new JSONObject();
        data.put("username", username);
        return data;
    }
}
