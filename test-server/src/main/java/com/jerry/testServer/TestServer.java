package com.jerry.testServer;

import com.jerry.api.AnimalService;
import com.jerry.api.HelloService;
import com.jerry.core.register.DefaultServiceRegistry;
import com.jerry.core.register.ServiceRegistry;
import com.jerry.core.server.RpcServer;

public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        AnimalService animalService=new AnimalServiceImpl();
        //RpcServer rpcServer = new RpcServer();
        //rpcServer.register(helloService, 9000);//实现类,端口
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        serviceRegistry.register(animalService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);//监听端口就行

    }

}