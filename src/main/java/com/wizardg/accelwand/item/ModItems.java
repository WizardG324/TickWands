package com.wizardg.accelwand.item;

import com.wizardg.accelwand.AccelerationWand;
import com.wizardg.accelwand.Config;
import com.wizardg.accelwand.item.custom.AccelWand;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AccelerationWand.MODID);

    // for now
    public static final DeferredItem<Item> BASIC_ACCEL_WAND = ITEMS.register("basic_accel_wand",
            () -> new AccelWand(new Item.Properties()
                    .setNoRepair()
                    .stacksTo(1), Config.BASIC_WAND_COOLDOWN));

    // for later
    public static final DeferredItem<Item> ADVANCED_ACCEL_WAND = ITEMS.register("advanced_accel_wand",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
