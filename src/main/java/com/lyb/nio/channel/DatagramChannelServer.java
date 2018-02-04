package com.lyb.nio.channel;

import com.lyb.nio.util.NioUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author Yubo Lee
 * @create 2018-02-04 20:36
 **/
public class DatagramChannelServer {

    private static final Logger logger = LoggerFactory.getLogger(DatagramChannelServer.class);

    public static void main(String args[]){

        DatagramChannel datagramChannel = null;
        try {
            // 创建DatagramChannel
            datagramChannel = DatagramChannel.open();
            // 将DatagramChannel绑定到本地服务器9001端口
            datagramChannel.socket().bind(new InetSocketAddress(9001));

            // receive()方法会将接收到的数据包内容复制到指定的Buffer. 如果Buffer容不下收到的数据，多出的数据将被丢弃。
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while(true){
                datagramChannel.receive(byteBuffer);
                byteBuffer.flip();
                // 将接收到的数据打印出来
                String content = NioUtils.transferBufferToString(byteBuffer);
                logger.info("UTP服务端接收的数据包："+content);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 需要清空buffer，不然打印的一直是第一次发送的数据.
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null!=datagramChannel){
                try {
                    datagramChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
