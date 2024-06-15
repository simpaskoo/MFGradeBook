package net.anax.appServerClient.client.http;

import java.util.HashMap;

public class HttpResponse {
    String payload;
    public String message;
    public int responseCode;
    HashMap<String, String> headers = new HashMap<>();
    HttpResponse(String message, int responseCode){
        this.message = message;
        this.responseCode = responseCode;
    }

    public void printSelf(){
        System.out.println("status code: " + responseCode);
        System.out.println("message: " + message);
        System.out.println("headers:");
        for(String key : headers.keySet()){
            System.out.println("\t" + key + ": " + headers.get(key));
        }
        System.out.println("payload: " + payload);
    }

    public HttpResponse setPayload(String payload){
        this.payload = payload;
        return this;
    }

    public HttpResponse addHeader(HttpHeader header, String value){
        return addHeader(header.key, value);
    }
    public HttpResponse addHeader(String header, String value){
        headers.put(header, value);
        return this;
    }

    public String getHeader(String header){
        return headers.get(header);
    }

    public String getHeader(HttpHeader header){
        return getHeader(header.key);
    }

    public String getPayload(){
        return payload;
    }



}
