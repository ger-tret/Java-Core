package com.test.task.trainee.factory;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Factory factory = new Factory(new LinkedBlockingQueue<>());
        Faction wednesdayFaction = new Faction("Wednesday", factory.getRobotParts());
        Faction worldFaction = new Faction("World", factory.getRobotParts());
        Thread factoryThread = new Thread(factory);
        Thread faction1Thread = new Thread(wednesdayFaction);
        Thread faction2Thread = new Thread(worldFaction);
        while (factory.getDayCounter().get() < 100) {
            factoryThread.run();
            faction1Thread.run();
            faction2Thread.run();
        }
        if (worldFaction.getRobotsCount().get() < wednesdayFaction.getRobotsCount().get()) {
            System.out.println("100 day's passed! Winner is " + wednesdayFaction.getFactionName() + "!!!");
        } else {
            System.out.println("100 day's passed! Winner is " + worldFaction.getFactionName() + "!!! =)");
        }

    }
}
