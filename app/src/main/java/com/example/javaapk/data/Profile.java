package com.example.javaapk.data;

import net.anax.appServerClient.client.data.ClientUser;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.TaskAssignment;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import org.json.simple.JSONObject;

public class Profile {

    public String username;

    public MFGradeBookHandler mfGradeBookHandler;
    SkolaOnlineHandler skolaOnlineHandler = null;
    public Profile(String username, String password) throws RequestFailedException, HttpErrorStatusException {
        ClientUser user = ClientUser.login(username, password, DataManager.REMOTE_SERVER);

        this.username = username;
        mfGradeBookHandler = new MFGradeBookHandler(username, user);

    }

    public void verifyPassword(String password) throws RequestFailedException, HttpErrorStatusException {
        ClientUser.login(username, password, DataManager.REMOTE_SERVER);
    }
    public boolean hasSkolaOnline(){
        return skolaOnlineHandler != null;
    }

    public String getName(){
        return mfGradeBookHandler.getName();
    }

    public TaskAssignment[] getTaskAssignments(){
        return mfGradeBookHandler.getTaskIds();
    }

    public Profile addPassword(String password){
        mfGradeBookHandler.memoryManager.client.setPassword(password);
        return this;
    }
    public boolean isPasswordRemembered(){
        return mfGradeBookHandler.memoryManager.client.isPasswordRemembered();
    }

    public int getId(){
        return mfGradeBookHandler.memoryManager.client.getId();
    }
    public JSONObject toJson(){

        JSONObject data = new JSONObject();

        data.put("username", username);

        if(skolaOnlineHandler != null){
            data.put("skolaOnlineHandler", skolaOnlineHandler.toJson());
        }

        if(mfGradeBookHandler != null){
            data.put("mfGradBookHanler", mfGradeBookHandler.toJson());
        }
        return data;
    }

}
