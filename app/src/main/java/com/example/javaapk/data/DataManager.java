package com.example.javaapk.data;

import android.content.Context;

import com.example.javaapk.GlobalConstants;

import net.anax.appServerClient.client.data.MissingDataException;
import net.anax.appServerClient.client.server.RemoteServer;
import net.anax.appServerClient.client.util.JsonUtilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataManager {
    private static DataManager INSTANCE = null;
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

    public JSONObject toJson(){
        JSONObject data = new JSONObject();

        JSONArray profilesJsonArray = new JSONArray();
        for(Profile p : profiles){
            profilesJsonArray.add(p.toJson());
        }

        data.put("profiles", profilesJsonArray);
        data.put("selected", isProfileSelected() ? profiles.indexOf(selectedProfile) : -1);
        return data;

    }

    public static DataManager fromJson(JSONObject data) throws MissingDataException {
        MissingDataException e = new MissingDataException("missing data in json");
        JSONArray profilesArray = JsonUtilities.extractJSONArray(data, "profiles", e);
        int selectedProfile = JsonUtilities.extractInt(data, "selected", e);

        ArrayList<Profile> profiles = new ArrayList<>();

        for(int i = 0; i < profilesArray.size(); i++){
            Profile p = Profile.fromJson((JSONObject) profilesArray.get(i));
            profiles.add(p);
        }

        DataManager dataManager = new DataManager();
        dataManager.profiles = profiles;
        if(selectedProfile != -1){
            dataManager.selectProfile(selectedProfile);
        }
        return dataManager;
    }

    public void saveData(Context context) throws IOException {
        String data = this.toJson().toJSONString();
        FileOutputStream fos = context.openFileOutput(GlobalConstants.saveFileName, Context.MODE_PRIVATE);
        fos.write(data.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    public static void loadData(Context context){
        StringBuilder builder = new StringBuilder();

        try {

            FileInputStream fis = context.openFileInput(GlobalConstants.saveFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

            String line;
            while((line = reader.readLine()) != null){
                builder.append(line).append("\n");
            }

            String fileData = builder.toString();
            JSONParser parser = new JSONParser();

            JSONObject data = (JSONObject) parser.parse(fileData);

            INSTANCE = DataManager.fromJson(data);

        } catch (FileNotFoundException e) {
            System.out.println("save file not found, aborting data loading");
        } catch (IOException e) {
            System.out.println("IOException while reading file, aborting data loading");
        } catch (ParseException e) {
            System.out.println("Parse exception while parsing json from the file, aborting loading");
        } catch (MissingDataException e) {
            System.out.println("MissingData exception when reconstructing data from the json, aborting loading");
        }
    }

}
