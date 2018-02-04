package com.lyb.nio.channel;

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

    private static final String STRING = "I confess that when first I made acquaintance with Charles Strickland I never for a moment discerned that there was in him anything out of the ordinary. " +
            "Yet now few will be found to deny his greatness. I do not speak of that greatness which is achieved by the fortunate politician or the successful soldier; " +
            "that is a quality which belongs to the place he occupies rather than to the man; and a change of circumstances reduces it to very discreet proportions. " +
            "The Prime Minister out of office is seen, too often, to have been but a pompous rhetorician, and the General without an army is but the tame hero of a market town. " +
            "The greatness of Charles Strickland was authentic. It may be that you do not like his art, but at all events you can hardly refuse it the tribute of your interest." +
            " He disturbs and arrests. The time has passed when he was an object of ridicule, and it is no longer a mark of eccentricity to defend or of perversity to extol him. " +
            "His faults are accepted as the necessary complement to his merits. It is still possible to discuss his place in art, and the adulation of his admirers is perhaps no less capricious than the disparagement of his detractors; " +
            "but one thing can never be doubtful, and that is that he had genius. To my mind the most interesting thing in art is the personality of the artist; and if that is singular, " +
            "I am willing to excuse a thousand faults. I suppose Velasquez was a better painter than El Greco, but custom stales one's admiration for him: the Cretan, sensual and tragic, " +
            "proffers the mystery of his soul like a standing sacrifice. The artist, painter, poet, or musician, by his decoration, sublime or beautiful, satisfies the aesthetic sense; " +
            "but that is akin to the sexual instinct, and shares its barbarity: he lays before you also the greater gift of himself. To pursue his secret has something of the fascination of a detective story. " +
            "It is a riddle which shares with the universe the merit of having no answer. The most insignificant of Strickland's works suggests a personality which is strange, tormented, " +
            "and complex; and it is this surely which prevents even those who do not like his pictures from being indifferent to them; it is this which has excited so curious an interest in his life and character.";

    public static void main(String args[]){
        String sourceFilePath = "E:\\Temp\\test.txt";
        readFromFileChannel(sourceFilePath);

        String targetFilePath = "E:\\Temp\\test2.txt";
        writeToFileChannel(targetFilePath);

        String sourceFilePathForSize = "E:\\Temp\\test.txt";
        testSizeOfFileChannel(sourceFilePathForSize);

        String sourceFilePathForReadPosition = "E:\\Temp\\test.txt";
        testPositionOfFileChannelOnRead(sourceFilePathForReadPosition);

        String targetFilePathForWritePosition = "E:\\Temp\\test3.txt";
        testPositionOfFileChannelOnWrite(targetFilePathForWritePosition);

        String targetFilePathForTruncate = "E:\\Temp\\moonAndSixpence.txt";
        testTruncateOfFileChannel(targetFilePathForTruncate);
    }


    public static void testTruncateOfFileChannel(String targetFilePath){
        FileChannel fileChannel = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(8*1024*1024);
        byteBuffer.put(STRING.getBytes());
        byteBuffer.flip();
        try {
            fileChannel = new FileOutputStream(targetFilePath).getChannel();
            fileChannel.write(byteBuffer);
            fileChannel.force(true);
            // truncate表示截取FileChannel对应的文件的长度，起始位置为文件的开头，与FileChannel的position没有关系，截取之后文件后面的内容将会被直接删除掉，
            fileChannel.truncate(1000);
            // 出于性能方面的考虑，操作系统会将数据缓存在内存中，所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，需要调用force()方法。
            // 所以之前的truncate方法，为了保证截取的文件内容能正常同步到文件上，最好使用force方法。
            // force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。
            fileChannel.force(true);
        } catch (FileNotFoundException e) {
            logger.error("test size of file channel has an error.",e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null==fileChannel){
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void testSizeOfFileChannel(String sourcePath){
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileInputStream(sourcePath).getChannel();
            // 获取FileChannel对应的文件的大小，无论文件多大都可以获取
            long size = fileChannel.size();
            logger.info("file channel of file {} size = {}.",sourcePath,size);
        } catch (FileNotFoundException e) {
            logger.error("test size of file channel has an error.",e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void testPositionOfFileChannelOnRead(String sourceFilePath){
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileInputStream(sourceFilePath).getChannel();
            // 为了简化测试，假定原文件比较小，将Buffer的大小设置为1024，一次性将文件内容读取出来，不再循环读取了，正常应该循环从FileChannel中读取放入到Buffer中，至到没有读入的长度为-1，表示 读取完成。
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            fileChannel.read(byteBuffer);
            // 将读到Buffer中的内容读取出来，转为字符串进行打印，便于查看读取了哪些内容。
            byteBuffer.flip(); // 将Buffer切换到读模式，limit表示可以从Buffer中读取多少数据
            byte [] byteArray = new byte[byteBuffer.limit()];
            byteBuffer.get(byteArray);
            logger.info("read from file channel, content is : \n{}",new String(byteArray));

            // 指定位置读取
            ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
            fileChannel.read(byteBuffer2,10);
            byteBuffer2.flip(); // 将Buffer切换到读模式，limit表示可以从Buffer中读取多少数据
            byte [] byteArray2 = new byte[byteBuffer2.limit()];
            byteBuffer2.get(byteArray2);
            logger.info("read from file channel's specified position while reading, content is : \n{}",new String(byteArray2));

            // 先指定位置再进行读取
            ByteBuffer byteBuffer3 = ByteBuffer.allocate(1024);
            fileChannel.position(20);
            fileChannel.read(byteBuffer3);
            byteBuffer3.flip(); // 将Buffer切换到读模式，limit表示可以从Buffer中读取多少数据
            byte [] byteArray3 = new byte[byteBuffer3.limit()];
            byteBuffer3.get(byteArray3);
            logger.info("read from file channel's specified position, content is : \n{}",new String(byteArray3));

            // 如果指定位置大于文件末尾，则返回-1
            ByteBuffer byteBuffer4 = ByteBuffer.allocate(1024);
            fileChannel.position(fileChannel.size()+20); // 指定位置为size + 20
            int readLength = fileChannel.read(byteBuffer4);
            logger.info("read length = "+readLength);
        } catch (FileNotFoundException e) {
            logger.error("test size of file channel has an error.",e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void testPositionOfFileChannelOnWrite(String targetFilePath){
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
            logger.info("---------- 顺序写入 ----------");
            fileChannel = new FileOutputStream(new File(targetFilePath)).getChannel();
            String content = "New String write to file, I am learning nio, but i have a lot of questions. i need a long time to understand them."+System.currentTimeMillis();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(content.getBytes());
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()){
                fileChannel.write(byteBuffer); // 将Buffer中的数据写入到FileChannel
            }
            // 获取当前FileChannel的position
            Long position = fileChannel.position();
            // 在写模式下，写完之后position等于size()，position在文件的末尾
            logger.info("file size = "+fileChannel.size());
            logger.info("position = "+position);

            logger.info("---------- 先指定位置再写入 ----------");
            // FileChannel的position的位置可以手动指定，再次进行写入的话会从指定之后的position开始写
            // 创建第二个Buffer，并向Buffer中写入一些数据。
            ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
            byteBuffer2.put("this is second.".getBytes());
            byteBuffer2.flip();
            // 将Buffer中的数据写入到FileChannel 中，并且在写入Channel的时候指定位置进行写，从最终写入FileChannel对应的文件结果来看，write的时候并不会原位置的内容往后移动，而是会进行覆盖。相当于重写了原位置的内容。
            fileChannel.position(0);
            while (byteBuffer2.hasRemaining()){
                fileChannel.write(byteBuffer2); // 将Buffer中的数据写入到FileChannel
            }
            long position2 = fileChannel.position();
            long size = fileChannel.size();
            logger.debug("position = "+position2); // 写入之后的position=原position+写入内容的长度
            logger.debug("size = "+size);   // size为原size，或者 size = position + 新写入内容的长度

            logger.info("---------- 写入时指定位置 ----------");
            // 写入时指定position
            ByteBuffer byteBuffer3 = ByteBuffer.allocate(1024);
            byteBuffer3.put("this is the third.".getBytes());
            byteBuffer3.flip();
            // 将Buffer中的数据写入到FileChannel 中，并且在写入Channel的时候指定位置进行写，从最终写入FileChannel对应的文件结果来看，write的时候并不会原位置的内容往后移动，而是会进行覆盖。相当于重写了原位置的内容。
            while (byteBuffer3.hasRemaining()){
                fileChannel.write(byteBuffer3,10); // 将Buffer中的数据写入到FileChannel
            }
            logger.debug("position = "+fileChannel.position()); // 写入之后的position=原position+写入内容的长度
            logger.debug("size = "+fileChannel.size());   // size为原size，或者 size = position + 新写入内容的长度

            // 指定长度超过文件末尾时写入会是什么结果呢？
            logger.info("---------- 指定长度超过文件末尾时写入 ----------");
            ByteBuffer byteBuffer4 = ByteBuffer.allocate(1024);
            byteBuffer4.put("this is the fourth.".getBytes());
            byteBuffer4.flip();
            fileChannel.position(2000); // 指定的位置远大于文件末尾，中间会出现文件空洞，二进制的零。
            while (byteBuffer4.hasRemaining()){
                fileChannel.write(byteBuffer4); // 将Buffer中的数据写入到FileChannel
            }
            logger.debug("position = "+fileChannel.position()); // 写入之后的position=原position+写入内容的长度
            logger.debug("size = "+fileChannel.size());   // size为原size，或者 size = position + 新写入内容的长度
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

}
