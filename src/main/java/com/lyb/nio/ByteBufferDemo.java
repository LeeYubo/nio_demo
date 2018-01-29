package com.lyb.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ${DESCRIPTION}
 *
 * @author Yubo Lee
 * @create 2018-01-28 0:03
 **/
public class ByteBufferDemo {

    public static void main(String args[]){
//        testByteBufferBase();
        testFileChannel();
    }


    public static void testFileChannel(){
        FileChannel fileChannel = null;
        try {
            fileChannel = new RandomAccessFile("e:\\test.txt","rw").getChannel();
            String content = "New String to write to file, I am learning nio, but i have a lot of questions. i need a long time to understand them."+System.currentTimeMillis();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // ByteBuffer在创建的时候被设置大小为1024，如果要写入到buffer中的内容大于1024，将会抛出异常，超出了buffer的capacity
            byteBuffer.put(content.getBytes());
            // 放入完成之后将buffer的模式切换为读模式
            byteBuffer.flip();
            // 因为channel不一定是一次性从buffer将数据全部数据读入到channel中，有可能是分多次，所以如果想保证将buffer中的数据全部读出，最好使用while的形式，多次判断，
            // 每次从buffer中读取数据之后他的remaining值会更新，所以使用hasRemaining可以判断buffer中是否还有数据剩余，如果有将持续调用channel的write方法，从buffer读入再写入到channel。
            while (byteBuffer.hasRemaining()){
                fileChannel.write(byteBuffer);
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


    public static  void testByteBufferBase(){
        // Buffer的初始化，其实是创建了一个指定类型的指定长度的数组,ByteBuffer还是一个抽象类，
        // 它下面还有四个子类，下面方式创建的是HeapByteBuffer对象，数组信息在抽象类ByteBuffer类里
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 'H').put((byte) 'e').put((byte) 'l').put((byte) 'l').put((byte) 'o').put((byte)'a').put((byte) 'b');
        System.out.println("position = "+byteBuffer.position());
        System.out.println("byteBuffer.hasRemaining() = "+byteBuffer.hasRemaining());
        System.out.println("byteBuffer.remaining() = "+byteBuffer.remaining());
        System.out.println("byteBuffer.limit = "+byteBuffer.limit());

        System.out.println("切换为读模式");
        byteBuffer.flip();
        // 在之前调用了put方法，他会自动将Buffer设置为写模式，如果在写完之后不进行模式切换，那它依然是写模式，
        // 在写模式下调用get方法，如果不带绝对位置的话返回position位置的值，就是个null，并且position还会加1。
        // 在写模式下，每put一次（不带绝对位置），position加1，limit值不变；
        // 切换成读模式下之后position=0；limit=当前的数据容量，remain()=还可以读取的数据数量，每次读完之后position位置加1，limit不变。

        // limit在写模式下表示一共可写入多少数据，在读模式下表示一共可读多少数据，他不会随着操作而发生变化、
        // 比如在写模式下，初始化时limit=10，写了五个之后，limit还是10，但是remaining会减少成5；
        // 切换到读模式下，limit被置位5，表示一共可读的数据量为5，从中读取数据之后limit的值一直不变，但是remaining会减少。
        System.out.println("-- "+(char)byteBuffer.get());
        System.out.println("-- "+(char)byteBuffer.get());
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
