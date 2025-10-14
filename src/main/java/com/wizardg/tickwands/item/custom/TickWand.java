package com.wizardg.tickwands.item.custom;

import com.wizardg.tickwands.Config;
import com.wizardg.tickwands.item.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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

    private final RandomSource random = RandomSource.create();
    private static final Random RAND = new Random();

    public TickWand(Properties properties, Supplier<Integer> abilityCooldown, boolean isAdvanced) {
        super(properties);

        this.abilityCooldown = abilityCooldown;
        this.isAdvanced = isAdvanced;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.translatable("tooltips.tickwands.wand_energy", getCurrentEnergy(stack)).withStyle(ChatFormatting.WHITE));
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
            //context.getPlayer().displayClientMessage(Component.literal("Wand Doesn't work in off-hand!").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), true);
            return InteractionResult.PASS;
        }

        String blockId = BuiltInRegistries.BLOCK.getKey(clickedBlock).toString();

        // Pass on blocks that we have blacklisted and render text saying that it's blacklisted.
        if (Config.WAND_BLOCK_BLACKLIST.get().contains(blockId)) {
            //context.getPlayer().displayClientMessage(Component.literal("This is a blacklisted block!").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), true);
            return InteractionResult.PASS;
        }

        if (getCurrentEnergy(item) <= 0 && !player.isCreative()) {
            //context.getPlayer().displayClientMessage(Component.literal("Not Enough Energy!").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), true);
            return InteractionResult.PASS;
        }

        // Let the magic happen here
        BlockState state = level.getBlockState(context.getClickedPos());
        BlockEntity tileEntity = level.getBlockEntity(context.getClickedPos());

        for (int i = 0; i < RAND.nextInt(5, BASIC_WAND_MAX_TICK); i++) {
            if (tileEntity != null) {
                EntityBlock entity = (EntityBlock)state.getBlock();
                BlockEntityTicker<BlockEntity> ticker = entity.getTicker(level, state, (BlockEntityType<BlockEntity>)tileEntity.getType());

                if (ticker != null) {
                    ticker.tick(level, context.getClickedPos(), state, tileEntity);
                }
            } else {
                if (RAND.nextInt(BASIC_WAND_RANDOM_TICK) == 0) {
                    state.randomTick((ServerLevel)level, context.getClickedPos(), random);
                }
            }
        }


        if (abilityCooldown.get() > 0) {
            player.getCooldowns().addCooldown(this, abilityCooldown.get());
        }

        if (!player.isCreative()) {
            removeEnergy(item, BASIC_WAND_COST, false);
        }

        return InteractionResult.SUCCESS;
    }

    /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }
    */

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
        return isAdvanced ? 100000 : 10000;
    }

    private void removeEnergy(ItemStack item, int amount, boolean simulate) {
        IEnergyStorage energyStorage = item.getCapability(Capabilities.EnergyStorage.ITEM, null);

        if (energyStorage != null) {
            energyStorage.extractEnergy(amount, simulate);
        }
    }
}
