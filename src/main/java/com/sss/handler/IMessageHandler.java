package com.sss.handler;

import io.netty.channel.ChannelHandlerContext;

public interface  IMessageHandler<T> {
    /**
     * 完成数据的处理
     * @param ctx ctx
     * @param requestId 请求ID
     * @param object 用于处理的对象
     */
    void handle (ChannelHandlerContext ctx,String requestId,T object);
}
