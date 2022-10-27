package com.winshare.demo.thread;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.winshare.demo.rest.Person;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws Exception {
/**
 * 会报错，key 必须实现CompareAble接口
 */
        /*ConcurrentSkipListMap skipListMap = new ConcurrentSkipListMap();
        skipListMap.put(new Person("qq", 1), new Person("qq", 1));
        Object qq1 = skipListMap.get(new Person("qq", 1));

        *//**
         * 会报错，key 必须实现Comparable接口
         * 所有有排序功能的Map都要实现 比较器接口Comparable
         *//*
        TreeMap<Person, Person> treeMap = new TreeMap<>();
        treeMap.put(new Person("qq", 1), new Person("qq", 1));
        treeMap.put(new Person("qq", 2), new Person("qq", 1));
        Person qq = new Person("qq", 3);
        treeMap.put(qq, new Person("qq", 1));
        Person person = treeMap.get(qq);


        long millis = System.currentTimeMillis();
        System.out.println("--------------" + millis);
        long nanos = TimeUnit.SECONDS.toNanos(10);
        System.out.println(nanos + "--------------" + (System.currentTimeMillis() - millis));*/

        DemoDataListener babyEventListener = new DemoDataListener();

        FileInputStream url = new FileInputStream("C:\\Users\\64357\\Downloads\\pdxNS4C7Na.xlsx");




        try{
            EasyExcel.read(new File("C:\\Users\\64357\\Downloads\\pdxNS4C7Na.xlsx"), DemoData.class, babyEventListener).sheet().doRead();
        }catch (ExcelAnalysisException e){

            String[] split = e.getMessage().split(":");
            if(split!=null && split.length > 1){
                System.out.println(split[1].split("to")[0]);
            }
        }

        int i = babyEventListener.getTotal().intValue();
        int a = 0;

    }

}
