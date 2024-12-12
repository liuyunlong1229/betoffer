package com.test.lyl.test;

import com.sun.net.httpserver.HttpServer;
import com.test.lyl.test.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {


    public static void main(String[] args) throws IOException {



        // 创建HttpServer实例并绑定到端口8001
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);



        ChainHandler handler=new ChainHandler();
        handler.addHandler(new RootHandler());
        handler.addHandler(new LoginHandler());
        handler.addHandler(new StakeHandler());
        handler.addHandler(new HighstakesHandler());


        server.createContext("/",handler) ;



        // 启动服务器
        server.start();

        System.out.println("Server started on port 8001. Listening for requests...");
    }









}
