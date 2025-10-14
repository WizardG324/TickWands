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

    // for now
    public static final DeferredItem<Item> BASIC_ACCEL_WAND = ITEMS.register("basic_tick_wand",
            () -> new TickWand(new Item.Properties()
                    .setNoRepair()
                    .stacksTo(1), Config.BASIC_WAND_COOLDOWN));

    // for later
    //public static final DeferredItem<Item> ADVANCED_ACCEL_WAND = ITEMS.register("advanced_tick_wand",
    //        () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
