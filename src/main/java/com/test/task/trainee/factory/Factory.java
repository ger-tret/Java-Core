package com.test.task.trainee.factory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Factory implements Runnable {

    private final LinkedBlockingQueue<RobotPart> robotParts;
    transient int counter = 0;
    private AtomicInteger dayCounter = new AtomicInteger(0);

    public Factory(LinkedBlockingQueue<RobotPart> robotParts) {
        this.robotParts = robotParts;
    }

    public LinkedBlockingQueue<RobotPart> getRobotParts() {
        return robotParts;
    }

    public AtomicInteger getDayCounter() {
        return dayCounter;
    }

    @Override
    public void run() {
        try {
            while (counter < 10) {
                robotParts.put(new RobotPart());

                counter++;
            }
            Thread.sleep(200);
            counter = 0;
            dayCounter.incrementAndGet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
