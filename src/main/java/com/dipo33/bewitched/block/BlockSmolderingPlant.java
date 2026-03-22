package com.dipo33.bewitched.block;

import com.dipo33.bewitched.data.Position;
import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Objects;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSmolderingPlant extends BlockBush {
    private final boolean canHang;
    private final SmolderConfig smolderConfig;

    /**
     * Create a smoldering plant block configured for hanging behavior and client visuals.
     *
     * @param canHang       true if the block may be supported from above (can hang); false if it requires support below
     * @param smolderConfig configuration for particle intensity, vertical center, and horizontal variation
     * @throws NullPointerException if {@code smolderConfig} is null
     */
    public BlockSmolderingPlant(boolean canHang, SmolderConfig smolderConfig) {
        super(Material.plants);
        this.canHang = canHang;
        this.smolderConfig = Objects.requireNonNull(smolderConfig, "smolderConfig must not be null");
    }

    /**
     * Sets this block's axis-aligned bounds (collision and rendering) in block-local coordinates.
     *
     * @param minX minimum X coordinate (local, typically 0.0–1.0)
     * @param minY minimum Y coordinate (local, typically 0.0–1.0)
     * @param minZ minimum Z coordinate (local, typically 0.0–1.0)
     * @param maxX maximum X coordinate (local, typically 0.0–1.0)
     * @param maxY maximum Y coordinate (local, typically 0.0–1.0)
     * @param maxZ maximum Z coordinate (local, typically 0.0–1.0)
     * @return this block instance to allow method chaining
     */
    public BlockSmolderingPlant withBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        return this;
    }

    /**
     * Determines whether this plant can remain at the specified world coordinates.
     *
     * @param world the world containing the block
     * @param x the block X coordinate
     * @param y the block Y coordinate
     * @param z the block Z coordinate
     * @return `true` if there is a solid block beneath the position, or if `canHang` is enabled and there is a solid block above; `false` otherwise
     */
    @Override
    public boolean canBlockStay(final World world, final int x, final int y, final int z) {
        return world.getBlock(x, y - 1, z).isBlockSolid(world, x, y - 1, z, ForgeDirection.UP.flag)
            || (world.getBlock(x, y + 1, z).isBlockSolid(world, x, y + 1, z, ForgeDirection.DOWN.flag) && canHang);
    }

    /**
     * Periodically attempts to spread this plant to a nearby valid position.
     *
     * <p>On a random tick this method, with a 1-in-15 chance, checks local density and—if not too dense—
     * performs up to four randomized nearby steps (each accepting a step only if the target is valid)
     * and finally places this block at the last valid candidate position if that position remains suitable.</p>
     */
    @Override
    public void updateTick(final World world, int x, int y, int z, final Random rng) {
        if (!shouldAttemptSpread(rng)) {
            return;
        }

        if (isTooDenseNearby(world, x, y, z)) {
            return;
        }

        Position pos = new Position(x, y, z);

        // initial candidate near the original block
        Position candidate = randomNearby(pos, rng);

        // up to 4 "walk" attempts; update pos when candidate is valid
        for (int i = 0; i < 4; i++) {
            if (canSpreadAt(world, candidate)) {
                pos = candidate;
            }
            candidate = randomNearby(pos, rng);
        }

        // final placement attempt
        if (canSpreadAt(world, candidate)) {
            world.setBlock(candidate.x(), candidate.y(), candidate.z(), this, 0, 2);
        }
    }

    /**
     * Determines whether the plant should attempt to spread during a random tick.
     *
     * @param rng the random number generator used to make the probabilistic decision
     * @return `true` with probability 1/15, `false` otherwise
     */
    private boolean shouldAttemptSpread(Random rng) {
        return rng.nextInt(15) == 0;
    }

    /**
     * Determines whether five or more blocks of this type exist within a 4-block horizontal radius
     * and from y-1 to y+1 vertically centered at the given coordinates.
     *
     * @param world the world to scan
     * @param x the x coordinate of the origin position
     * @param y the y coordinate of the origin position
     * @param z the z coordinate of the origin position
     * @return `true` if five or more matching blocks are found in the region, `false` otherwise
     */
    private boolean isTooDenseNearby(World world, int x, int y, int z) {
        final int radius = 4;

        // Includes the origin block in the density count
        int remaining = 5;
        for (int xi = x - radius; xi <= x + radius; xi++) {
            for (int zi = z - radius; zi <= z + radius; zi++) {
                for (int yi = y - 1; yi <= y + 1; yi++) {
                    if (world.getBlock(xi, yi, zi) == this) {
                        remaining--;
                        if (remaining <= 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Chooses a nearby position around the given origin with small random offsets.
     *
     * @param origin the central position to perturb
     * @param rng    the random source used to generate offsets
     * @return       a new Position whose X and Z are offset by -1..1 from the origin and whose Y is offset by -1, 0, or 1 (with a bias toward 0)
     */
    private Position randomNearby(Position origin, Random rng) {
        // [-1, 1]
        int nx = origin.x() + rng.nextInt(3) - 1;
        // -1, 0, 0, 1 (more likely to not move)
        int ny = origin.y() + rng.nextInt(2) - rng.nextInt(2);
        // [-1, 1]
        int nz = origin.z() + rng.nextInt(3) - 1;
        return new Position(nx, ny, nz);
    }

    /**
     * Determines whether a smoldering plant may be placed at the specified position.
     *
     * @param world the world to check
     * @param pos the candidate position for placement
     * @return true if the target position is air and the block below is grass, dirt, farmland, or sand, false otherwise
     */
    private boolean canSpreadAt(World world, Position pos) {
        Block block = world.getBlock(pos.x(), pos.y() - 1, pos.z());
        return world.isAirBlock(pos.x(), pos.y(), pos.z()) && (
            block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland || block == Blocks.sand
        );
    }

    /**
     * Spawns smoldering flame particles around the block according to the configured smolder parameters.
     *
     * The chance to emit a particle is governed by the block's `smolderConfig.intensity`. When emitted, the particle
     * is placed near the block center with vertical offset controlled by `smolderConfig.center` (plus a small random
     * variation) and horizontal displacement controlled by `smolderConfig.horizontalVariation`.
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rng) {
        if (rng.nextFloat() < this.smolderConfig.intensity) {
            final float CENTER_OFFSET = 0.5F;
            final float HEIGHT_VARIATION = 4F / 16F;

            float baseX = x + CENTER_OFFSET;
            float baseY = y + this.smolderConfig.center + (rng.nextFloat() - 0.5F) * HEIGHT_VARIATION;
            float baseZ = z + CENTER_OFFSET;

            float xOffset = rng.nextFloat() * this.smolderConfig.horizontalVariation - (this.smolderConfig.horizontalVariation / 2);
            float zOffset = rng.nextFloat() * this.smolderConfig.horizontalVariation - (this.smolderConfig.horizontalVariation / 2);

            world.spawnParticle("flame", baseX + xOffset, baseY, baseZ + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

    @Desugar
    public record SmolderConfig(float intensity, float center, float horizontalVariation) {
    }
}
