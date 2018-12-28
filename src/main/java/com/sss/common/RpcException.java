package com.sss.common;

/**
 * RpcException class
 *
 * @author Sss
 * @date 2018/12/27
 */
public class RpcException extends RuntimeException{

    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
