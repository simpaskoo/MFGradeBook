package net.anax.skolaOnlineScraper.util;

public class StringUtilities {
    public static boolean isInteger(String text){
        if(text == null){return false;}
        try{
            Integer.parseInt(text);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public static String repeat(String string, int a){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < a; i++){
            builder.append(string);
        }
        return builder.toString();
    }

}
