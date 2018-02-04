package com.lyb.nio.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author Yubo Lee
 * @create 2018-02-04 20:54
 **/
public class DatagramChannelClient {

    private static final Logger logger = LoggerFactory.getLogger(DatagramChannelClient.class);

    public static void main(String args[]){
        // 发送时指定host和port
//        sendUdpMessage();

        // 先建立连接再发送
        sendUdpMessageOnConnect();
    }


    public static void sendUdpMessageOnConnect(){
        DatagramChannel datagramChannel = null;
        try {
            // 创建DatagramChannel
            datagramChannel = DatagramChannel.open();
            // 先建立连接（伪连接），因为UDP是没有连接的，这里的连接到特定地址并不会像TCP通道那样创建一个真正的连接。而是锁住DatagramChannel ，让其只能从特定地址收发数据。
            datagramChannel.connect(new InetSocketAddress("127.0.0.1",9001));

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            String contentOne = "Hi UDP Server, I am client one, nice to meet you. Time = "+System.currentTimeMillis();
            byteBuffer.put(contentOne.getBytes());
            byteBuffer.flip();
            // 在发送时指定主机名和端口号，
            int result = datagramChannel.write(byteBuffer);
            logger.info("第一次发送，send result = "+result);

            byteBuffer.clear();
            String contentTwo = "Hi UDP Server, I am client two, nice to meet you. Time = "+System.currentTimeMillis();
            byteBuffer.put(contentTwo.getBytes());
            byteBuffer.flip();
            // 在发送时指定主机名和端口号，
            result = datagramChannel.write(byteBuffer);
            logger.info("第二次发送，send result = "+result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null!=datagramChannel){
                    datagramChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendUdpMessage(){
        DatagramChannel datagramChannel = null;
        try {
            // 创建DatagramChannel
            datagramChannel = DatagramChannel.open();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            String content = "Hi UDP Server sgax, I am client, nice to meet you. Time = "+System.currentTimeMillis();
            byteBuffer.put(content.getBytes());
            byteBuffer.flip();
            // 在发送时指定主机名和端口号，
            int result = datagramChannel.send(byteBuffer,new InetSocketAddress("127.0.0.1",9001));
            logger.info("send result = "+result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null!=datagramChannel){
                    datagramChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
