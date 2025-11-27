package com.example;
import com.example.*;
public class Userlogin {
    private static final Userlogin instance = new Userlogin();
    private Userlogin() {}
    public static HttpResponseJson handle(String ReqBody)
    { 
        return new HttpResponseJson(200, "HTTP/1.1", null, "");//暂定
    }
}
