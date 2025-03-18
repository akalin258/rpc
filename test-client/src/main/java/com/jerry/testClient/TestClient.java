package com.jerry.testClient;

import com.jerry.api.AnimalService;
import com.jerry.api.HelloObject;
import com.jerry.api.HelloService;
import com.jerry.core.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        AnimalService animalService=proxy.getProxy(AnimalService.class);
        System.out.println(animalService.sayName());
        /*RpcClientProxy proxy1 = new RpcClientProxy("127.0.0.1", 9001);
        HelloService helloService1=proxy1.getProxy(HelloService.class);
        System.out.println(helloService1.hello(object));*/
    }
}
