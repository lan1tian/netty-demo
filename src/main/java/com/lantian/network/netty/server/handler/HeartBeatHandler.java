package com.lantian.network.netty.server.handler;

import com.lantian.network.netty.Message;
import com.lantian.network.netty.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private static ConcurrentMap<String, Long> aliveChannels = new ConcurrentHashMap<String, Long>();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        if (message.getType() == MessageType.HEARTBEAT) {
            System.out.println("服务端接受到心跳");
            String id = ctx.channel().id().asLongText();
            aliveChannels.put(id, System.currentTimeMillis());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 定时检测连接活跃性
     * 如果15分钟时间都没有心跳，则断连
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            String id = ctx.channel().id().asLongText();
            Long lastActive = aliveChannels.get(id);
            if (lastActive == null || lastActive + 15 * 60 * 1000 < System.currentTimeMillis()) {
                aliveChannels.remove(id);
                ctx.disconnect();
            }
        }
    }


}
