package net.anax.appServerClient.client.http;

//exception when the server responded with an error
public class HttpErrorStatusException extends Exception{
    int statusCode;
    String statusMessage;

    public HttpErrorStatusException(String message, int statusCode, String statusMessage){
        super(message);
    }

}
