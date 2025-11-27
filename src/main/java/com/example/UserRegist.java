package com.example;
import com.example.HttpResponseJson;
/**
 * 
 */
public class UserRegist {
    private static final UserRegist instance = new UserRegist();
    private UserRegist() {}
    public static HttpResponseJson handle(String ReqBody)
    { 
        return new HttpResponseJson(200, "HTTP/1.1", null, "");//暂定
    }
}
