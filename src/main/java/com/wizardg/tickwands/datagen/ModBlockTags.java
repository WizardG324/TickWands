package com.wizardg.tickwands.datagen;

import com.wizardg.tickwands.TickWands;
import com.wizardg.tickwands.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends BlockTagsProvider {
    public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, TickWands.MODID);
    }

    //Credits: JDT
    private static TagKey<Block> neoForgeTag(String name) {
        return BlockTags.create(Identifier.fromNamespaceAndPath("c", name));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL).add(ModBlocks.TIME_END_ORE.get());
        tag(BlockTags.INCORRECT_FOR_GOLD_TOOL).add(ModBlocks.TIME_END_ORE.get());
        tag(BlockTags.INCORRECT_FOR_IRON_TOOL).add(ModBlocks.TIME_END_ORE.get());
        tag(BlockTags.INCORRECT_FOR_STONE_TOOL).add(ModBlocks.TIME_END_ORE.get());
        tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).add(ModBlocks.TIME_END_ORE.get());

        tag(Tags.Blocks.ORES).add(ModBlocks.TIME_END_ORE.get());
        tag(neoForgeTag("ores/time_shard")).add(ModBlocks.TIME_END_ORE.get());

        tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(ModBlocks.TIME_END_ORE.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TIME_END_ORE.get());

        tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED).add(ModBlocks.TIME_END_ORE.get());
    }
}
