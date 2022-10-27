package com.winshare.demo.netty.service;

import com.winshare.demo.netty.prot.ProtocolData;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty handler 是放在pipeline 里的一个双向链表，且handler被包装成一个 ChannelHandlerContext
 * handler 分为 ChannelInboundHandler和 ChannelOutboundHandler
 * 有pipeline固定的heard 和 tail 标识头和尾
 * IO入站从heard 开始完后走且只执行 ChannelInboundHandler的 handler，
 * IO出站是从 tail往前走  且只执行 ChannelOutboundHandler 的handler
 * ChannelHandlerContext和 netty channel 执行写 会调用链表执行handler 并 有 findContextOutbound方法判断
 * 读数据netty已经提我们执行了，并且在执行handler链时也会 调用 findContextInbound 方法 判断入站的handler
 *
 */
@ChannelHandler.Sharable
public class ShimpInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       //ctx.writeAndFlush(msg); //从当前ctx 关联的handler往后执行
      //  ctx.channel().writeAndFlush(msg);  //从handler 尾的 tail开始执行出站，执行所有的outBoundHandler
        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("--------------"+Thread.currentThread().getName());
            }
        });
        System.out.println(msg+"+++++++++++++++"+Thread.currentThread().getName());
    }


}
