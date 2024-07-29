package com.example.javaapk.data;

import net.anax.appServerClient.client.data.ClientUser;
import net.anax.appServerClient.client.data.MemoryManager;

import org.json.simple.JSONObject;

public class MFGradeBookHandler {
    String username;
    MemoryManager memoryManager;
    public MFGradeBookHandler(String username, ClientUser user) {
        memoryManager = new MemoryManager(user);
        this.username = username;
    }

    public String getName(){
        return memoryManager.getClient().cachedName;
    }
    public JSONObject toJson(){
        JSONObject data = new JSONObject();
        data.put("username", username);
        return data;
    }
}
