package com.basejava.webapp;

public class MainDeadlock {
    public static void main(String[] args) {
        System.out.println("Deadlock demonstration");
        final String lock1 = "Lock 1";
        final String lock2 = "Lock 2";

        new Thread(() -> lockResources(lock1, lock2)).start();
        new Thread(() -> lockResources(lock2, lock1)).start();
    }

    private static void lockResources(Object o1, Object o2) {
        synchronized (o1) {
            System.out.println(getThreadName() + " locked object " + o1.toString());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getThreadName() + " is trying to lock object " + o2.toString());
            synchronized (o2) {
                System.out.println(getThreadName() + " locked object " + o2.toString());
            }
        }
    }

    private static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
