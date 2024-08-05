package net.anax.appServerClient.client.data;

import net.anax.appServerClient.client.http.HttpErrorStatusException;
import net.anax.appServerClient.client.server.RemoteServer;
import net.anax.appServerClient.client.server.Token;
import net.anax.appServerClient.client.util.HttpUtilities;
import net.anax.appServerClient.client.util.JsonUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Group {
    public int id;
    public String cachedName = "";
    public String cachedAccessCode = "";
    public int treasurerUserId = ID.UNKNOWN.id;
    public int adminUserId = ID.UNKNOWN.id;
    public ArrayList<Integer> userIds = new ArrayList<>();
    ArrayList<Integer> taskIds = new ArrayList<>();
    public Group(int id){
        this.id = id;
    }

    public static Group requestGroupFromId(Token token, int id, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        Group g = new Group(id);
        g.requestGetGroup(token, server);
        return g;
    }
    public void requestGetGroup(Token token, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {

        JSONObject data = new JSONObject();
        data.put("groupId", id);
        JSONObject response = HttpUtilities.doRequest(server.getUrl() + "/group/getGroup", data, token.getTokenString());

        RequestFailedException e = new RequestFailedException("necessary data not found in response: " + response.toJSONString());

        this.cachedName = JsonUtilities.extractString(response, "name", e);
        this.adminUserId = JsonUtilities.extractInt(response, "admin_id", e);
        this.cachedAccessCode = JsonUtilities.extractString(response, "accessCode", e);

        JSONArray userIds = JsonUtilities.extractJSONArray(response, "userIds", e);
        JSONArray taskIds = JsonUtilities.extractJSONArray(response, "taskIds", e);

        ArrayList<Integer> userIdsArray = new ArrayList<>();
        ArrayList<Integer> taskIdsArray = new ArrayList<>();

        for(int i = 0; i < userIds.size(); i++){
            if(userIds.get(i) instanceof Long){
                userIdsArray.add((int)(long)userIds.get(i));
            }
        }
        for(int i = 0; i < taskIds.size(); i++){
            if(taskIds.get(i) instanceof Long){
                taskIdsArray.add((int)(long)taskIds.get(i));
            }
        }

        try{
            this.treasurerUserId = JsonUtilities.extractInt(response, "treasurerUserId", new MissingDataException("no treasurer id"));
        } catch(MissingDataException ex){
            this.treasurerUserId = ID.NONE.id;
        }
        System.out.println("treasurer user id: " + this.treasurerUserId);

        this.taskIds = taskIdsArray;
        this.userIds = userIdsArray;
    }
    public void requestSetName(Token token, String newName, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("groupId", id);
        data.put("newName", newName);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl()+"/group/setName", data, token.getTokenString()));
        this.cachedName = newName;
    }

    public void requestRerollAccessCode(Token token, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("groupId", id);
        JSONObject response = HttpUtilities.doRequest(server.getUrl()+"/group/rerollAccessCode", data, token.getTokenString());
        this.cachedAccessCode = JsonUtilities.extractString(response, "newAccessCode", new RequestFailedException("response did not contain necessary data"));
    }

    public void requestSetTreasurerUserId(Token token, int newTreasurerUserId, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("groupId", id);
        data.put("newTreasurerUserId", newTreasurerUserId);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/group/setTreasurerUserId", data, token.getTokenString()));
        this.treasurerUserId = newTreasurerUserId;
    }
    public void requestSetAdminUserId(Token token, int newAdminId, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("groupId", id);
        data.put("newAdminId", newAdminId);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/group/setAdminUserId", data, token.getTokenString()));
        this.adminUserId = newAdminId;
    }

    public void requestSetIsInGroup(Token token, int userId, boolean isInGroup, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("groupId", id);
        data.put("userId", userId);
        data.put("isInGroup", isInGroup);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/group/setIsInGroup", data, token.getTokenString()));
        this.requestGetGroup(token, server);
    }

    public static Group createGroup(Token token, String name, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("authorUserId", token.subject);
        JSONObject response = HttpUtilities.doRequest(server.getUrl() + "/group/createGroup", data, token.getTokenString());
        if(!response.containsKey("id") || !(response.get("id") instanceof Long)){
            throw new RequestFailedException("necessary data not found in request");
        }
        Group g = new Group((int)(long)response.get("id"));
        g.requestGetGroup(token, server);
        return g;
    }

    public void requestRemoveTreasurer(Token token, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("groupId", id);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl()+"/group/removeTreasurer", data, token.getTokenString()));
        this.treasurerUserId = ID.NONE.id;
    }

    public JSONObject getJSON(){
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("treasurerUserId", treasurerUserId);
        data.put("adminUserID", adminUserId);
        data.put("accessCode", cachedAccessCode);

        JSONArray userIdsArray = new JSONArray();
        JSONArray taskIdsArray = new JSONArray();
        for(Integer id : this.userIds){
            userIdsArray.add(id);
        }
        for(Integer id : this.taskIds){
            taskIdsArray.add(id);
        }

        data.put("userIds", userIdsArray);
        data.put("taskIds", taskIdsArray);

        return data;
    }

    public static Group getFromJSON(JSONObject data) throws MissingDataException{
        MissingDataException e = new MissingDataException("the json does not contain data necessary to recreate user");

        JSONArray userIdsArray = JsonUtilities.extractJSONArray(data, "userIds", e);
        JSONArray taskIdsArray = JsonUtilities.extractJSONArray(data, "taskIds", e);

        int id = JsonUtilities.extractInt(data, "id", e);
        String name = JsonUtilities.extractString(data, "name", e);
        int treasurerUserId = JsonUtilities.extractInt(data, "treasurerUserId", e);
        int adminUserId = JsonUtilities.extractInt(data, "adminUserId", e);
        String accessCode = JsonUtilities.extractString(data, "accessCode", e);

        Group g = new Group(id);
        g.cachedName = name;
        g.treasurerUserId = treasurerUserId;
        g.adminUserId = adminUserId;
        g.cachedAccessCode = accessCode;

        for(Object o : userIdsArray){
            if(o instanceof Long){
                g.userIds.add((int)(long)o);
            }
        }
        for(Object o : taskIdsArray){
            if(o instanceof Long){
                g.taskIds.add((int)(long)o);
            }
        }
        return g;
    }

    public void printSelf() {
        System.out.println("------Group printSelf start---------");
        System.out.println("id: " + id);
        System.out.println("name: " + cachedName);
        System.out.println("admin: " + adminUserId);
        System.out.println("treasurer: " + treasurerUserId);
        for(Integer id : userIds){
            System.out.println("member: " + id);
        }
        for(Integer id : taskIds){
            System.out.println("task: " + id);
        }
    }
}
