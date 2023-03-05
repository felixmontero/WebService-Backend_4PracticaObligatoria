package com.esliceu.webservice.utils;

import java.util.Random;

public class Utils {
    public static String randomColor() {
        Random rand = new Random();
        int hue = rand.nextInt(360);  // El valor de hue va de 0 a 359
        int saturation = rand.nextInt(41) + 30;  // El valor de saturation va de 30 a 70%
        int lightness = rand.nextInt(41) + 30;  // El valor de lightness va de 30 a 70%
        return String.format("hsl(%d,%d%%,%d%%)", hue, saturation, lightness);
    }
}
