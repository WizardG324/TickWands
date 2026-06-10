package com.wizardg.tickwands.datagen;

import com.wizardg.tickwands.TickWands;
import com.wizardg.tickwands.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, ModItems.BASIC_TICK_WAND.get())
                .pattern(" RC")
                .pattern(" IR")
                .pattern("I  ")
                .define('C', Items.CLOCK).define('R', Items.REDSTONE).define('I', Items.IRON_INGOT)
                .unlockedBy("has_clock", has(Items.CLOCK))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.ADVANCED_TICK_WAND.get())
                .pattern(" TS")
                .pattern(" NT")
                .pattern("N  ")
                .define('T', ModItems.TIME_SHARD).define('S', Items.NETHER_STAR).define('N', Items.NETHERITE_INGOT)
                .unlockedBy("has_time_shard", has(ModItems.TIME_SHARD))
                .save(output);
    }

    // The runner to add to the data generator
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "TickWands Recipes";
        }
    }
}
