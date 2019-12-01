package com.lantian.network.netty.client.handler;

import com.lantian.network.netty.Message;
import com.lantian.network.netty.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

    public static final ConcurrentMap<String, Channel> CHANNEL_HOLDER = new ConcurrentHashMap<>();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            System.out.println("客户端发送心跳");
            Message message = new Message();
            message.setType(MessageType.HEARTBEAT);
            ctx.channel().writeAndFlush(message);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String id = ctx.channel().id().asLongText();
        CHANNEL_HOLDER.put(id, ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        if (message.getType() == MessageType.ECHO) {
            System.out.println(message.getEcho());
        }
        ReferenceCountUtil.release(msg);
    }

}
