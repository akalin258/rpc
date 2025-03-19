package com.jerry.testClient;

import com.jerry.api.AnimalService;
import com.jerry.api.HelloObject;
import com.jerry.api.HelloService;
import com.jerry.core.RpcClientProxy;
import com.jerry.core.netty.client.NettyClient;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);

        HelloService helloService=proxy.getProxy(HelloService.class);
        HelloObject obj = new HelloObject(258, "akalin");
        String hello = helloService.hello(obj);
        System.out.println(hello);
        AnimalService animalService=proxy.getProxy(AnimalService.class);
        String s = animalService.sayName();
        System.out.println(s);

    }
}
