package com.jerry.core;

import com.jerry.common.entity.RpcRequest;
import com.jerry.common.entity.RpcResponse;
import com.jerry.core.netty.client.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    //代理类处理网络通信
    //为啥参数不是T,因为客户端那里没有实现类,也就是没有T
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args) //参数别传错了
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new NettyClient();
        return rpcClient.sendRequest(rpcRequest);
    }
}
