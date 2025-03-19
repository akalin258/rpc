package com.jerry.testServer;


import com.jerry.api.AnimalService;
import com.jerry.api.HelloService;
import com.jerry.core.RpcServer;
import com.jerry.core.netty.server.NettyServer;
import com.jerry.core.registry.DefaultServiceRegistry;
import com.jerry.core.registry.ServiceRegistry;

public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        AnimalService animalService=new AnimalServiceImpl();

        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        serviceRegistry.register(animalService);

        RpcServer server = new NettyServer();
        server.start(9000);


    }

}