package com.lyb.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Yubo Lee
 * @create 2018-02-04 16:36
 **/
public class SocketChannelDemo {

    public static void main(String args[]){
        SocketChannel socketChannel = null;
        try {
            // 打开一个Channel
            socketChannel = SocketChannel.open();
            // 将Channel绑定到本地的9000端口上
            socketChannel.connect(new InetSocketAddress("127.0.0.1",9000));

            // 向SocketChannel中写入数据，因为Channel只能与Buffer进行数据交互，所以，先将数据放入Buffer，再从Buffer写入到SocketChannel中。
            String content = "Hello, This is me first time to talk to you. Time = "+System.currentTimeMillis();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(content.getBytes());
            byteBuffer.flip();
            // Write()方法无法保证能写多少字节到SocketChannel。所以，我们重复调用write()直到Buffer没有要写的字节为止。
            while(byteBuffer.hasRemaining()){
                socketChannel.write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null!=socketChannel){
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
