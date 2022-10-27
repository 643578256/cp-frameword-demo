package com.winshare.demo.mq.api.util;

import java.io.*;

public class SerializableMessageUtil {


    public static byte[] enCode(Object message){
        if(message instanceof Serializable){
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(byteOut);
                outputStream.writeObject(message);
                byte[] bytes = byteOut.toByteArray();
                outputStream.close();
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            throw new RuntimeException("序列化对象没有实现序列化接口");
        }
        throw new RuntimeException(message.toString()+"：对象序列化对象异常");
    }


    public static <T> T deCode(byte[] message){
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(message);
            ObjectInputStream inputStream = new ObjectInputStream(byteIn);
            Object o = inputStream.readObject();
            return (T)o;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("对象反序列化异常");
    }
}
