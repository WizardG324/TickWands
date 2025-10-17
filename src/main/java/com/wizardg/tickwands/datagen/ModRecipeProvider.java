package com.wizardg.tickwands.datagen;

import com.wizardg.tickwands.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider  implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BASIC_TICK_WAND.get())
                .pattern(" RC")
                .pattern(" IR")
                .pattern("I  ")
                .define('C', Items.CLOCK).define('R', Items.REDSTONE).define('I', Items.IRON_INGOT)
                .unlockedBy("has_clock", has(Items.CLOCK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_TICK_WAND.get())
                .pattern(" TS")
                .pattern(" NT")
                .pattern("N  ")
                .define('T', ModItems.TIME_SHARD).define('S', Items.NETHER_STAR).define('N', Items.NETHERITE_INGOT)
                .unlockedBy("has_time_shard", has(ModItems.TIME_SHARD))
                .save(recipeOutput);
    }
}
