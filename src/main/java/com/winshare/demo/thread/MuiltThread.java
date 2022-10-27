package com.winshare.demo.thread;

import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MuiltThread {


    public static void main(String[] aaa) {


        long l = System.currentTimeMillis();
        CompletableFuture completableFuture1 = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("completableFuture1-------------+"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "completableFuture1";
        });
        CompletableFuture completableFuture2 = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("completableFuture2-------------+"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "completableFuture2";
        });
        CompletableFuture completableFuture = completableFuture1.thenCombine(completableFuture2, (v1, v2) -> v1 +""+ v2);

        /*CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(completableFuture1, completableFuture2);
        try {
            Void aVoid = voidCompletableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        // System.out.println("args = [" + o + "]"+Thread.currentThread().getName());
        try {
            Object o = completableFuture.get();
            System.out.printf((System.currentTimeMillis() - l) /1000+"--------"+ o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
