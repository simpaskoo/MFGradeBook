package com.example.javaapk.data;

import net.anax.appServerClient.client.data.ClientUser;
import net.anax.appServerClient.client.data.MemoryManager;

import org.json.simple.JSONObject;

public class MFGradeBookHandler {
    String username;
    String name = null;
    String password = null;
    MemoryManager memoryManager;
    public MFGradeBookHandler(String username, ClientUser user) {
        memoryManager = new MemoryManager(user);
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public JSONObject toJson(){
        JSONObject data = new JSONObject();
        data.put("username", username);
        if(password != null){
            data.put("password", password);
        }
        return data;
    }
}
