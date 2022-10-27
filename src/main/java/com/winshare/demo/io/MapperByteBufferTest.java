package com.winshare.demo.io;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class MapperByteBufferTest {
    public static void main(String[] args) throws Exception {
        mmap();
        /*new Thread(()->{
            try {
                fileChannelFrom();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                fileChannelTo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();*/
    }
    public static void fileChannelTo() throws Exception{
        RandomAccessFile fileInputStream = new RandomAccessFile("D:\\zymy\\code\\aa.txt","rw");
        FileChannel inc = fileInputStream.getChannel();

        SocketAddress sad = new InetSocketAddress("127.0.0.1",6754);
        SocketChannel socketChannel = SocketChannel.open();
        // 连接
        socketChannel.connect(sad);
        System.out.println("client====SocketChannel");
        // tcp 优化.
        socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, Boolean.FALSE);
        // 非阻塞
        //socketChannel.configureBlocking(false);
        long l = inc.transferTo(0, 1024, socketChannel);

        System.out.println("================="+l);


    }
    public static void fileChannelFrom() throws Exception{
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(6754));
        SocketChannel accept = ssc.accept();
        System.out.println("server====SocketChannel");
        RandomAccessFile outFile = new RandomAccessFile("D:\\zymy\\code\\aaaa.txt","rw");
        FileChannel outc = outFile.getChannel();
        //最后一个参数，必须和客户端发送的大小一致，否则会一直等待读取完
        long l = outc.transferFrom(accept, 0, 381);
        System.out.println("-----------------------"+l);

    }

    public static void mmap() throws Exception{
        RandomAccessFile fileInputStream = new RandomAccessFile("D:\\zymy\\code\\aa.txt","rw");
        MappedByteBuffer map = fileInputStream.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileInputStream.length());

        RandomAccessFile outFile = new RandomAccessFile("D:\\zymy\\code\\aaccmm2.txt","rw");
        MappedByteBuffer outMap = outFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileInputStream.length());
        //获取具体同一内存空间的数据,但是新的 DirectByteBuffer的数据是从上一个DirectByteBuffer的postion开始取，且limit 和 cap 都是减 pos后的大小，
        // 只是重新设置postion 为 0 ，使多个操作各不影响（但内存是拥有块，如果多个操作同一位置上的byte会相互覆盖）
        ByteBuffer slice = outMap.slice();
        ByteBuffer aaByte = map.slice();
        aaByte.position(100);
        ByteBuffer aaByte1 = aaByte.slice();
        aaByte1.limit(50);//这样就能获取到一个只有指定数据的ByteBuffer
        long size = 0;
        while (size < 50){
            byte b = map.get();
            byte b1 = aaByte1.get(); // map.slice() 也能读，却互不影响
            //slice.put(b); 也能写入对应数据
            outMap.put(b1);
            size++;
        }
        //关闭流
    }

    public static void mmap11() throws Exception{
        RandomAccessFile fileInputStream = new RandomAccessFile("D:\\zymy\\code\\aa.txt","rw");
        MappedByteBuffer map = fileInputStream.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileInputStream.length());

        RandomAccessFile outFile = new RandomAccessFile("D:\\zymy\\code\\aaccmm2.txt","rw");
        MappedByteBuffer outMap = outFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileInputStream.length());
        //获取具体同一内存空间的数据,但是新的 DirectByteBuffer的数据是从上一个DirectByteBuffer的postion开始取，且limit 和 cap 都是减 pos后的大小，
        // 只是重新设置postion 为 0 ，使多个操作各不影响（但内存是拥有块，如果多个操作同一位置上的byte会相互覆盖）
        ByteBuffer slice = outMap.slice();
        ByteBuffer aaByte = map.slice();
        //aaByte.position(100);
        ByteBuffer aaByte1 = aaByte.slice();
        long size = 0;
        while (fileInputStream.length()>size ){
            byte b = map.get();
            byte b1 = aaByte1.get(); // map.slice() 也能读，却互不影响
            //slice.put(b); 也能写入对应数据
            outMap.put(b1);
            size++;
        }
        //关闭流
    }
    public void mlock(MappedByteBuffer mappedByteBuffer) {
        final long beginTime = System.currentTimeMillis();
        final long address = ((DirectBuffer) (mappedByteBuffer)).address();
        Pointer pointer = new Pointer(address);
        {//锁定大小，rocketmq 为1G 此处自己改为 64M验证
            int ret = LibC.INSTANCE.mlock(pointer, new NativeLong(1024 * 1024 * 64L));
        }
        {
            int ret = LibC.INSTANCE.madvise(pointer, new NativeLong(1024 * 1024 * 64L), LibC.MADV_WILLNEED);
        }
    }

    //rocketMQ
    public void munlock(MappedByteBuffer mappedByteBuffer) {
        final long beginTime = System.currentTimeMillis();
        final long address = ((DirectBuffer) (mappedByteBuffer)).address();
        Pointer pointer = new Pointer(address);//大小，rocketmq 为1G 此处自己改为 64M验证
        int ret = LibC.INSTANCE.munlock(pointer, new NativeLong(1024 * 1024 * 64L));
    }

}






