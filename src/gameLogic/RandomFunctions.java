package gameLogic;

import java.util.Random;

public class RandomFunctions {

    public static Random random = new Random();

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public static boolean randomChance(double percentage) {
        double number = random.nextDouble();
        return number < percentage;
    }

}
