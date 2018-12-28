package com.sss.client;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.*;

/**
 * RpcFuture class
 *
 * @author Sss
 * @date 2018/12/27
 */
public class RpcFuture<T> implements Future<T> {

    private T result;
    private ChannelHandlerContext context;
    private Throwable cause;
    private CountDownLatch count = new CountDownLatch(1);

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        //若此时没有返回则没有完成
        return result != null || cause != null;
    }

    public void success(T result){
        count.countDown();
        this.result = result;

    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        count.await();
        if (cause != null){
            throw new ExecutionException(cause);
        }
        return result;
    }



    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        count.await(timeout, unit);
        if(cause != null){
            throw new ExecutionException(cause);
        }
        return result;
    }

    public void fail(Throwable error) {
        this.cause = error;
        //进程出错，计数减一 抛出异常
        count.countDown();
    }
}
