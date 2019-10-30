package com.cxlsky.controller;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {
    private Lock lock = new ReentrantLock();
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public static void main(String[] args)  {
        final Test test = new Test();

        new Thread(){
            @Override
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

        new Thread(){
            @Override
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

    }

    public void get(Thread thread) {
        reentrantReadWriteLock.readLock().lock();
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行读操作");
            }
            System.out.println(thread.getName() + "读操作完毕");
        }finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }

    public void insert(Thread thread) {
        //注意这个地方
//        Lock lock = new ReentrantLock();
//        lock.lock();
        if (lock.tryLock()) {

            try {
                System.out.println(thread.getName() + "得到了锁");
                for (int i = 0; i < 5; i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                System.out.println(thread.getName() + "释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName() + "获取锁失败");

        }
    }
}
