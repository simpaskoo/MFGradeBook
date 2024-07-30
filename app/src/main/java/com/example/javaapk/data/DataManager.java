package com.example.javaapk.data;

import com.example.javaapk.GlobalConstants;

import net.anax.appServerClient.client.server.RemoteServer;

import java.util.ArrayList;

public class DataManager {
    public static DataManager INSTANCE = null;
    public static RemoteServer REMOTE_SERVER = new RemoteServer(GlobalConstants.serverUrl);

    public ArrayList<Profile> profiles = new ArrayList<>();
    Profile selectedProfile = null;

    public DataManager(){
    }
    public static DataManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public int addProfile(Profile profile){
        //adds the profile and returns its index;
        profiles.add(profile);
        return profiles.size()-1;
    }
    public void selectProfile(Profile profile){
        this.selectedProfile = profile;
    }
    public void selectProfile(int profileIndex){
        this.selectProfile(profiles.get(profileIndex));
    }
    public boolean isProfileSelected(){
        return selectedProfile != null;
    }
    public Profile getSelectedProfile(){
        return selectedProfile;
    }

}
