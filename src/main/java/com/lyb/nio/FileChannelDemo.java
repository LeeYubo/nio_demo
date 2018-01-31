package com.lyb.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Yubo Lee
 * @create 2018-01-30 23:35
 **/
public class FileChannelDemo {

    private static final Logger logger = LoggerFactory.getLogger(FileChannelDemo.class);

    public static void main(String args[]){

        FileChannel fileChannel1 = null;

        String sourceFilePath = "E:\\Temp\\test.txt";
        try {
            fileChannel1 = new FileInputStream(new File(sourceFilePath)).getChannel();
            // 分配一个大小为1024字节的Buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 从FileChannel中读取数据到Buffer
            int length = fileChannel1.read(byteBuffer);
            logger.info("读取数据长度："+length);
            byte [] content = new byte[length];
            byteBuffer.flip();
            byteBuffer.get(content);
            logger.info("读取内容："+new String(content));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null!=fileChannel1){
                try {
                    fileChannel1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static void testInputStreamChannel(){
            FileChannel fileChannel2 = null;
            FileChannel fileChannel3 = null;
            String targetFilePath = "";
            try {
                fileChannel2 = new FileOutputStream(new File(targetFilePath)).getChannel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String randomFilePath = "";
                try {
                fileChannel3 = new RandomAccessFile(randomFilePath,"rw").getChannel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }
}
