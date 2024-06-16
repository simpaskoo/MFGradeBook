package com.example.javaapk.data;

import net.anax.appServerClient.client.data.ClientUser;
import net.anax.appServerClient.client.data.RequestFailedException;
import net.anax.appServerClient.client.http.HttpErrorStatusException;

import org.json.simple.JSONObject;

public class Profile {

    String name = null;
    String password = null;
    public String username;

    MFGradeBookHandler mfGradeBookHandler;
    SkolaOnlineHandler skolaOnlineHandler = null;
    public Profile(String username, String password) throws RequestFailedException, HttpErrorStatusException {
        ClientUser user = ClientUser.login(username, password, DataManager.REMOTE_SERVER);

        this.username = username;
        mfGradeBookHandler = new MFGradeBookHandler(username, user);
        mfGradeBookHandler.setPassword(password);

        this.name = mfGradeBookHandler.name;
    }
    public boolean hasSkolaOnline(){
        return skolaOnlineHandler != null;
    }

    public Profile addPassword(String password){this.password = password; return this;}
    public void forgetPassword(){
        this.password = null;
    }
    public boolean isPasswordRemembered(){
        return password != null;
    }
    public JSONObject toJson(){

        JSONObject data = new JSONObject();

        data.put("name", name);

        if(password != null){
            data.put("password", password);
        }

        if(skolaOnlineHandler != null){
            data.put("skolaOnlineHandler", skolaOnlineHandler.toJson());
        }

        if(mfGradeBookHandler != null){
            data.put("mfGradBookHanler", mfGradeBookHandler.toJson());
        }
        return data;
    }

}
