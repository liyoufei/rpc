package com.sss.service;

import com.sss.handler.ExpRequestHandler;
import com.sss.handler.FibRequestHandler;
import com.sss.server.RpcServer;

/**
 * DemoServer class
 *
 * @author Sss
 * @date 2018/12/28
 */
public class DemoServer {
    public static void main(String[] args) {
        RpcServer server = new RpcServer("localhost", 8888, 2, 16);
        server.register("fib_req", Integer.class, new FibRequestHandler()).register("exp_req", ExpInput.class,
                new ExpRequestHandler());
        server.start();
    }
}
