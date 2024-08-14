package net.anax.appServerClient.client.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class StringUtilities {
    public static boolean isInteger(String string){
        if(string == null){return false;}
        try{
            Integer.parseInt(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    public static boolean isLong(String string){
        if(string == null){return false;}
        try{
            Long.parseLong(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isBoolean(String bool){
        if(bool == null){return false;}
        if(bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("false")){return true;}
        return false;
    }

    public static String[] chopUp(int chunkSize, String string){
        try {
            ArrayList<String> list = new ArrayList<>();
            StringReader reader = new StringReader(string);
            StringBuilder builder = new StringBuilder();
            loop: while (true) {
                for(int i = 0; i < chunkSize; i++){
                    int chr = reader.read();
                    if(chr == -1){
                        if(builder.length() != 0){
                            list.add(builder.toString());
                        }
                        break loop;
                    }
                    builder.append((char) chr);
                }
                list.add(builder.toString());
                builder.setLength(0);
            }

            return list.toArray(new String[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
