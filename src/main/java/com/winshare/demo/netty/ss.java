package com.winshare.demo.netty;

import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.*;

public class ss {
    public static final Map<String,String> MAP = new HashMap<>();



    public static void main() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        MAP.put("a","aaaaaa");
        for (int i = 0; i < 20 ; i++) {
            executorService.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    //TimeUnit.MILLISECONDS.sleep(10);
                    System.out.println(MAP.get("a")+"-------"+Thread.currentThread().getName());
                    MAP.put("a",Thread.currentThread().getName());
                    System.out.println(MAP.get("a")+"+++++++++++++++++++++");
                }
            });
        }
        ExecutorService tt = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 20 ; i++) {
            tt.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {

                }
            });
        }
    }
}
