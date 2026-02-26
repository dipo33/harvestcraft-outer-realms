package com.dipo33.bewitched;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean rightClickMatureCropHarvest = true;

    /**
     * Loads the mod configuration from the provided file, updates the
     * {@code rightClickMatureCropHarvest} setting from the "rightClickMatureCropHarvest"
     * entry in the general category, and persists any changes.
     *
     * @param configFile the file to load and (if modified) save the configuration to
     */
    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        rightClickMatureCropHarvest = configuration.getBoolean(
            "rightClickMatureCropHarvest", Configuration.CATEGORY_GENERAL, rightClickMatureCropHarvest,
            "Should right click on mature crop harvest the crop?"
        );

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
