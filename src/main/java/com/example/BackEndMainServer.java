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
    
    public static void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
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
class UserRegistHandler implements HttpHandler {
    @Override 
    public void handle(HttpExchange httpExchange) throws IOException { 
        String requestMethod = httpExchange.getRequestMethod();
        
        // 处理非POST请求
        if (!"POST".equals(requestMethod)) {
            BackEndMainServer.sendErrorResponse(httpExchange, 405, "Method Not Allowed");
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

            BackEndMainServer.sendErrorResponse(httpExchange, 500, "Internal Server Error");
        }
       
    }
        
}
class UserloginHandler implements HttpHandler {
    @Override 
    public void handle(HttpExchange httpExchange) throws IOException { 
        String requestMethod = httpExchange.getRequestMethod();
        // 处理非POST请求
        if (!"POST".equals(requestMethod)) {
            BackEndMainServer.sendErrorResponse(httpExchange, 405, "Method Not Allowed");
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

            BackEndMainServer.sendErrorResponse(httpExchange, 500, "Internal Server Error");
        }
       
    }
        
}
/**
 * 四级树形结构：
Level 1 - 模块

根节点/最大容器：moduleID

    描述：整个系统被划分为不同的模块或版块。所有内容都归属于某个特定的模块。这是最顶层的容器。

Level 2 - 主帖

父节点：moduleID
当前节点：PostID

    描述：在某个模块 (moduleID) 下，用户可以创建主帖 (PostID)。一个模块下可以有无数个主帖。

Level 3 - 一级评论

父节点：PostID
当前节点：CommentID

    描述：用户可以直接回复主帖，这些回复被称为一级评论 (CommentID)。一个主帖下可以有无数个一级评论。

Level 4 - 二级评论

父节点：CommentID
当前节点：Commentlv2ID
描述：用户不仅可以回复主帖，还可以回复其他人的评论，这种对评论的回复被称为二级评论 (Commentlv2ID)。一个一级评论下可以有无数个二级评论。
通过这四个id再加上一个枚举值就确定了一个资源的位置和类型。
 */
/**
 * 改变状态：(post)
请求路径：/api/auth/toggle
方法名不一定是POST,要根据方法名来路由
一个方法若要使用n个参数则需要从param1开始向后使用到paramn,(n<=6)
对于其它不使用的参数则强制为0，不合法则会返回参数错误
下面来设计方法
LIKE “点赞方法”，
DISLIKE “点踩方法”，
COLLECT “收藏方法”，
DISCOLLECT “取消收藏方法”，
FOLLOW “关注方法”，
UNFOLLOW “取消关注方法”，
DISRESOURCE “删除资源方法”，
{    
	"userId":uint64_t,//当前用户id
  	"Resoure":{
        "moduleID":uint32_t,//资源所属模块id
        "PostID":uint32_t//帖子id
        "CommentID":uint32_t//评论帖id
        "Commentlv2ID":uint32_t//二级评论id
        “Type”:uint8_t//资源类型（0-模块，1-主帖，2-一级评论，3-二级评论）
    }
}
接收：
{
	"code":number,//200表示成功
	"message":string,//"错误信息"
	"data":null
}

 */
class UsertoggleHandler implements HttpHandler {
    @Override 
    public void handle(HttpExchange httpExchange) throws IOException { 
        String requestMethod = httpExchange.getRequestMethod();
        // 处理非POST请求
        if (!"POST".equals(requestMethod)) {
            BackEndMainServer.sendErrorResponse(httpExchange, 405, "Method Not Allowed");
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

            BackEndMainServer.sendErrorResponse(httpExchange, 500, "Internal Server Error");
        }
       
    }
        
}