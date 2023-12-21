package com.iit.concurrentProgramming.Coursework.constants;

import java.util.Random;

/**
 * @Author Harini Rodrigo
 * @Version 1.0
 * @Since 27/11/2023
 * @Description: This class provides random values.
 */
public class Utils {

    /**
     * Generates a random time value in milliseconds.
     *
     * @return A random time value in milliseconds.
     */
    public static int getRandomTime() {
        Random rand = new Random();
        return (rand.nextInt(10) + 1) * 500;
    }
}
