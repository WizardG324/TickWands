package com.wizardg.tickwands;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue BASIC_WAND_COOLDOWN = BUILDER
            .comment("The cooldown in ticks for the basic acceleration wand.")
            .defineInRange("cooldownTicks", 20, 0, 200);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> WAND_BLOCK_BLACKLIST = BUILDER
            .comment("Blocks that the wands will not accelerate.")
            .defineListAllowEmpty("blocks", List.of("minecraft:dirt"), () -> "", Config::validateBlockName);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateBlockName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(itemName));
    }
}
