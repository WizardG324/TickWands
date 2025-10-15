package com.wizardg.tickwands.network.payloads;

import com.wizardg.tickwands.TickWands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientMessagePayload(int errorId) implements CustomPacketPayload {
    public static final Type<ClientMessagePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TickWands.MODID, "client_message_packet"));

    public static final StreamCodec<FriendlyByteBuf, ClientMessagePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientMessagePayload::errorId,
            ClientMessagePayload::new
    );

    @Override
    public Type<ClientMessagePayload> type() {
        return TYPE;
    }
}
