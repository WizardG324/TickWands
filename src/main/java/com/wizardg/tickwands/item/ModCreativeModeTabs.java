package com.wizardg.tickwands.item;

import com.wizardg.tickwands.TickWands;
import com.wizardg.tickwands.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TickWands.MODID);

    public static final Supplier<CreativeModeTab> ACCELERATION_WAND_TAB = CREATIVE_MODE_TAB.register("tickwands_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BASIC_TICK_WAND.get()))
                    .title(Component.translatable("creativetab.tickwands.tick_wands"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.BASIC_TICK_WAND);
                        output.accept(ModItems.ADVANCED_TICK_WAND);
                        output.accept(ModItems.TIME_SHARD);
                        output.accept(ModBlocks.TIME_END_ORE);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
