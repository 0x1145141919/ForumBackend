package com.example;
// 核心Java包
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;

// HTTP服务器包
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import java.io.OutputStream;

import com.example.UserRegist;
import com.example.HttpResponseJson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
public class BackEndMainServer {
    
    public static void main(String[] args) {
          try {
            HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
            server.createContext("/Backend-1/api/auth/register", new UserRegistHandler());
            server.createContext("/Backend-1/api/auth/login", new UserloginHandler());
            server.start(); 
            Thread.currentThread().join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
class UserRegistHandler implements HttpHandler {
    @Override 
    public void handle(HttpExchange httpExchange) throws IOException { 
        String requestMethod = httpExchange.getRequestMethod();
        
        // 处理非POST请求
        if (!"POST".equals(requestMethod)) {
            sendErrorResponse(httpExchange, 405, "Method Not Allowed");
            return;
        }
        
        try {
            // 读取请求体（如果需要处理注册数据）
            InputStream requestBody = httpExchange.getRequestBody();
            String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            HttpResponseJson response = UserRegist.handle(requestBodyString);
            httpExchange.sendResponseHeaders(response.statusCode, response.responseBody.length());
            // 写入响应体
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.responseBody.getBytes(StandardCharsets.UTF_8));
                os.flush(); // 确保数据被刷新
            }

            
        } catch (Exception e) {

            sendErrorResponse(httpExchange, 500, "Internal Server Error");
        }
       
    }
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        String errorJson = String.format("{\"code\":%d,\"message\":\"%s\",\"data\":null}", 
                                       statusCode, message);
        byte[] errorBytes = errorJson.getBytes(StandardCharsets.UTF_8);
        
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        
        exchange.sendResponseHeaders(statusCode, errorBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(errorBytes);
            os.flush();
        }
    }
        
}
class UserloginHandler implements HttpHandler {
    @Override 
    public void handle(HttpExchange httpExchange) throws IOException { 
        String requestMethod = httpExchange.getRequestMethod();
        // 处理非POST请求
        if (!"POST".equals(requestMethod)) {
            sendErrorResponse(httpExchange, 405, "Method Not Allowed");
            return;
        }
        
        try {
            // 读取请求体（如果需要处理注册数据）
            InputStream requestBody = httpExchange.getRequestBody();
            String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            HttpResponseJson response = Userlogin.handle(requestBodyString);
            httpExchange.sendResponseHeaders(response.statusCode, response.responseBody.length());
            // 写入响应体
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.responseBody.getBytes(StandardCharsets.UTF_8));
                os.flush(); // 确保数据被刷新
            }

            
        } catch (Exception e) {

            sendErrorResponse(httpExchange, 500, "Internal Server Error");
        }
       
    }
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        String errorJson = String.format("{\"code\":%d,\"message\":\"%s\",\"data\":null}", 
                                       statusCode, message);
        byte[] errorBytes = errorJson.getBytes(StandardCharsets.UTF_8);
        
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        
        exchange.sendResponseHeaders(statusCode, errorBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(errorBytes);
            os.flush();
        }
    }
        
}