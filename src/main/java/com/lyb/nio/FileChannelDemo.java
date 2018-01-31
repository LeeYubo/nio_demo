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
        String sourceFilePath = "E:\\Temp\\test.txt";
//        readFromFileChannel(sourceFilePath);

        String targetFilePath = "E:\\Temp\\test2.txt";
//        writeToFileChannel(targetFilePath);

        String targetFilePath2 = "E:\\Temp\\test3.txt";
        testPositionOfFileChannel(targetFilePath2);
    }


    public static void testPositionOfFileChannel(String targetFilePath){
        File file = new File(targetFilePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("an exception occurred on creating file {}.",targetFilePath);
            }
        }
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileOutputStream(new File(targetFilePath)).getChannel();
            String content = "New String to write to file, I am learning nio, but i have a lot of questions. i need a long time to understand them."+System.currentTimeMillis();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(content.getBytes());
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()){
                fileChannel.write(byteBuffer); // 将Buffer中的数据写入到FileChannel
            }
            long position = fileChannel.position();
            long size = fileChannel.size();
            logger.debug("position = "+position);
            logger.debug("size = "+size);

            // 清空Buffer，供再次使用；
            byteBuffer.flip();
            byteBuffer.clear();
            fileChannel.read(byteBuffer);
            logger.info("size -position = "+(fileChannel.size()-fileChannel.position()));
            byte [] byteArray = new byte[(int)(fileChannel.size()-fileChannel.position())];
            byteBuffer.put(byteArray);
            logger.info("根据position读取的内容："+new String(byteArray));

        } catch (FileNotFoundException e) {
            logger.error("fine {} not found.",targetFilePath,e);
        } catch (IOException e) {
            logger.error("an exception occurred on writing to file channel.", e);
        } finally {
            try {
                if(null!=fileChannel){
                    fileChannel.close();
                }
            } catch (IOException e) {
                logger.error("an exception occurred on closing file channel.", e);
            }
        }
    }


    /**
     * 将制定内容通过FileChannel写入到文件中
     *
     * @param targetFilePath
     */
    public static void writeToFileChannel(String targetFilePath){
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileOutputStream(new File(targetFilePath)).getChannel();
            String content = "New String to write to file, I am learning nio, but i have a lot of questions. i need a long time to understand them."+System.currentTimeMillis();

            // ByteBuffer在创建的时候被设置大小为1024，如果要写入到buffer中的内容大于1024，将会抛出异常，超出了buffer的capacity
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(content.getBytes());

            // 放入完成之后将buffer的模式切换为读模式
            byteBuffer.flip();
            // 因为channel不一定是一次性从buffer将数据全部数据读入到channel中，有可能是分多次，所以如果想保证将buffer中的数据全部读出，最好使用while的形式，多次判断，
            // 每次从buffer中读取数据之后他的remaining值会更新，所以使用hasRemaining可以判断buffer中是否还有数据剩余，如果有将持续调用channel的write方法，从buffer读入再写入到channel。
            while (byteBuffer.hasRemaining()){
                fileChannel.write(byteBuffer); // 将Buffer中的数据写入到FileChannel
            }
            // 查看fileChannel对应的文件的大小
            System.out.println("file size = "+fileChannel.size());

            // 从fileChannel对应的文件中截取一部分
            fileChannel.truncate(100);
            System.out.println("file size = "+fileChannel.size());

            // 出于性能方面的考虑，操作系统会将数据缓存在内存中，所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，需要调用force()方法。
            // 所以之前的truncate方法，为了保证截取的文件内容能正常同步到文件上，最好使用force方法。
            // force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。
            fileChannel.force(true);
        } catch (FileNotFoundException e) {
            logger.error("fine {} not found.",targetFilePath,e);
        } catch (IOException e) {
            logger.error("an exception occurred on writing to file channel.", e);
        } finally {
            try {
                if(null!=fileChannel){
                    fileChannel.close();
                }
            } catch (IOException e) {
                logger.error("an exception occurred on closing file channel.", e);
            }
        }
    }


    /**
     * 通过FileChannel从文件中读取数据到Buffer，然后打印出来。
     *
     * @param sourceFilePath
     */
    public static void readFromFileChannel(String sourceFilePath){
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileInputStream(new File(sourceFilePath)).getChannel();
            // 分配一个大小为1024字节的Buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 从FileChannel中读取数据到Buffer
            int length = fileChannel.read(byteBuffer);
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
            if(null!=fileChannel){
                try {
                    fileChannel.close();
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
