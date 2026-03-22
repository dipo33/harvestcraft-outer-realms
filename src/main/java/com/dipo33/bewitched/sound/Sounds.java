package com.dipo33.bewitched.sound;

import com.dipo33.bewitched.Bewitched;

public class Sounds {
    public static final String MUTANDIS = formatName("mutandis");

    /**
     * Constructs a namespaced identifier for a given sound name.
     *
     * The namespace prefix is taken from Bewitched.MODID.
     *
     * @param name the unqualified sound name to namespace
     * @return the formatted identifier in the form "modid:name"
     */
    private static String formatName(String name) {
        return String.format("%s:%s", Bewitched.MODID, name);
    }
}
