package com.jerry.core.netty.server;

import com.jerry.core.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    @Override
    public void start(int port) {
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 服务端/客户端 Pipeline 配置
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null))); // 解码：ByteBuf → 对象

                            pipeline.addLast(new ObjectEncoder());         // 编码：对象 → ByteBuf

                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(9000).sync();
            channelFuture.channel().closeFuture().sync();  // 阻塞直到服务器通道关闭
        }catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
