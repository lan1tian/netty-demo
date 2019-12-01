package com.lantian.network.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ProtostuffDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int size = in.readInt();
        byte[] body = new byte[size];
        in.readBytes(body);
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(body);
            ProtostuffSerialize protostuffSerialization = new ProtostuffSerialize();
            Object obj = protostuffSerialization.deserialize(byteArrayInputStream);
            out.add(obj);
        } finally {
            if (byteArrayInputStream != null)
                byteArrayInputStream.close();
        }
    }

}
