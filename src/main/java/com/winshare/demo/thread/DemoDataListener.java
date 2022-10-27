package com.winshare.demo.thread;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class DemoDataListener extends AnalysisEventListener<DemoData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 200;
    List<DemoData> list = new ArrayList<DemoData>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param demoDAO
     */

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */

    AtomicInteger total = new AtomicInteger(0);
    AtomicInteger success = new AtomicInteger(0);
    AtomicInteger error = new AtomicInteger(0);

    AtomicInteger wait = new AtomicInteger(0);
    Random random =  new Random();
    Random random1 =  new Random();
    ExecutorService executors = Executors.newFixedThreadPool(5);
    int index =0;
    @Override
    public void invoke(DemoData data, AnalysisContext context) {

        LOGGER.info("存储数据库成功={}，{}",index++,data);
       /* list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            List nList = new ArrayList(list);
            list.clear();
            executors.execute(()->{
                wait.incrementAndGet();
                saveData(nList);
                nList.clear();
                wait.decrementAndGet();

            });
            // 存储完成清理 list
        }*/
        total.addAndGet(1);
    }
    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        /*wait.incrementAndGet();
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData(list);
        LOGGER.info("所有数据解析完成！"+total.intValue());
        wait.decrementAndGet();*/
        total.addAndGet(1);
    }
    /**
     * 加上存储数据库
     */
    private void saveData( List<DemoData> newList) {
        try {
            int i = new Random().nextInt(random.nextInt(5000));
            int i1 = random1.nextInt(50);
            error.addAndGet(i1);
           int aa = newList.size()-i1;
            success.addAndGet(aa);
           total.addAndGet(newList.size());
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("{}条数据，开始存储数据库！{}", newList.size(),total.intValue());
    }

    public AtomicInteger getSuccess() {
        return success;
    }

    public AtomicInteger getError() {
        return error;
    }

    public AtomicInteger getWait() {
        return wait;
    }

    public AtomicInteger getTotal() {
        return total;
    }
}