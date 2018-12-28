package com.sss.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * MessageDecoder class
 *
 * @author Sss
 * @date 2018/12/26
 */
public class MessageDecoder extends ReplayingDecoder<MessageInput> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String type = readStr(in);
        String requestId = readStr(in);
        String payload = readStr(in);
        out.add(new MessageInput(type,requestId,payload));
    }

    private String readStr(ByteBuf in) {
        //可读的int数
        int len = in.readInt();

        if(len > (1 << 20) || len < 0){
            throw new DecoderException("string too long");
        }
        byte[] bytes = new byte[len];
        //将ByteBuf中的数据存入Byte[]中
        in.readBytes(bytes);
        //返回字符串
        return new String(bytes, Charset.forName("UTF-8"));
    }
}
