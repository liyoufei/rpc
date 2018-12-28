package com.sss.common;

/**
 * MessageOutput class
 *
 * @author Sss
 * @date 2018/12/26
 */
public class MessageOutput {
    private String type;
    private String requestId;
    private Object payload;

    public MessageOutput(String type, String requestId, Object payload) {
        this.type = type;
        this.requestId = requestId;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
