package com.lyb.nio.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author Yubo Lee
 * @create 2018-02-04 20:44
 **/
public class NioUtils {


    public static String transferBufferToString(ByteBuffer byteBuffer){
        byte [] content = new byte[byteBuffer.limit()];
        byteBuffer.get(content);
        return new String(content);
    }
}
