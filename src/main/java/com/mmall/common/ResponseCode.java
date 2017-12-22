package com.mmall.common;

/**
 * this file is used to deal with the response code
 * the code will represents the description
 */
public enum ResponseCode {

    /*different code number represents the error message*/
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    /*constructor*/
    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    /*getter for getting the code and description*/
    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }

}
