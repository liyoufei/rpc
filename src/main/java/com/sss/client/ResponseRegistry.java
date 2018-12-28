package com.sss.client;

import java.util.HashMap;
import java.util.Map;

/**
 * ResponseRegistry class
 *
 * @author Sss
 * @date 2018/12/27
 */
public class ResponseRegistry {
    private  Map<String,Class<?>> map = new HashMap<>();

    public  void register(String type,Class<?> clazz){
        map.put(type,clazz);
    }

    public  Class<?> getClass(String type){
        return map.get(type);
    }
}
