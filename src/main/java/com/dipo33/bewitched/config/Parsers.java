package com.dipo33.bewitched.config;

import com.dipo33.bewitched.api.mutation.Mutation;
import com.dipo33.bewitched.data.MetaBlock;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class Parsers {

    /**
     * Parse a mutation rule string into a Mutation.
     *
     * @param line the rule in the form "<sources>-><output>" where <sources> are '+'-separated block tokens
     *             and <output> is a block token that must include metadata (e.g. "stone@3")
     * @return the parsed Mutation whose output is derived from the right-hand token (with a basic strategy
     *         based on its metadata) and whose sources are parsed from the left-hand tokens
     * @throws IllegalArgumentException if the input is missing "->", if the output lacks required metadata,
     *                                  if a block name cannot be resolved or is air, if metadata is not an
     *                                  integer (except '*'), or if metadata is outside 0–15
     */
    public static Mutation parseMutation(String line) {
        String[] arrow = line.split("->", 2);
        if (arrow.length != 2) {
            throw new IllegalArgumentException("Missing '->'");
        }

        String sourcesPart = arrow[0].trim();
        String outputPart = arrow[1].trim();

        List<Mutation.Source> sources = new ArrayList<>();
        for (String token : sourcesPart.split("\\+")) {
            sources.add(parseSource(token.trim()));
        }

        MetaBlock output = parseMetaBlock(outputPart, true);

        return new Mutation(
            new Mutation.Output(output.block(), Mutation.basicStrategy(output.meta())),
            sources
        );
    }

    /**
     * Parses a source token into a corresponding Mutation.Source.
     *
     * @param token a block token optionally suffixed with "@<meta>" or "@*" (e.g. "stone", "stone@3", "stone@*")
     * @return a Source that matches any metadata for the parsed block if no metadata was specified, or a Source that matches the parsed metadata exactly
     */
    private static Mutation.Source parseSource(String token) {
        MetaBlock parsed = parseMetaBlock(token, false);

        if (parsed.meta() == null) {
            return Mutation.Source.anyMeta(parsed.block());
        } else {
            return Mutation.Source.exact(parsed.block(), parsed.meta());
        }
    }

    /**
     * Parses a token of the form "blockName@meta" or "blockName" into a MetaBlock.
     *
     * <p>If a metadata suffix is present:
     * - a literal "*" is treated as unspecified (represented as `null`),
     * - a decimal integer is parsed and validated to be between 0 and 15 inclusive.
     *
     * @param token the input token containing a block name optionally followed by "@<meta>"
     * @param requireMeta if true, the token must include a metadata suffix (otherwise an exception is thrown)
     * @return a MetaBlock containing the resolved Block and the parsed metadata (may be `null` when metadata is not required)
     * @throws IllegalArgumentException if the metadata part is not a number (except "*"),
     *         if metadata is missing when {@code requireMeta} is true,
     *         if the block name does not resolve to a valid block or is {@code Blocks.air},
     *         or if a numeric metadata value is outside the range 0–15
     */
    private static MetaBlock parseMetaBlock(String token, boolean requireMeta) {
        int at = token.indexOf('@');

        String blockName;
        Integer meta = null;

        if (at >= 0) {
            blockName = token.substring(0, at).trim();
            String metaText = token.substring(at + 1).trim();

            if (!metaText.equals("*")) {
                try {
                    meta = Integer.parseInt(metaText);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid metadata '" + metaText + "'", e);
                }
            }
        } else {
            blockName = token.trim();
        }

        if (requireMeta && meta == null) {
            throw new IllegalArgumentException("Output must specify metadata: " + token);
        }

        Block block = (Block) Block.blockRegistry.getObject(blockName);
        if (block == null || block == Blocks.air) {
            throw new IllegalArgumentException("Unknown block: " + blockName);
        }

        if (meta != null && (meta < 0 || meta > 15)) {
            throw new IllegalArgumentException("Metadata out of range 0-15: " + token);
        }

        return new MetaBlock(block, meta);
    }
}
