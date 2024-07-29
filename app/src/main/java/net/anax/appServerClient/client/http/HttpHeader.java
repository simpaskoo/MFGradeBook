package net.anax.appServerClient.client.http;

public enum HttpHeader {
    Authorization("Authorization"),
    ContentType("Content-Type"),
    ContentLength("Content-Length"),
    Accept("Accept"),
    UserAgent("User-Agent"),

    ;
    public String key;
    HttpHeader(String key){
        this.key = key;
    }

}
