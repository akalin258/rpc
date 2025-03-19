package com.jerry.core;

import com.jerry.common.entity.RpcRequest;
import com.jerry.common.entity.RpcResponse;
import com.jerry.common.enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//真正去执行实现类的方法
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    //在这里面又要交给别的方法执行反射
    public Object handle(RpcRequest rpcRequest,Object service){
        Object result = null;
        try {
            result = invokeTargetMethod(rpcRequest, service);
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        } return result;
    }
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws IllegalAccessException, InvocationTargetException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {

            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD);
        }
        //找到哪个方法之后,传入实现类,方法参数
        return method.invoke(service, rpcRequest.getParameters());
    }
}
