package com.sss.client;

import com.sss.common.*;
import com.sss.server.MessageRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;


/**
 * NewRpcClient class
 *
 * @author Sss
 * @date 2018/12/27
 */
public class NewRpcClient {

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private ClientController controller;
    private String ip;
    private int port;
    private boolean started;
    private boolean stoped;

    private MessageRegistry messageRegistry = new MessageRegistry();

    public NewRpcClient(String ip,int port){
        this.ip = ip;
        this.port = port;
        this.init();
    }

    private void init() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);
        controller = new ClientController(this,messageRegistry);
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ReadTimeoutHandler(60));
                pipeline.addLast(new MessageEncoder());
                pipeline.addLast(new MessageDecoder());
                pipeline.addLast(controller);
            }
        });

        bootstrap.option(ChannelOption.TCP_NODELAY,true).option(ChannelOption.SO_KEEPALIVE,true);

    }


    public NewRpcClient register(String type,Class<?> reqClass){
        messageRegistry.register(type, reqClass);
        return this;
    }

    public <T> RpcFuture<T> sendAsync(String type, Object payload){
        if(!started){
            connect();
        }

        String requestId = RequestId.getId();
        MessageOutput output = new MessageOutput(type,requestId,payload);
        return controller.send(output);
    }

    public <T> T send(String type,Object payload){
        RpcFuture<T> future = sendAsync(type, payload);
        try{
            return future.get();
        }catch(Exception e){
            throw new RpcException();
        }

    }

    public void connect(){
        bootstrap.connect(ip,port).syncUninterruptibly();
        started = true;
    }

    public void reconnect(){
        if(stoped){
            return;
        }

        bootstrap.connect(ip,port).addListener(future -> {
            if(future.isSuccess()){
                return;
            }
            if (!stoped){
                group.schedule(() ->
                        reconnect(),1, TimeUnit.SECONDS);
            }
        });

    }

    public void close(){
        stoped = true;
        controller.close();
        group.shutdownGracefully();
    }
}
