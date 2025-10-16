package com.wizardg.tickwands;

import com.wizardg.tickwands.block.ModBlocks;
import com.wizardg.tickwands.item.ModComponents;
import com.wizardg.tickwands.item.ModCreativeModeTabs;
import com.wizardg.tickwands.item.ModItems;
import com.wizardg.tickwands.item.custom.TickWand;
import com.wizardg.tickwands.network.PacketHandler;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.ClientPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TickWands.MODID)
public class TickWands {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tickwands";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public TickWands(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (TickWands) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);
        ModComponents.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        // Register energy capabilities for our wand so it can accept energy from other mods
        modEventBus.addListener(this::onRegisterCapabilities);
        // Register our payloads
        modEventBus.addListener(PacketHandler::registerPayloads);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ModItems.ITEMS.getEntries().forEach(entry -> {
            Item item = entry.get();

            if (item instanceof TickWand wand) {
                if (!wand.getIsAdvanced()) {
                    event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) -> new ComponentEnergyStorage(stack, ModComponents.ENERGY_COMPONENT.get(),
                            wand.getMaxEnergy(), 500, 10000), item);
                } else {
                    event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) -> new ComponentEnergyStorage(stack, ModComponents.ENERGY_COMPONENT.get(),
                            wand.getMaxEnergy(), 50000), item);
                }
            }
        });
    }
}
