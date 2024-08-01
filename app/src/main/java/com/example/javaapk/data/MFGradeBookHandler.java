package com.example.javaapk.data;

import net.anax.appServerClient.client.data.ClientUser;
import net.anax.appServerClient.client.data.Group;
import net.anax.appServerClient.client.data.MemoryManager;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.TaskAssignment;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import org.json.simple.JSONObject;

import java.util.ArrayList;
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

    public int[] getGroupIds(){
        HashSet<Integer> set = memoryManager.getClient().cachedGroupIds;
        return set.stream().mapToInt(Number::intValue).toArray();
    }

    public int[] getUsersFromGroups(){
        int[] groupIds = getGroupIds();

        HashSet<Integer> userIds = new HashSet<>();
        for(int groupId : groupIds){
            try {
                Group g = memoryManager.getGroup(groupId);
                userIds.addAll(g.userIds);
            } catch (RequestFailedException | HttpErrorStatusException e) {
                continue;
            }
        }
        return userIds.stream().mapToInt(Number::intValue).toArray();
    }

    public Group createGroup(String name) throws RequestFailedException, HttpErrorStatusException {
        Group g = Group.createGroup(memoryManager.getClient().getToken(), name, DataManager.REMOTE_SERVER);

        memoryManager.rememberedGroups.put(g.id, g);
        return g;

    }

    public JSONObject toJson(){
        JSONObject data = new JSONObject();
        data.put("username", username);
        return data;
    }
}
