package com.winshare.demo.netty.client;

import com.winshare.demo.netty.service.ShimpInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DemoClient {
    public  void main() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new StringDecoder());
                            ch.pipeline().addLast("encoder",new StringEncoder());
                            ch.pipeline().addLast("shimpInboundHandler",new SimpleChatClientInitializer());
                        }
                    });
            ChannelFuture channelF = bootstrap.connect("127.0.0.1", 10009).sync();
            Channel channel = channelF.channel();
            /*ExecutorService executorService = Executors.newFixedThreadPool(100);
            for (int i = 0; i < 100; i++) {
                *//*executorService.execute(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        channel.writeAndFlush("我的测试"+Thread.currentThread().getName());
                        System.out.println("--------------"+Thread.currentThread().getName());

                    }
                });*//*
                channel.writeAndFlush("我的测试"+i+Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
            }*/
            //channel.writeAndFlush("我的测试1"+Thread.currentThread().getName());
            //channel.writeAndFlush("我的测试2"+Thread.currentThread().getName());
            channel.writeAndFlush("我的测试"+Thread.currentThread().getName());
            channelF.channel().close().sync();
        } catch (Exception e) {
            e.printStackTrace();
            group.shutdownGracefully();
        } finally {
        }
    }
}
