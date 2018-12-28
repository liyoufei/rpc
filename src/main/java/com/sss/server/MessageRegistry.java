package com.sss.server;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageRegistry class
 *  实现消息类型名与类的映射
 * @author Sss
 * @date 2018/12/26
 */
public class MessageRegistry {

    private  Map<String,Class<?>> map = new HashMap<>();

    public  void register(String type,Class<?> clazz){
        map.put(type, clazz);
    }

    /**
     * 通过type获得class
     * @param type
     * @return
     */
    public  Class<?> getClass(String type){
        return map.get(type);
    }
}
