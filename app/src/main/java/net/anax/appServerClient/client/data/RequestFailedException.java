package net.anax.appServerClient.client.data;


//exception when the server could not be reached
public class RequestFailedException extends Exception{
    public static boolean doPrintStackTrace = false;
    public RequestFailedException(String message){
        this(message, null);
    }
    public RequestFailedException(String message, Exception e){
        super(message);
        this.initCause(e);
        if(doPrintStackTrace){
            this.printStackTrace();
            if(e != null){
                e.printStackTrace();
            }
        }
    }
}
