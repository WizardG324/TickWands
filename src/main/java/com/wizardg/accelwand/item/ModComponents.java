package com.wizardg.accelwand.item;

import com.mojang.serialization.Codec;
import com.wizardg.accelwand.AccelerationWand;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModComponents {
    //Create component register
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,
            AccelerationWand.MODID);

    //Create our custom energy component for our wands
    public static final Supplier<DataComponentType<Integer>> ENERGY_COMPONENT = COMPONENTS.register("energy",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build());

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }
}
