package com.lyb.nio;

import java.nio.ByteBuffer;

/**
 * ${DESCRIPTION}
 *
 * @author Yubo Lee
 * @create 2018-01-28 0:03
 **/
public class ByteBufferDemo {

    public static void main(String args[]){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 'H').put((byte) 'e').put((byte) 'l').put((byte) 'l').put((byte) 'o').put((byte)'a').put((byte) 'b');
        System.out.println("position = "+byteBuffer.position());
        System.out.println("byteBuffer.hasRemaining() = "+byteBuffer.hasRemaining());
        System.out.println("byteBuffer.remaining() = "+byteBuffer.remaining());
        System.out.println("byteBuffer.limit = "+byteBuffer.limit());

        System.out.println("切换为读模式");
        byteBuffer.flip();
        byteBuffer.flip();
        // 在之前调用了put方法，他会自动将Buffer设置为写模式，如果在写完之后不进行模式切换，那它依然是写模式，
        // 在写模式下调用get方法，如果不带绝对位置的话返回position位置的值，就是个null，并且position还会加1。
        // 在写模式下，每put一次（不带绝对位置），position加1，limit值不变；
        // 切换成读模式下之后position=0；limit=当前的数据容量，remain()=还可以读取的数据数量，每次读完之后position位置加1，limit不变。
        byte [] bytes = new byte[1];
        bytes[0] = byteBuffer.get();
        System.out.println("-- "+new String(bytes));
        System.out.println("position = "+byteBuffer.position());
        System.out.println("byteBuffer.hasRemaining() = "+byteBuffer.hasRemaining());
        System.out.println("byteBuffer.remaining() = "+byteBuffer.remaining());
        System.out.println("byteBuffer.limit = "+byteBuffer.limit());

        // 再次切换为写模式
        System.out.println("再次切换为写模式");
        byteBuffer.flip();
        System.out.println("position = "+byteBuffer.position());
        System.out.println("byteBuffer.hasRemaining() = "+byteBuffer.hasRemaining());
        System.out.println("byteBuffer.remaining() = "+byteBuffer.remaining());
        System.out.println("byteBuffer.limit = "+byteBuffer.limit());
    }
}
