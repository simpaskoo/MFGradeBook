package com.example.javaapk.util;

import net.anax.appServerClient.client.util.ExceptionUtilities;

import org.json.simple.JSONObject;

public class JsonUtilities {
    public static <E extends Exception> String extractString(JSONObject data, String stringName, E e) throws E {
        if(!data.containsKey(stringName)){throw ExceptionUtilities.throwAgain(e);}
        if(data.get(stringName) instanceof String){return (String) data.get(stringName);}
        throw ExceptionUtilities.throwAgain(e);
    }
}
