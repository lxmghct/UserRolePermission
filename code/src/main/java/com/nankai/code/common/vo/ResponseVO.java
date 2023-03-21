package com.nankai.code.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.nankai.code.common.enums.CodeEnum;
import lombok.Data;

//这个类是作为返回的map格式，有响应码、信号还有数据；   其中响应码和信号都可以从枚举里面去取，数据则需要自己加载
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)  //为了不显示data为null的
public class ResponseVO<T> {

    private Integer status;

    private String message;

    private T data;
    public ResponseVO() {

    }

    private ResponseVO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
    private ResponseVO(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResponseVO(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    /**
     * 成功
     */
    //成功的静态方法
    public static <T> ResponseVO<T> success(){
        return new ResponseVO<>(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVO<T> success(T data){
        return new ResponseVO<>(CodeEnum.SUCCESS.getCode(),data);
    }
    //自定义返回成功消息
    public static <T> ResponseVO<T> success(String message){
        return new ResponseVO<>(CodeEnum.SUCCESS.getCode(),message);
    }
    //自定义返回成功消息
    public static <T> ResponseVO<T> success(CodeEnum codeEnum,String message){
        return new ResponseVO<>(codeEnum.getCode(),message);
    }
    public static <T> ResponseVO<T> success(CodeEnum codeEnum, T data){
        return new ResponseVO<>(CodeEnum.SUCCESS.getCode(), data);
    }

    public static <T> ResponseVO<T> success(CodeEnum codeEnum){
        return new ResponseVO<>(codeEnum.getCode(),codeEnum.getDesc());
    }



    /**
     * 失败
     */
    //失败的静态方法
    public static <T> ResponseVO<T> error(){
        return new ResponseVO<>(CodeEnum.ERROR.getCode(),CodeEnum.ERROR.getDesc());
    }
    //自定义返回失败消息
    public static <T> ResponseVO<T> error(String message){
        return new ResponseVO<>(CodeEnum.ERROR.getCode(),message);
    }
    public static <T> ResponseVO<T> error(CodeEnum codeEnum){
        return new ResponseVO<>(codeEnum.getCode(),codeEnum.getDesc());
    }
    //自定义返回失败消息
    public static <T> ResponseVO<T> error(CodeEnum codeEnum,String message){
        return new ResponseVO<>(codeEnum.getCode(),message);
    }
    public static <T> ResponseVO<T> error(CodeEnum codeEnum, T data){
        return new ResponseVO<>(CodeEnum.SUCCESS.getCode(), data);
    }
}
