package com.wizardg.tickwands.block;

import com.wizardg.tickwands.TickWands;
import com.wizardg.tickwands.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(TickWands.MODID);

    public static final DeferredBlock<Block> TIME_END_ORE = registerBlock(
            "time_shard_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops().sound(SoundType.NETHER_GOLD_ORE))
    );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> returnBlock = BLOCKS.register(name, block);

        ModItems.ITEMS.register(name, () -> new BlockItem(returnBlock.get(), new Item.Properties()));

        return returnBlock;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
