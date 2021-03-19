package com.basejava.webapp;

public class MainDeadlock {
    public static void main(String[] args) {
        MainDeadlock mainDeadlock = new MainDeadlock();
        mainDeadlock.getDeadlock();
    }

    private void getDeadlock() {
        System.out.println("Deadlock demonstration");
        final String lock1 = "Lock 1";
        final String lock2 = "Lock 2";

        this.getThread(lock1, lock2).start();
        this.getThread(lock2, lock1).start();
    }

    private Thread getThread(Object lock1, Object lock2) {
        return new Thread(() -> {
            try {
                this.lockResources(lock1, lock2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void lockResources(Object o1, Object o2) throws InterruptedException {
        synchronized (o1) {
            System.out.println(Thread.currentThread().getName() + " locked object " + o1.toString());
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + " is trying to lock object " + o2.toString());
            synchronized (o2) {
                System.out.println(Thread.currentThread().getName() + " locked object " + o2.toString());
            }
        }
    }
}
