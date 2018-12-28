package com.sss.common;

import java.util.UUID;

/**
 * RequestId class
 *
 * @author Sss
 * @date 2018/12/27
 */
public class RequestId {


    public static String getId(){
        return UUID.randomUUID().toString();
    }
}
