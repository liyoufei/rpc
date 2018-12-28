package com.sss.common;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MessageEncoder class
 *
 * @author Sss
 * @date 2018/12/26
 */

@ChannelHandler.Sharable
public class MessageEncoder extends MessageToMessageEncoder<MessageOutput> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageOutput msg, List<Object> out) throws Exception {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        writeStr(buf,msg.getType());
        writeStr(buf,msg.getRequestId());
        String payload = JSON.toJSONString(msg.getPayload());
        writeStr(buf,payload);
        out.add(buf);

    }

    private void writeStr(ByteBuf buf, String type){
        buf.writeInt(type.length());
        buf.writeBytes(type.getBytes(StandardCharsets.UTF_8));
    }
}
