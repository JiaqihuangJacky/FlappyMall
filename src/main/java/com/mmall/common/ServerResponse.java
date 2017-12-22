package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * This file is used to deal with teh server response
 */
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL) //reject all the null object
//make sure that when doing the serialization of json
//if we encouter the null object, the key will disappear
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    /*constructor*/
    private ServerResponse(int status){
        this.status = status;
    }
    /*constructor*/
    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    /*constructor*/
    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    /*constructor*/
    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    /*use it as json serialization, it will return the success message*/
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    /*getter for get the status, data and message*/
    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    /*call the constructor from the ResponseCode class, then, get the success code*/
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    /*call the constructor from the ResponseCode class, then, get the success code and message*/
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    /*call the constructor from the ResponseCode class, then, get the success code and data*/
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    /*call the constructor from the ResponseCode class, then, get the success code, message and data*/
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }


    /*call the constructor from the ResponseCode class, then, get the error code, message description*/
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }


    /*call the constructor from the ResponseCode class, then, get the error code, message description*/
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    /*call the constructor from the ResponseCode class, then, get the error code, message string*/
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }













}
