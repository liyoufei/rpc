package com.sss.server;

import com.sss.handler.IMessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageHanlderRegistry class
 *  消息处理器注册
 * @author Sss
 * @date 2018/12/26
 */
public class MessageHanlderRegistry {
    private static Map<String, IMessageHandler<?>> map = new HashMap<>();

    public static void register(String type,IMessageHandler<?> iMessageHandler){
        map.put(type,iMessageHandler);
    }

    public static IMessageHandler<?> getHandler(String type){
        return map.get(type);
    }
}
