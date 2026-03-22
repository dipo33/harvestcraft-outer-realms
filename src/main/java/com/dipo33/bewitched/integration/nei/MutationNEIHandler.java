package com.dipo33.bewitched.integration.nei;

import org.lwjgl.opengl.GL11;

import com.dipo33.bewitched.Bewitched;
import com.dipo33.bewitched.api.mutation.Mutation;
import com.dipo33.bewitched.api.mutation.MutationPoolType;
import com.dipo33.bewitched.api.mutation.MutationRegistry;
import com.dipo33.bewitched.init.BewitchedItems;
import com.dipo33.bewitched.utils.ItemStackHelper;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class MutationNEIHandler extends TemplateRecipeHandler {

    /**
     * Provides the localized display name for the mutation NEI recipe category.
     *
     * @return the localized display name for the mutation NEI category
     */
    @Override
    public String getRecipeName() {
        return I18n.format(Bewitched.MODID + ".nei.mutation");
    }

    /**
     * Provides the resource path of the NEI mutation GUI texture.
     *
     * @return the resource location string for the NEI mutation GUI texture
     */
    @Override
    public String getGuiTexture() {
        return Bewitched.MODID + ":textures/gui/nei_mutation.png";
    }

    /**
     * Provides the GUI container class associated with this recipe handler.
     *
     * @return the GuiContainer subclass this handler targets, or null if there is no specific GUI class
     */
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return null;
    }

    /**
     * NEI overlay identifier for mutation recipe displays.
     *
     * @return the overlay ID string formed as Bewitched.MODID + ".mutation"
     */
    @Override
    public String getOverlayIdentifier() {
        return Bewitched.MODID + ".mutation";
    }

    /**
     * Registers the recipe transfer area used by the mutation overlay.
     *
     * Adds a single transfer rectangle at (67, 22) with size 30×20 that maps to this handler's overlay identifier.
     */
    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(67, 22, 30, 20), this.getOverlayIdentifier()));
    }

    /**
     * Provides the vertical size of the recipe display in pixels.
     *
     * @param recipe the index of the recipe being queried
     * @return the height of the recipe display in pixels
     */
    @Override
    public int getRecipeHeight(final int recipe) {
        return 150;
    }

    /**
     * Draws the recipe background using this handler's NEI GUI texture.
     *
     * @param recipe the index of the recipe being rendered
     */
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 144);
    }

    /**
     * Draws the recipe-specific overlay elements for the recipe at the given index.
     *
     * This renders the mutation progress bar and the chance/extra descriptive text for the selected recipe.
     *
     * @param recipeIdx index of the recipe in this handler's recipe list
     */
    @Override
    public void drawExtras(final int recipeIdx) {
        var recipe = (CachedMutationRecipe) this.arecipes.get(recipeIdx);
        this.drawProgressBar(recipe);
        this.drawAdditionalText(recipe);
    }

    /**
     * Draws the mutation progress bar using a texture variant selected from the recipe's catalyst.
     *
     * Chooses one of two progress-bar texture slices based on whether the catalyst contains
     * MUTANDIS or does not contain MUTANDIS_EXTREMIS, then renders the bar at the fixed UI position.
     *
     * @param recipe the cached mutation recipe whose catalyst determines the progress-bar variant
     */
    private void drawProgressBar(CachedMutationRecipe recipe) {
        var catalyst = recipe.getIngredient();
        if (catalyst.contains(BewitchedItems.MUTANDIS.get()) || !catalyst.contains(BewitchedItems.MUTANDIS_EXTREMIS.get())) {
            drawProgressBar(67, 22, 167, 0, 30, 20, 20, 1);
        } else {
            drawProgressBar(67, 22, 167, 21, 30, 20, 20, 1);
        }
    }

    /**
     * Renders the localized mutation chance text (and a pool-specific note for flower pots)
     * for the provided cached mutation recipe onto the NEI recipe display.
     *
     * The displayed percentage is derived from the number of possible outputs in the recipe
     * and is formatted with up to two decimal places. The resulting text is wrapped to fit
     * the display width and drawn starting at x=2, y=122 in black.
     *
     * @param recipe the cached mutation recipe whose outputs and pool type determine the text
     */
    private void drawAdditionalText(CachedMutationRecipe recipe) {
        var outputs = recipe.getOtherStacks();
        float chance = outputs.size() > 1 ? (100f / (outputs.size() - 1)) : 100f;
        DecimalFormat df = new DecimalFormat("0.##");
        String chanceStr = df.format(chance) + "%";

        StringBuilder text = new StringBuilder(I18n.format(Bewitched.MODID + ".nei.mutation.chance", chanceStr));
        if (recipe.poolType == MutationPoolType.FLOWER_POT) {
            text.append('\n').append(I18n.format(Bewitched.MODID + ".nei.mutation.flower_pot"));
        }

        int y = 122;
        for (String line : Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text.toString(), 150)) {
            GuiDraw.drawString(line, 2, y, 0x000000, false);
            y += 11;
        }
    }

    /**
     * Adds a cached recipe entry for each MutationPoolType when the requested overlay ID targets this handler; otherwise delegates to the superclass.
     *
     * @param outputId the overlay identifier to load recipes for
     * @param results  additional context or parameters supplied by the caller (ignored by this implementation)
     */
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getOverlayIdentifier()) && this.getClass() == MutationNEIHandler.class) {
            for (var pool : MutationPoolType.values()) {
                this.arecipes.add(new CachedMutationRecipe(pool));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    /**
     * Adds cached mutation recipes for each mutation pool that can produce the given output.
     *
     * For each MutationPoolType, if any member's output stack is equal to the supplied `result`,
     * a corresponding CachedMutationRecipe for that pool is added to `arecipes`. Comparison is
     * performed using the same-stack equality used by the registry.
     *
     * @param result the output ItemStack to match against mutation members' outputs
     */
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (var pool : MutationPoolType.values()) {
            for (var member : MutationRegistry.getMembers(pool)) {
                if (ItemStackHelper.areStacksSame(member.output().asStack(), result)) {
                    this.arecipes.add(new CachedMutationRecipe(pool));
                    break;
                }
            }
        }
    }

    /**
     * Adds NEI recipes for every mutation pool that uses the given item as a catalyst or a member.
     *
     * @param ingredient the item stack to match against mutation pool catalysts or members; when a match is found a CachedMutationRecipe for that pool is added to `arecipes`
     */
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (var pool : MutationPoolType.values()) {
            if (MutationRegistry.isCatalyst(pool, ingredient) || MutationRegistry.isMember(pool, ingredient)) {
                this.arecipes.add(new CachedMutationRecipe(pool));
            }
        }
    }

    public class CachedMutationRecipe extends CachedRecipe {

        private final PositionedStack catalyst;
        private final List<PositionedStack> outputs = new ArrayList<>();
        private final MutationPoolType poolType;

        /**
         * Creates a cached mutation recipe view for the specified mutation pool.
         *
         * Builds the positioned catalyst stack from the pool's registered catalysts and populates
         * positioned output stacks in a 9-column grid based on the pool's registered mutation members.
         *
         * @param poolType the mutation pool type to build the cached recipe for
         */
        public CachedMutationRecipe(MutationPoolType poolType) {
            this.poolType = poolType;
            List<ItemStack> catalysts = MutationRegistry.getCatalysts(poolType);
            List<Mutation> mutations = MutationRegistry.getMembers(poolType);

            this.catalyst = new PositionedStack(new ArrayList<>(catalysts), 74, 4);
            for (int i = 0; i < mutations.size(); i++) {
                Mutation mutation = mutations.get(i);
                ItemStack output = mutation.output().asStack();

                int col = i % 9;
                int row = i / 9;

                this.outputs.add(new PositionedStack(
                    output,
                    3 + 18 * col,
                    45 + 18 * row
                ));
            }
        }

        /**
         * The catalyst PositionedStack used as the recipe's ingredient.
         *
         * @return the `PositionedStack` representing the catalyst ingredient
         */
        @Override
        public PositionedStack getIngredient() {
            return this.catalyst;
        }

        /**
         * Provides the primary result stack for this cached recipe.
         *
         * @return the result PositionedStack, or {@code null} when there is no single primary result
         */
        @Override
        public PositionedStack getResult() {
            return null;
        }

        /**
         * Provides the positioned output stacks for this cached mutation recipe.
         *
         * @return the list of positioned output stacks shown as possible mutation results
         */
        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.outputs;
        }

        /**
         * Get the single ingredient (the catalyst) used by this cached mutation recipe.
         *
         * @return a list containing the catalyst {@code PositionedStack}
         */
        @Override
        public List<PositionedStack> getIngredients() {
            return Collections.singletonList(this.catalyst);
        }
    }
}
