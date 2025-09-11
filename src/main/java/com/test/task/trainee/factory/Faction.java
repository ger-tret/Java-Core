package com.test.task.trainee.factory;
import lombok.Getter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Faction implements Runnable{

    private String factionName;
    private final LinkedBlockingQueue<RobotPart> robotParts;
    private AtomicInteger handCount = new AtomicInteger(0);
    private AtomicInteger torsoCount = new AtomicInteger(0);
    private AtomicInteger headCount = new AtomicInteger(0);
    private AtomicInteger feetCount = new AtomicInteger(0);
    private AtomicInteger robotsCount = new AtomicInteger(0);
    transient int counter = 0;

    public Faction(String factionName, LinkedBlockingQueue<RobotPart> robotParts) {
        this.factionName = factionName;
        this.robotParts = robotParts;
    }

    @Override
    public void run() {
        try{
            while(counter < 5){
                RobotPart robotPart = robotParts.take();
                if(robotPart.getPart().equals(RobotPart.Part.HAND)) {
                    handCount.incrementAndGet();
                } else if(robotPart.getPart().equals(RobotPart.Part.TORSO)){
                    torsoCount.incrementAndGet();
                } else if(robotPart.getPart().equals(RobotPart.Part.HEAD)){
                    headCount.incrementAndGet();
                } else if(robotPart.getPart().equals(RobotPart.Part.FEET)) {
                    feetCount.incrementAndGet();
                }
                counter++;
            }
            if (handCount.get() > 1 && torsoCount.get() > 1 && headCount.get() > 1 && feetCount.get() >1){
                System.out.println(factionName + " have " + robotsCount.incrementAndGet() + " robots!");
                handCount.decrementAndGet();
                torsoCount.decrementAndGet();
                headCount.decrementAndGet();
                feetCount.decrementAndGet();

            }
            counter = 0;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
