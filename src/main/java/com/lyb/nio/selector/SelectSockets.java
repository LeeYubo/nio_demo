package com.lyb.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Yubo Lee
 * @create 2018-03-10 22:21
 **/
public class SelectSockets {

    private static Integer port  = 1234;
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public static void main(String args[]){

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            Selector selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true){
                int n = selector.select();
                if(n<=0) continue;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterable = selectionKeys.iterator();
                while(selectionKeyIterable.hasNext()){
                    SelectionKey selectionKey = selectionKeyIterable.next();
                    ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);

                    byteBuffer.clear();
                    byteBuffer.put(("Hi, I am PangBo Lee, Nice to meet you. Now is "+System.currentTimeMillis()).getBytes());
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
