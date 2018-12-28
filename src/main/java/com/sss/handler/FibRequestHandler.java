package com.sss.handler;

import com.sss.common.MessageOutput;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * FibRequestHandler class
 *
 * @author Sss
 * @date 2018/12/26
 */
public class FibRequestHandler implements IMessageHandler<Integer> {

    private List<Long> fibs = new ArrayList<>();

    {
        //初始化前两位fib
        fibs.add(1L);
        fibs.add(1L);
    }

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, Integer n) {
        for(int i = fibs.size();i < n + 1; ++i){
            Long a = fibs.get(i-2);
            Long b = fibs.get(i-1);
            fibs.add(a + b);
        }

        ctx.writeAndFlush(new MessageOutput("fib_res",requestId,fibs.get(n)));

    }
}
