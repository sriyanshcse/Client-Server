package com.okcredit.server;


public class WrapperThread<T extends Runnable> extends Thread {
    private final T target;

    public WrapperThread(String name, T target) {
        super(target, name);
        this.target = target;
    }

    public T getTarget() {
        return target;
    }

    public WrapperThread<T> startThread() {
        start();
        return this;
    }
}
