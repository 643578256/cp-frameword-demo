package com.winshare.demo.thread;

import com.winshare.demo.rest.Person;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class ReferenceTest {
    public static void main(String[] args) {
        ReferenceQueue<Pserson> psersonReferenceQueue = new ReferenceQueue<>();
        WeakReference<Pserson> weakReference = new WeakReference(new Person("AA",1),psersonReferenceQueue);
        WeakReference<Pserson> weakReference1 = new WeakReference<>(new Pserson("BB",2),psersonReferenceQueue);
        Reference<? extends Pserson> poll = psersonReferenceQueue.poll();
        System.out.println(poll == null );
        System.gc();
        System.out.println("GC====================");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Reference<? extends Pserson> pol2 = psersonReferenceQueue.poll();
        System.out.println(pol2 == null );
        System.gc();
        Reference<? extends Pserson> pol3 = psersonReferenceQueue.poll();
        System.out.println(pol2 == null );
        System.out.println("=============GC===================="+weakReference.get());
    }
}
