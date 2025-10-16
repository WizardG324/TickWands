package com.wizardg.tickwands.item;

import com.wizardg.tickwands.TickWands;
import com.wizardg.tickwands.Config;
import com.wizardg.tickwands.item.custom.TickWand;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TickWands.MODID);

    public static final DeferredItem<Item> BASIC_ACCEL_WAND = ITEMS.register("basic_tick_wand",
            () -> new TickWand(new Item.Properties()
                    .setNoRepair()
                    .stacksTo(1), Config.BASIC_WAND_COOLDOWN, false));

    public static final DeferredItem<Item> TIME_SHARD = ITEMS.register("time_shard",
            () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
