package com.winshare.demo.netty.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;

public class NettyService {
    @SneakyThrows
    public static void main() {


        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 默认为CUP核数 * 2
        EventLoopGroup workerGroup = new NioEventLoopGroup();//默认为CUP核数 * 2
        EventLoopGroup noGroup = new DefaultEventLoopGroup(8);
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    //Reactor 主线程业务IO链接处理handler 可以放在这里，一般不处理netty自己处理
                    // .handler() 客户端的 添加handler在这个方法  AbstractBootstrap
                    // 处理Reactor中IO读写业务操作的handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new StringDecoder());
                            ch.pipeline().addLast("encoder",new StringEncoder());
                            ch.pipeline().addLast(noGroup,"shimpInboundHandler",new ShimpInboundHandler());
                        }
                    })
                    // 当大于boss线程最大的链接并发数时，新的链接任务放入的队列大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 给没一个链接的channel 设置参数

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(10009).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
