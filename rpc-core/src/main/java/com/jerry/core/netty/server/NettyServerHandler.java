package com.jerry.core.netty.server;

import com.jerry.common.entity.RpcRequest;
import com.jerry.common.entity.RpcResponse;
import com.jerry.core.registry.DefaultServiceRegistry;
import com.jerry.core.registry.ServiceRegistry;
import com.jerry.core.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static {
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = serviceRegistry.getService(interfaceName);
        Object data = requestHandler.handle(rpcRequest, service);
        logger.info("服务端发送响应类型: {}", RpcResponse.success(data).getClass()); // 添加此行
        channelHandlerContext.writeAndFlush(RpcResponse.success(data));
    }
}
