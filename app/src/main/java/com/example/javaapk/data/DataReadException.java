package com.example.javaapk.data;

public class DataReadException extends Exception{
    public DataReadExceptionType type;
    public DataReadException(String message, DataReadExceptionType type){
        super(message);
        this.type = type;
    }

    public static enum DataReadExceptionType{
        DataNotPresent,
        InvalidData,
        InvalidFormat
    }

}
