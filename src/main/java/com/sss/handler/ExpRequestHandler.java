package com.sss.handler;

import com.sss.service.ExpInput;
import com.sss.service.ExpOutput;
import com.sss.common.MessageOutput;
import io.netty.channel.ChannelHandlerContext;

/**
 * ExpRequestHandler class
 *
 * @author Sss
 * @date 2018/12/26
 */
public class ExpRequestHandler implements IMessageHandler<ExpInput> {

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, ExpInput expInput) {
        Long result = 1L;
        long start = System.currentTimeMillis();
        for (int i = 0; i < expInput.getExp(); i++) {
            result = result * expInput.getBase();
        }
        long end = System.currentTimeMillis();

        ctx.writeAndFlush(new MessageOutput("exp_res",requestId,new ExpOutput(result,end - start)));


    }
}
