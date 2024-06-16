package com.example.javaapk.data;

import com.example.javaapk.util.JsonUtilities;

import org.json.simple.JSONObject;

public class SkolaOnlineHandler {
    String username;
    String password;
    public SkolaOnlineHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public JSONObject toJson(){
        JSONObject data = new JSONObject();
        data.put("username", username);
        data.put("password", password);

        return data;
    }

    public static SkolaOnlineHandler fromJson(JSONObject data) throws DataReadException {
        DataReadException e = new DataReadException("unable to reconstruct Handler, missing fields.", DataReadException.DataReadExceptionType.DataNotPresent);
        return new SkolaOnlineHandler(JsonUtilities.extractString(data, "username", e), JsonUtilities.extractString(data, "password", e));
    }
}
