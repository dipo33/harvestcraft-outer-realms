package com.dipo33.bewitched.sound;

import com.dipo33.bewitched.Bewitched;

public class Sounds {
    public static final String MUTANDIS = formatName("mutandis");

    private static String formatName(String name) {
        return String.format("%s:%s", Bewitched.MODID, name);
    }
}
