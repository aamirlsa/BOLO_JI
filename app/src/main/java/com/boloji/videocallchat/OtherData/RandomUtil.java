package com.boloji.videocallchat.OtherData;

import java.util.Random;

public class RandomUtil {
    public static int generateRandomBetween(int i, int i2) {
        int nextInt = new Random().nextInt(2147483646) % i2;
        return nextInt < i ? i : nextInt;
    }
}
