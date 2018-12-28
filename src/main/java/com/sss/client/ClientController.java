package com.sss.client;

import com.sss.common.MessageInput;
import com.sss.common.MessageOutput;
import com.sss.server.MessageRegistry;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * ClientController class
 *
 * @author Sss
 * @date 2018/12/27
 */
@ChannelHandler.Sharable
public class ClientController extends ChannelInboundHandlerAdapter {

    private MessageRegistry messageRegistry;
    private NewRpcClient rpcClient;
    private ChannelHandlerContext context;
    private ConcurrentMap<String,RpcFuture<?>> pendingTasks = new ConcurrentHashMap<>();

    private Throwable error = new Exception("rpc server is inactive now");

    public ClientController(NewRpcClient rpcClient, MessageRegistry messageRegistry){
        this.messageRegistry = messageRegistry;
        this.rpcClient = rpcClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.context = null;
        pendingTasks.forEach((s,future) ->
            future.fail(error)
        );
        pendingTasks.clear();
        //尝试重连
        ctx.channel().eventLoop().schedule(() -> rpcClient.reconnect(),1, TimeUnit.SECONDS);
    }

    public <T> RpcFuture<T> send(MessageOutput messageOutput){
        RpcFuture<T> future = new RpcFuture<>();

        if(context != null){
            context.channel().eventLoop().schedule(() -> {
                pendingTasks.put(messageOutput.getRequestId(),future);
                context.writeAndFlush(messageOutput);
            },1,TimeUnit.SECONDS);
        }else {
            future.fail(error);
        }

        return future;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof MessageInput)){
            return;
        }

        MessageInput input = (MessageInput) msg;
        String type = input.getType();
        Class<?> clazz = messageRegistry.getClass(type);
        if(clazz ==null){
            System.out.println("unrecognized msg type:"+type);
            return;
        }
        Object o = input.getPayload(clazz);
        @SuppressWarnings("unchecked")
        RpcFuture<Object> future = (RpcFuture<Object>) pendingTasks.remove(input.getRequestId());

        if(future == null){
            System.out.println("future not found with type : "+type);
            return;
        }
        future.success(o);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public void close() {
        if(context != null){
            context.close();
        }
    }
}
