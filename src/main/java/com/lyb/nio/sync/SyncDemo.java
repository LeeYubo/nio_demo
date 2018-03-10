package com.lyb.nio.sync;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yubo Lee
 * @create 2018-02-25 16:22
 **/
public class SyncDemo {
    private static Integer lock = 0;
    private static Integer number = 0;

    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String args[]){
        for(int i=0;i<10;i++){
            pool.submit(new CalcThread());
        }
        pool.shutdown();
    }

    private static class CalcThread implements Runnable {

        @Override
        public void run() {

            String name = Thread.currentThread().getName();

            for(int i=0;i<1000;i++){
                synchronized (lock){
                    number++;
                }
                System.out.println(name+": number = "+number);
            }
        }
    }
}
