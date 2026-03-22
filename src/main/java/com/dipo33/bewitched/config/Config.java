package com.dipo33.bewitched.config;

import com.dipo33.bewitched.Bewitched;
import com.dipo33.bewitched.api.mutation.Mutation;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean rightClickMatureCropHarvest = true;

    public static Collection<Mutation> mutandisAdditionalMutations;
    public static Collection<Mutation> mutandisAdditionalFlowerPotMutations;
    public static Collection<Mutation> mutandisExtremisAdditionalMutations;
    public static Collection<Mutation> mutandisExtremisAdditionalGrassMutations;

    /**
     * Load configuration from the given file and populate Config's static settings.
     *
     * Populates the following static fields from the file: {@code rightClickMatureCropHarvest},
     * {@code mutandisAdditionalMutations}, {@code mutandisAdditionalFlowerPotMutations},
     * {@code mutandisExtremisAdditionalMutations}, and {@code mutandisExtremisAdditionalGrassMutations}.
     * If the configuration is changed during loading, the method saves the updated configuration back to the file.
     *
     * @param configFile the file to read configuration from and save back to if changes are made
     */
    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        rightClickMatureCropHarvest = configuration.getBoolean(
            "rightClickMatureCropHarvest", Configuration.CATEGORY_GENERAL, true,
            "Should right click on mature crop harvest the crop?"
        );

        mutandisAdditionalMutations = Arrays.stream(configuration.getStringList(
                "mutandisAdditionalMutations", Configuration.CATEGORY_GENERAL, new String[]{
                    "bewitched:glint_weed@0 -> bewitched:glint_weed@0",
                    "minecraft:tallgrass@2 -> minecraft:tallgrass@2"
                }, "Additional mutations in the Mutandis mutation pool"
            )).map(Config::tryParseMutation)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        mutandisAdditionalFlowerPotMutations = Arrays.stream(configuration.getStringList(
                "mutandisAdditionalFlowerPotMutations", Configuration.CATEGORY_GENERAL, new String[]{},
                "Additional mutations in the Mutandis flower pot mutation pool"
            )).map(Config::tryParseMutation)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        mutandisExtremisAdditionalMutations = Arrays.stream(configuration.getStringList(
                "mutandisExtremisAdditionalMutations", Configuration.CATEGORY_GENERAL, new String[]{},
                "Additional mutations in the Mutandis Extremis mutation pool"
            )).map(Config::tryParseMutation)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        mutandisExtremisAdditionalGrassMutations = Arrays.stream(configuration.getStringList(
                "mutandisExtremisAdditionalGrassMutations", Configuration.CATEGORY_GENERAL, new String[]{},
                "Additional mutations in the Mutandis Extremis special grass mutation pool"
            )).map(Config::tryParseMutation)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    /**
     * Parse a configuration line describing a mutation and return the corresponding Mutation object.
     *
     * @param line configuration line representing a mutation
     * @return the parsed Mutation, or {@code null} if the line is invalid or cannot be parsed
     */
    private static Mutation tryParseMutation(String line) {
        try {
            return Parsers.parseMutation(line);
        } catch (Exception e) {
            Bewitched.LOG.warn("Invalid mutandis mutation config entry '{}': {}", line, e.getMessage());
            return null;
        }
    }
}
