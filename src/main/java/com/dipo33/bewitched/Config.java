package com.dipo33.bewitched;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean rightClickMatureCropHarvest = true;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        rightClickMatureCropHarvest = configuration.getBoolean(
            "rightClickMatureCropHarvest",
            Configuration.CATEGORY_GENERAL,
            rightClickMatureCropHarvest,
            "Should right click on mature crop harvest the crop?");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
