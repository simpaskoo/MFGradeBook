package com.example.javaapk.data;

import net.anax.appServerClient.client.data.ClientUser;
import net.anax.appServerClient.client.data.MissingDataException;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.data.TaskAssignment;
import net.anax.appServerClient.client.http.HttpErrorStatusException;
import net.anax.appServerClient.client.util.JsonUtilities;
import net.anax.skolaOnlineScraper.data.InvalidDataInJsonException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Profile {

    public String username;

    public MFGradeBookHandler mfGradeBookHandler = null;
    public SkolaOnlineHandler skolaOnlineHandler = null;

    public Profile(String username, String password) throws RequestFailedException, HttpErrorStatusException {
        ClientUser user = ClientUser.login(username, password, DataManager.REMOTE_SERVER);
        this.username = username;
        mfGradeBookHandler = new MFGradeBookHandler(username, user);
    }

    public void initiateSkolaOnline(String username, String password){
        this.skolaOnlineHandler = new SkolaOnlineHandler(username, password);
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
            data.put("skon", skolaOnlineHandler.toJson());
        }

        if(mfGradeBookHandler != null){
            data.put("mfgb", mfGradeBookHandler.toJson());
        }
        return data;
    }

    private Profile(String username){
        this.username = username;
    }

    public static Profile fromJson(JSONObject data) throws MissingDataException {

        MissingDataException e = new MissingDataException("not enough data in json");
        MFGradeBookHandler mfgbHandler = null;
        SkolaOnlineHandler skonHandler = null;

        try{
            JSONObject mfgb = JsonUtilities.extractJSONObject(data, "mfgb", e);
            mfgbHandler = MFGradeBookHandler.fromJson(mfgb);

        }catch (MissingDataException ignored){}

        try{
            JSONObject skon = JsonUtilities.extractJSONObject(data, "skon", e);
            skonHandler = SkolaOnlineHandler.fromJson(skon);

        }catch (MissingDataException | InvalidDataInJsonException | ParseException | DataReadException ignored){}

        String username = JsonUtilities.extractString(data, "username", e);
        Profile p = new Profile(username);

        p.mfGradeBookHandler = mfgbHandler;
        p.skolaOnlineHandler = skonHandler;
        return p;
    }
}
