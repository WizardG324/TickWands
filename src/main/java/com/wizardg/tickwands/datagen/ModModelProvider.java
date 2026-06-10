package com.wizardg.tickwands.datagen;

import com.wizardg.tickwands.TickWands;
import com.wizardg.tickwands.block.ModBlocks;
import com.wizardg.tickwands.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, TickWands.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // items
        itemModels.generateFlatItem(ModItems.BASIC_TICK_WAND.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ADVANCED_TICK_WAND.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TIME_SHARD.get(), ModelTemplates.FLAT_ITEM);
        //blocks
        blockModels.createTrivialCube(ModBlocks.TIME_END_ORE.get());
    }
}
