package com.basejava.webapp;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    public static final int SIZE = 10_000;
    private static int counter;

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
            }
        };
        thread0.setPriority(Thread.MIN_PRIORITY);
        thread0.start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState())).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        List<Thread> threadList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
            thread.start();
            threadList.add(thread);
        }
        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(counter);
        getDeadlock(mainConcurrency);
    }

    private synchronized void inc() {
        counter++;
    }

    private static void getDeadlock(MainConcurrency mainConcurrency) {
        System.out.println("Deadlock demonstration");
        final Object lock1 = new Object();
        final Object lock2 = new Object();
        Thread thread1 = new Thread(() -> {
            try {
                mainConcurrency.getResource(lock1, lock2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                mainConcurrency.getResource(lock2, lock1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
    }

    private void getResource(Object o1, Object o2) throws InterruptedException {
        synchronized (o1) {
            System.out.println("o1");
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(100);
            synchronized (o2) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("o2");
            }
        }
    }
}
