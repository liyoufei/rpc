package com.sss.server;

import com.sss.common.MessageDecoder;
import com.sss.common.MessageEncoder;
import com.sss.handler.IMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;


/**
 * RpcServer class
 *
 * @author Sss
 * @date 2018/12/27
 */
public class RpcServer {

    private String host;
    private int port;
    private int workerThreads;
    private int ioThreads;
    private MessageHanlderRegistry hanlderRegistry = new MessageHanlderRegistry();
    private MessageRegistry messageRegistry = new MessageRegistry();

    public RpcServer(String host,int port,int workerThreads,int ioThreads){
        this.host = host;
        this.port = port;
        this.ioThreads = ioThreads;
        this.workerThreads = workerThreads;
    }

    /**
     * 快捷方式注册，类型对应相应的消息类以及消息处理类
     * @param type
     * @param reqClass
     * @param handler
     * @return
     */
    public RpcServer register(String type, Class<?> reqClass, IMessageHandler<?> handler){
        MessageHanlderRegistry.register(type,handler);
        messageRegistry.register(type,reqClass);
        return this;
    }

    private ServerBootstrap bootstrap;
    private ServerController messageController;
    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;
    private Channel serverChannel;


    public void start(){
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(ioThreads);
        workerGroup = new NioEventLoopGroup(ioThreads);
        messageController = new ServerController(workerThreads,hanlderRegistry,messageRegistry);

        bootstrap.group(bossGroup,workerGroup);

        bootstrap.channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ReadTimeoutHandler(30));
                        pipeline.addLast(new MessageDecoder());
                        pipeline.addLast(new MessageEncoder());
                        pipeline.addLast(messageController);

                    }
                });
        bootstrap.option(ChannelOption.SO_BACKLOG,100)
                 .option(ChannelOption.SO_REUSEADDR,true)
                 .option(ChannelOption.TCP_NODELAY,true)
                 .childOption(ChannelOption.SO_KEEPALIVE,true);

        serverChannel = bootstrap.bind(host,port).channel();

    }

    public void stop(){
        serverChannel.close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        messageController.shutdownGracefully();
    }

}
