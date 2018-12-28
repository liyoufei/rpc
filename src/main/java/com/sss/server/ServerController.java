package com.sss.server;

import com.sss.common.MessageInput;
import com.sss.handler.IMessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClientController class
 *
 * @author Sss
 * @date 2018/12/27
 */

@ChannelHandler.Sharable
public class ServerController extends ChannelInboundHandlerAdapter {
    /**
     * 业务线程池
     */
    private ThreadPoolExecutor poolExecutor;
    private MessageRegistry messageRegistry ;
    private MessageHanlderRegistry hanlderRegistry;

    public ServerController(int threads,MessageHanlderRegistry hanlderRegistry,MessageRegistry messageRegistry){

        this.messageRegistry = messageRegistry;
        this.hanlderRegistry = hanlderRegistry;
        BlockingQueue<Runnable> blockingDeque = new ArrayBlockingQueue<>(1000);

        ThreadFactory threadFactory = new ThreadFactory() {

            AtomicInteger seq = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("thread["+seq+"]");
                return t;
            }
        };

        this.poolExecutor = new ThreadPoolExecutor(1,threads,30,TimeUnit.MINUTES,blockingDeque,threadFactory);

    }

    public void shutdownGracefully(){

        this.poolExecutor.shutdown();
        try{
            this.poolExecutor.awaitTermination(10,TimeUnit.SECONDS);
        }catch(Exception e){
            e.printStackTrace();
        }
        this.poolExecutor.shutdownNow();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection established");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection terminated");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof MessageInput){
            System.out.println("read a message");

            this.poolExecutor.execute(() -> {
                handleMessage(ctx,(MessageInput) msg);
            });
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, MessageInput input) {

        //通过消息类型注册中心获得相应的类型
        Class<?> clazz = messageRegistry.getClass(input.getType());

        Object o = input.getPayload(clazz);

        //同种类型对应消息类型和处理器类型
        @SuppressWarnings("unchecked")
        IMessageHandler<Object> handler = (IMessageHandler<Object>) MessageHanlderRegistry.getHandler(input.getType());

        if(handler != null){
            handler.handle(ctx,input.getRequestId(),o);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("connection error");
        cause.printStackTrace();
        ctx.close();
    }
}
