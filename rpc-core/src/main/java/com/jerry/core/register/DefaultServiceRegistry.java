package com.jerry.core.register;

import com.jerry.common.enumeration.RpcError;
import com.jerry.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);
    //保存服务,key为名字方便用户存取
    //接口名,实现了
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    //用来判断服务是否已注册
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();
    @Override
    public synchronized <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)){
            return;
        }
        registeredService.add(serviceName);
        //一个实现类可能是实现了多个接口,要把那些接口都注册了
        //()
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length==0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> i:interfaces){
            serviceMap.put(i.getCanonicalName(),service);
        }
        logger.info("向接口: {} 注册服务: {}", interfaces, serviceName);
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
