package com.wizardg.tickwands.item.custom;

import com.wizardg.tickwands.Config;
import com.wizardg.tickwands.item.ModComponents;
import com.wizardg.tickwands.network.payloads.ClientMessagePayload;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class TickWand extends Item {
    private final Supplier<Integer> abilityCooldown;
    private final boolean isAdvanced;
    // Make this dynamic later
    private static final int BASIC_WAND_COST = 250;
    private static final int BASIC_WAND_MAX_TICK = 60;
    private static final int BASIC_WAND_RANDOM_TICK = 9;

    private static final int ADVANCED_WAND_COST = 7500;
    private static final int ADVANCED_WAND_MAX_TICK = 120;
    private static final int ADVANCED_WAND_RANDOM_TICK = 4;

    private final RandomSource random = RandomSource.create();
    private static final Random RAND = new Random();

    public TickWand(Properties properties, Supplier<Integer> abilityCooldown, boolean isAdvanced) {
        super(properties);

        this.abilityCooldown = abilityCooldown;
        this.isAdvanced = isAdvanced;
    }

    public boolean getIsAdvanced() {
        return this.isAdvanced;
    }

    // This is the only way I can think of how to do this sadly
    public static void ErrorHandler(Player player, int id) {
        // Offhand
        if (id == 0) {
            player.displayClientMessage(Component.literal("Wand doesn't work in offhand!").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), true);
        }
        //Blacklisted block
        else if (id == 1) {
            player.displayClientMessage(Component.literal("This is a blacklisted block!").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), true);
        }
        //Insufficient energy
        else if (id == 2) {
            player.displayClientMessage(Component.literal("Insufficient Energy!").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), true);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        DecimalFormat format = new DecimalFormat("#,###");
        tooltipComponents.add(Component.translatable("tooltips.tickwands.wand_energy", format.format(getCurrentEnergy(stack))).withStyle(ChatFormatting.WHITE));
    }

    @Override
    @SuppressWarnings("unchecked")
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) return InteractionResult.PASS;

        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();
        ItemStack item = context.getItemInHand();

        Player player = context.getPlayer();

        if (player == null) {
            return InteractionResult.FAIL;
        }

        // Don't want this to be used by a fake player. No point for now
        if (player.isFakePlayer()) {
            return InteractionResult.PASS;
        }

        // Maybe someday different functionality in offhand?
        if (context.getHand() == InteractionHand.OFF_HAND) {
            PacketDistributor.sendToPlayer((ServerPlayer)player, new ClientMessagePayload(0));
            return InteractionResult.PASS;
        }

        String blockId = BuiltInRegistries.BLOCK.getKey(clickedBlock).toString();

        // Pass on blocks that we have blacklisted and render text saying that it's blacklisted.
        if (Config.WAND_BLOCK_BLACKLIST.get().contains(blockId)) {
            PacketDistributor.sendToPlayer((ServerPlayer)player, new ClientMessagePayload(1));
            return InteractionResult.PASS;
        }

        if (getCurrentEnergy(item) <= 0 && !player.isCreative()) {
            PacketDistributor.sendToPlayer((ServerPlayer)player, new ClientMessagePayload(2));
            return InteractionResult.PASS;
        }

        // Let the magic happen here
        BlockState state = level.getBlockState(context.getClickedPos());
        BlockEntity tileEntity = level.getBlockEntity(context.getClickedPos());

        int ticksToRun = isAdvanced ? (RAND.nextInt(BASIC_WAND_MAX_TICK, ADVANCED_WAND_MAX_TICK + 1)) :
                RAND.nextInt(5, BASIC_WAND_MAX_TICK);

        for (int i = 0; i < ticksToRun; i++) {
            if (tileEntity != null) {
                EntityBlock entity = (EntityBlock)state.getBlock();
                BlockEntityTicker<BlockEntity> ticker = entity.getTicker(level, state, (BlockEntityType<BlockEntity>)tileEntity.getType());

                if (ticker != null) {
                    ticker.tick(level, context.getClickedPos(), state, tileEntity);
                }
            } else if (state.isRandomlyTicking()) {
                if (isAdvanced) {
                    if (RAND.nextInt(ADVANCED_WAND_RANDOM_TICK) == 0) {
                        state.randomTick((ServerLevel)level, context.getClickedPos(), random);
                    }
                } else {
                    if (RAND.nextInt(BASIC_WAND_RANDOM_TICK) == 0) {
                        state.randomTick((ServerLevel)level, context.getClickedPos(), random);
                    }
                }
            } else {
                // Not a tickable block
                return InteractionResult.PASS;
            }
        }

        // Might make the advanced wand have a non-changeable 5 tick cooldown haven't decided yet
        if (abilityCooldown.get() > 0 && !isAdvanced) {
            player.getCooldowns().addCooldown(this, abilityCooldown.get());
        }

        if (!player.isCreative()) {
            if (isAdvanced) {
                float multiplier = RAND.nextFloat(3.0F, 9.0F);
                int energyCost = (int)(ADVANCED_WAND_COST * multiplier);

                removeEnergy(item, energyCost, false);
            } else {
                removeEnergy(item, BASIC_WAND_COST, false);
            }
        }

        return InteractionResult.SUCCESS;
    }

    /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }
    */

    // Removes the reequip animation whenever the item receives energy
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    // Grab the current energy of our wand.
    public int getCurrentEnergy(ItemStack item) {
        int value = item.getOrDefault(ModComponents.ENERGY_COMPONENT, 0);
        return Math.max(0, value);
    }

    // Don't believe this should/needs to be any higher. Could possibly change in the future.
    public int getMaxEnergy() {
        return isAdvanced ? 500000 : 10000;
    }

    private void removeEnergy(ItemStack item, int amount, boolean simulate) {
        IEnergyStorage energyStorage = item.getCapability(Capabilities.EnergyStorage.ITEM, null);

        if (energyStorage != null) {
            energyStorage.extractEnergy(amount, simulate);
        }
    }
}
