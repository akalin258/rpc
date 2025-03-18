package com.jerry.core.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.jerry.common.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RpcClient {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    //发送请求
    /*public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            //把request格式放进去
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时有错误发生：", e);
            return null;
        }

    }*/

    //手写RpcClient的发送请求
    public Object sendRequest(RpcRequest rpcRequest,String host,int port){
        try(Socket socket = new Socket(host, port)) {
            //客户端往外输出,把请求放到流上(把对象序列化成字节)
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //等到服务端响应之后,就可用输入流把字节反序列化成对象
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            //强制将缓冲区数据写入底层流（如 Socket），避免数据滞留。
            //在发送关键数据后调用 flush() 是良好的编程习惯。
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时有错误发生：", e);
            return null;
        }
    }
}

