package com.lyb.nio;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author Yubo Lee
 * @create 2018-01-30 23:35
 **/
public class FileChannelDemo {

    public static void main(String args[]){

        FileChannel fileChannel1 = null;
        FileChannel fileChannel2 = null;
        FileChannel fileChannel3 = null;

        String sourceFilePath = "";
        try {
            fileChannel1 = new FileInputStream(new File(sourceFilePath)).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String targetFilePath = "";
        try {
            fileChannel2 = new FileOutputStream(new File(targetFilePath)).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
