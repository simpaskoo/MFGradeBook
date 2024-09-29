package net.anax.appServerClient.client.data;

import com.example.javaapk.R;

import net.anax.appServerClient.client.http.HttpErrorStatusException;
import net.anax.appServerClient.client.server.RemoteServer;
import net.anax.appServerClient.client.server.Token;
import net.anax.appServerClient.client.util.HttpUtilities;
import net.anax.appServerClient.client.util.JsonUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
public class Task {
    int id;
    public long cachedDueTimestamp = 0;
    public long cachedStartTimestamp = 0;
    public String cachedDescription = "";
    public String cachedName = "";
    public int cachedType = TaskType.UNKNOWN.type;
    public int cachedGroupId = ID.UNKNOWN.id;
    public int cachedAmount = ID.UNKNOWN.id;


    ArrayList<Integer> cachedUserIds = new ArrayList<>();
    public Task(int id){
        this.id = id;
    }

    public static Task requestTaskFromId(Token token, int id, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        Task task = new Task(id);
        task.requestGetTask(token, server);
        return task;
    }
    public void requestGetTask(Token token, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("taskId", id);
        JSONObject response = HttpUtilities.doRequest(server.getUrl() + "/task/getTask", data, token.getTokenString());

        RequestFailedException e = new RequestFailedException("response does not contain necessary data");

        long dueTimestamp = JsonUtilities.extractLong(response, "dueTimestamp", e);
        String description = JsonUtilities.extractString(response, "description", e);
        String name = JsonUtilities.extractString(response, "name", e);
        int type = JsonUtilities.extractInt(response, "type", e);
        long startTimestamp = JsonUtilities.extractLong(response, "startTimestamp", e);

        this.cachedDueTimestamp = dueTimestamp;
        this.cachedStartTimestamp = startTimestamp;
        this.cachedName = name;
        this.cachedDescription = description;
        this.cachedType = type;

        if(response.containsKey("groupId") && response.get("groupId") instanceof Long){
            cachedGroupId = (int) ((Long)response.get("groupId")).longValue();
        }
        if(response.containsKey("amount") && response.get("amount") instanceof Long){
            this.cachedAmount = (int) ((Long)response.get("amount")).longValue();
        }else{
            this.cachedAmount = ID.NONE.id;
        }

        JSONArray userIds = JsonUtilities.extractJSONArray(response, "userIds", e);

        for(int i = 0; i < userIds.size(); i++){
            if(userIds.get(i) instanceof Long){
                cachedUserIds.add((int) ((Long)userIds.get(i)).longValue());
            }
        }
    }

    public static Task requestCreateTask(Token token, long dueTimestamp, long startTimestamp, String name, String description, int type, int[] userIds, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        JSONArray userIdsJSONArray = new JSONArray();
        for(int userId : userIds){
            userIdsJSONArray.add(userId);
        }
        data.put("dueTimestamp", dueTimestamp);
        data.put("startTimestamp", startTimestamp);
        data.put("name", name);
        data.put("description", description);
        data.put("type", type);
        data.put("authorUserId", token.subject);
        data.put("userIds", userIdsJSONArray);

        JSONObject response = HttpUtilities.doRequest(server.getUrl() + "/task/createTask", data, token.getTokenString());

        int id = JsonUtilities.extractInt(response, "id", new RequestFailedException("necessary data not found in response" + response.toJSONString()));

        Task createdTask = new Task(id);
        createdTask.cachedDescription = description;
        createdTask.cachedDueTimestamp = dueTimestamp;
        createdTask.cachedType = type;

        createdTask.cachedAmount = ID.NONE.id;
        createdTask.cachedGroupId = ID.NONE.id;

        for(int userId : userIds){
            createdTask.cachedUserIds.add(userId);
        }

        return createdTask;
    }

    public void requestSetDueTimestamp(Token token, long timestamp, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("taskId", id);
        data.put("timestamp", timestamp);
        JSONObject response = HttpUtilities.doRequest(server.getUrl() + "task/setDueTimestamp", data, token.getTokenString());
        HttpUtilities.validateSuccess(response);
        this.cachedDueTimestamp = timestamp;
    }
    public void requestSetDescription(Token token, String description, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("description", description);
        data.put("taskId",id);
        JSONObject response = HttpUtilities.doRequest(server.getUrl() + "task/setDescription", data, token.getTokenString());
        HttpUtilities.validateSuccess(response);
        this.cachedDescription = description;
    }

    public void requestSetType(Token token, int newType, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("newType", newType);
        data.put("taskId",id);
        JSONObject response = HttpUtilities.doRequest(server.getUrl() + "/task/setType", data, token.getTokenString());
        HttpUtilities.validateSuccess(response);
        this.cachedType = newType;
    }

    public void requestSetGroupId(Token token, int newGroupId, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("newGroupId", newGroupId);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/task/setGroup", data, token.getTokenString()));
        this.cachedGroupId = newGroupId;
    }

    public void requestRemoveGroupId(Token token, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("taskId", id);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/task/removeGroupId", data, token.getTokenString()));
        this.cachedGroupId = ID.NONE.id;
    }

    public void requestRemoveAmount(Token token, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("taskId", id);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/task/removeAmount", data, token.getTokenString()));
        this.cachedAmount = ID.NONE.id;
    }

    public void requestSetAmount(Token token, int amount, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("amount", amount);
        data.put("taskId", id);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/task/setAmount", data, token.getTokenString()));
        this.cachedAmount = amount;
    }
    public void requestSetHasUserTask(Token token, int userId, boolean hasTask, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("taskId",id);
        data.put("userId", userId);
        data.put("hasTask", hasTask);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/task/setHasUserTask",data, token.getTokenString()));
        this.requestGetTask(token, server);
    }

    public void requestSetCompleteness(Token token, int userId, boolean isComplete, RemoteServer server) throws RequestFailedException, HttpErrorStatusException {
        JSONObject data = new JSONObject();
        data.put("taskId", id);
        data.put("userId", userId);
        data.put("isComplete", isComplete);
        HttpUtilities.validateSuccess(HttpUtilities.doRequest(server.getUrl() + "/task/setCompleteness",data, token.getTokenString()));
    }

    public JSONObject getJSON(){
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("description", cachedDescription);
        data.put("dueTimestamp", cachedDueTimestamp);
        data.put("groupId", cachedGroupId);
        data.put("type", cachedType);
        data.put("amount", cachedAmount);
        return data;
    }
    public static Task getFromJSON(JSONObject data) throws MissingDataException {
        MissingDataException e = new MissingDataException("necessary data to recreate task not found");

        int id = JsonUtilities.extractInt(data, "id", e);
        String description = JsonUtilities.extractString(data, "description", e);
        long due = JsonUtilities.extractInt(data, "dueTimestamp", e);
        int groupId = JsonUtilities.extractInt(data, "groupId", e);
        int type = JsonUtilities.extractInt(data, "type", e);
        int amount = JsonUtilities.extractInt(data, "amount", e);

        Task t = new Task(id);
        t.cachedDescription = description;
        t.cachedDueTimestamp = due;
        t.cachedGroupId = groupId;
        t.cachedType = type;
        t.cachedAmount = amount;

        return t;
    }
    public void printSelf() {
        System.out.println("-------Task printSelf start--------");
        System.out.println("id: " + id);
        System.out.println("description: " + cachedDescription);
        System.out.println("due: " + cachedDueTimestamp);
        System.out.println("groupId: " + cachedGroupId);
        System.out.println("type: " + cachedType);
        System.out.println("amount: " + cachedAmount);
    }

    public static enum TaskType{
        UNKNOWN(-2, R.string.task_type_unknown),
        NONE(0, R.string.task_type_none),
        TASK(1, R.string.task_type_task),
        HOMEWORK(2, R.string.task_type_homework),
        TEST(3, R.string.task_type_test),
        PAYMENT(4, R.string.task_type_payment),
        EVENT(5, R.string.task_type_event)
        ;

        public final int type;
        public final int textId;

        TaskType(int type, int textId){
            this.type = type;
            this.textId = textId;
        }
        public static int getTextId(int type){
            for(TaskType taskType : TaskType.values()){
                if (taskType.type == type){return taskType.textId;}
            }
            return R.string.task_type_not_recognized;
        }

    }

}
