package com.dipo33.bewitched.integration.nei;

import org.lwjgl.opengl.GL11;

import com.dipo33.bewitched.Bewitched;
import com.dipo33.bewitched.api.mutation.Mutation;
import com.dipo33.bewitched.api.mutation.MutationPoolType;
import com.dipo33.bewitched.api.mutation.MutationRegistry;
import com.dipo33.bewitched.init.BewitchedItems;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class MutationNEIHandler extends TemplateRecipeHandler {

    @Override
    public String getRecipeName() {
        return I18n.format(Bewitched.MODID + ".nei.mutation");
    }

    @Override
    public String getGuiTexture() {
        return Bewitched.MODID + ":textures/gui/nei_mutation.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return null;
    }

    @Override
    public String getOverlayIdentifier() {
        return Bewitched.MODID + ".mutation";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(67, 22, 30, 20), this.getOverlayIdentifier()));
    }

    @Override
    public int getRecipeHeight(final int recipe) {
        return 150;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 144);
    }

    @Override
    public void drawExtras(final int recipeIdx) {
        var recipe = (CachedMutationRecipe) this.arecipes.get(recipeIdx);
        this.drawProgressBar(recipe);
        this.drawAdditionalText(recipe);
    }

    private void drawProgressBar(CachedMutationRecipe recipe) {
        var catalyst = recipe.getIngredient();
        if (catalyst.contains(BewitchedItems.MUTANDIS.get()) || !catalyst.contains(BewitchedItems.MUTANDIS_EXTREMIS.get())) {
            drawProgressBar(67, 22, 167, 0, 30, 20, 20, 1);
        } else {
            drawProgressBar(67, 22, 167, 21, 30, 20, 20, 1);
        }
    }

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

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (var pool : MutationPoolType.values()) {
            for (var member : MutationRegistry.getMembers(pool)) {
                if (areStacksSame(member.output().asStack(), result)) {
                    this.arecipes.add(new CachedMutationRecipe(pool));
                    break;
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (var pool : MutationPoolType.values()) {
            if (MutationRegistry.isCatalyst(pool, ingredient) || MutationRegistry.isMember(pool, ingredient)) {
                this.arecipes.add(new CachedMutationRecipe(pool));
            }
        }
    }

    private boolean areStacksSame(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (a.getItem() != b.getItem()) return false;
        return a.getItemDamage() == b.getItemDamage();
    }

    public class CachedMutationRecipe extends CachedRecipe {

        private final PositionedStack catalyst;
        private final List<PositionedStack> outputs = new ArrayList<>();
        private final MutationPoolType poolType;

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

        @Override
        public PositionedStack getIngredient() {
            return this.catalyst;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.outputs;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return Collections.singletonList(this.catalyst);
        }
    }
}
