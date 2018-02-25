package com.lyb.nio.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.*;

/**
 * @author liyubo4
 * @create 2018-02-22 12:02
 **/
public class SelectorDemo {

    private static final Logger logger = LoggerFactory.getLogger(SelectorDemo.class);

    public static void main(String args[]){

        SelectableChannel selectableChannel1;
        SelectableChannel selectableChannel2;
        SelectableChannel selectableChannel3;
        try {
            selectableChannel1 = ServerSocketChannel.open();
            selectableChannel2 = SocketChannel.open();
            selectableChannel3 = SocketChannel.open();


            Selector selector = Selector.open();
            selectableChannel1.configureBlocking(false);
            selectableChannel2.configureBlocking(false);
            selectableChannel3.configureBlocking(false);

            // channel在向Selector注册时需要告知Selector，让它监听哪些类型的事件，共有四种类型的事件：读（Read）、写（Write）、链接（Connect）、接收（Accept）
            // 每一种事件用一个整型的数值来表示，1>>0 1>>2 1>>3 1>>4 分别为1 4 8 16 ，如果监听多种类型的事件，那么整数值将会是这几个整数的和。
            // 按道理说是Channel注册到Selector时告诉它监听的事件类型。但是事件类型也可以在后续进行修改，修改之后会返回一个新的SelectionKey，
            // 可以通过带参数的interestOps()方法修改事件类型，但是在设置的时候需要注意，不是每一个SelectableChannel都支持所有的事件类型，比如说SocketChannel就不支持Accept操作。
            // 所以在注册事件之前，最好使用SelectableChannel的validOps()方法验证一下都支持哪些操作。
            SelectionKey selectionKey1 = selectableChannel1.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("selectionKey1 = "+selectionKey1.interestOps());

            SelectionKey selectionKey2 = selectableChannel2.register(selector, SelectionKey.OP_READ);
            logger.info("selectionKey2 = "+selectionKey2.interestOps());
            logger.info("selectableChannel2.validOps() = "+selectableChannel2.validOps());
            selectionKey2.interestOps(selectableChannel2.validOps());
            logger.info("selectionKey2-2 = "+selectionKey2.interestOps());
            selectionKey2.readyOps();

            // 或（|）操作表示两个整数每一位进行比较，如果有一个为1则为1，否则为0，因为这四种操作类型都是有一个不同的位来表示的，所以“或”操作之后会做一个拼接的操作，把几个数值加起来。
            // 与此同时，可以通过“与”操作来验证某一位是否有值，从而验证某一种类型的操作是否被监听或是否已经就绪。
            selectionKey2.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            logger.info("selectionKey2-3 = "+selectionKey2.interestOps());

            SelectionKey selectionKey3 = selectableChannel3.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            logger.info("selectionKey3 = "+selectionKey3.interestOps());
            logger.info(" 111111111111111111111 ");
            WakeThread wakeThread = new WakeThread(selector);
            logger.info(" 启动wakeup线程 ");
            wakeThread.run();
            selector.select();
            logger.info(" 222222222222222222222 ");

            selector.keys();
            selector.selectedKeys();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class WakeThread implements Runnable {

        private Selector selector;

        WakeThread(Selector selector){
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            selector.wakeup();
//            logger.info(" wakeup finished. ");
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info(" close finished. ");
        }
    }


}
