package com.sss.common;

import com.alibaba.fastjson.JSON;

/**
 * MessageInput class
 *
 * @author Sss
 * @date 2018/12/26
 */
public class MessageInput {
    private String type;
    private String requestId;
    private String payload;

    public MessageInput(String type, String requestId, String payload) {
        this.type = type;
        this.requestId = requestId;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public String getRequestId() {
        return requestId;
    }

    public <T> T getPayload(Class<T> clazz) {
        if (clazz == null){
            return null;
        }
        //直接反序列化为对象取出
        return JSON.parseObject(payload,clazz);
    }
}
