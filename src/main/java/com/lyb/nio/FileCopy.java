package com.lyb.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 多种文件拷贝性能的比较
 *
 * @author liyubo4
 * @create 2018-01-28 22:54
 **/
public class FileCopy {

    public static void main(String[] args) throws Exception {

        String sourcePath = "D:\\download\\CentOS-7-x86_64-Minimal-1708.iso";
        String targetPath1 = "E:\\Temp\\downLoadTemp\\111\\CentOS-7-x86_64-Minimal-1708.iso";
        String targetPath2 = "E:\\Temp\\downLoadTemp\\222\\CentOS-7-x86_64-Minimal-1708.iso";
        String targetPath3 = "E:\\Temp\\downLoadTemp\\333\\CentOS-7-x86_64-Minimal-1708.iso";

        copyFileByChannel(sourcePath, targetPath1);
        copyFileByBuffer(sourcePath,targetPath2);
        copyFileByStream(sourcePath,targetPath3);

    }

    /**
     * copy file by channel.
     *
     * @param sourcePath   : source file path
     * @param targetPath   : target file path
     * @throws Exception   : exception
     */
    private static void copyFileByChannel(String sourcePath, String targetPath) throws Exception {
        Long startTime = System.currentTimeMillis();
        RandomAccessFile sourceFile = null;
        RandomAccessFile targetFile = null;
        try {
            sourceFile = new RandomAccessFile(sourcePath,"rw");
            targetFile = new RandomAccessFile(targetPath,"rw");

            FileChannel sourceFileChannel = sourceFile.getChannel();
            FileChannel targetFileChannel = targetFile.getChannel();
            // transferFrom方法原理：
            // 1、首先获取SourceChannel的长度，
            // 2、transferFrom中自定义了一个默认长度8M，如果SourceChannel的长度大于8M，那么就创建一个8M的MappedByteBuffer，如果小于8M，则创建一个SourceChannel长度的MappedByteBuffer；
            // 3、从SourceChannel中读取MappedByteBuffer长度的元素到Buffer中；
            // 4、将Buffer中的数据写入到TargetChannel中；
            // 5、SourceChannel的size置为原size减去已读取的内容长度，循环读取剩余数据内容。
            targetFileChannel.transferFrom(sourceFileChannel, 0, sourceFileChannel.size());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            sourceFile.close();
            targetFile.close();
    }
        Long stopTime = System.currentTimeMillis();
        System.out.println(" copyFileByChannel 耗时： "+(stopTime-startTime));
    }


    /**
     * copy file via buffer.
     *
      * @param sourcePath   : source file path
     * @param targetPath    : target file path
     * @throws Exception    : exception
     */
    private static void copyFileByBuffer(String sourcePath, String targetPath) throws Exception {
        long beginTime = System.currentTimeMillis();
        FileChannel fc = new FileInputStream(new File(sourcePath)).getChannel();
        FileChannel fco = new RandomAccessFile(new File(targetPath), "rw").getChannel();
        ByteBuffer buf = ByteBuffer.allocate(8388608);
        while (fc.read(buf) != -1) {
            buf.flip();
            fco.write(buf);
            buf.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("采用NIO Buffer，耗时：" + (endTime - beginTime));
    }


    /**
     * copy file by stream.
     *
     * @param sourcePath   : source file path
     * @param targetPath   : target file path
     * @throws Exception   : exception
     */
    private static void copyFileByStream(String sourcePath, String targetPath) throws Exception {
        long beginTime = System.currentTimeMillis();
        File source = new File(sourcePath);
        File dest = new File(targetPath);
        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] buf = new byte[8388608];
        int len = 0;
        while ((len = fis.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fis.close();
        fos.close();
        long endTime = System.currentTimeMillis();
        System.out.println("采用BIO Stream，耗时：" + (endTime - beginTime));
    }
}
