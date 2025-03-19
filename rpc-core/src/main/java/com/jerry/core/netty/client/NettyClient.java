package com.jerry.core.netty.client;

import com.jerry.common.entity.RpcRequest;
import com.jerry.common.entity.RpcResponse;
import com.jerry.core.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.AttributeKey;

public class NettyClient implements RpcClient {

    private String host;
    private int port;
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try{
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 服务端/客户端 Pipeline 配置
                            pipeline.addLast(new ObjectEncoder());         // 编码：对象 → ByteBuf
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null))); // 解码：ByteBuf → 对象
                            pipeline.addLast(new NettyClientHandler());

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcRequest);//发完之后立刻返回
            channel.closeFuture().sync();//这一步是干什么的
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();
            return rpcResponse.getData();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
