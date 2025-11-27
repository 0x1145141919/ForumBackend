package com.example;
import com.sun.net.httpserver.Headers;
public class HttpResponseJson {
    public int statusCode;
    public String httpVersion;
    public Headers responseHeaders;
    public String responseBody;
    public HttpResponseJson(int statusCode, String httpVersion, Headers responseHeaders, String responseBody)
    { 
        this.statusCode = statusCode;
        this.httpVersion = httpVersion;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }
}
