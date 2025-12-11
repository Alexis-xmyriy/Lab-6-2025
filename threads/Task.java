package threads;

import functions.Function;

public class Task {
    private Function f;
    private double leftX;
    private double rightX;
    private double step;
    private int taskCount;
    private volatile boolean taskReady = false;

    public Task() {
        this.taskCount = 0;
    }
    public Function getFunction() {
        return f;
    }
    public synchronized void setFunction(Function f) {
        this.f = f;
        this.taskReady = true;
        notify();
    }
    public double getLeft() {
        return leftX;
    }
    public void setLeft (double leftX) {
        this.leftX = leftX;
    }
    public double getRight() {
        return rightX;
    }
    public void setRight (double rightX) {
        this.rightX = rightX;
    }
    public double getStep() {
        return step;
    }
    public void setStep(double step) {
    
        this.step = step;
    }
    public  int getTasksCount() {
        return taskCount;
    }
    public void setTasksCount(int taskCount) {
        this.taskCount = taskCount;
    }
    public synchronized boolean taskReady() {
        return taskReady;
    }    
    public synchronized void resetTask() {
        this.taskReady = false;
    }
    
}