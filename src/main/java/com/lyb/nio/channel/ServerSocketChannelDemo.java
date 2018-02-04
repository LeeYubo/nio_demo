package com.lyb.nio.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author Yubo Lee
 * @create 2018-02-04 17:22
 **/
public class ServerSocketChannelDemo {

    private static final Logger logger = LoggerFactory.getLogger(ServerSocketChannelDemo.class);

    public static void main(String args[]){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9000));
            logger.info("=准备接受新的连接...");
            SocketChannel socketChannel = serverSocketChannel.accept();
            logger.info("新创建的连接，socketChannel = "+socketChannel);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            byte [] content = new byte[byteBuffer.limit()];
            byteBuffer.get(content);
            logger.info("从客户端读取的数据，content = "+new String(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
