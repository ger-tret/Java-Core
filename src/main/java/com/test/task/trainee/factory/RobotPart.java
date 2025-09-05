package com.test.task.trainee.factory;

import lombok.Getter;

import java.util.Random;

@Getter
public class RobotPart {
    private Part part;
    Random r = new Random();

    enum Part {
        HEAD, TORSO, FEET, HAND
    }

    RobotPart() {
        int rand = r.nextInt(100);
        if (rand >= 0 && rand < 10) {
            this.part = Part.HEAD;
        } else if (rand >= 10 && rand < 20) {
            this.part = Part.TORSO;
        } else if (rand >= 20 && rand < 30) {
            this.part = Part.FEET;
        } else if (rand >= 30) {
            this.part = Part.HAND;
        }
    }
}
