package com.wizardg.tickwands.network.packets;

import com.wizardg.tickwands.item.custom.TickWand;
import com.wizardg.tickwands.network.payloads.ClientMessagePayload;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientMessagePacket {
    public static final ClientMessagePacket INSTANCE = new ClientMessagePacket();

    public static ClientMessagePacket get() {
        return INSTANCE;
    }

    public void handle(final ClientMessagePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
           var player = context.player();

            TickWand.ErrorHandler(player, payload.errorId());
        });
    }
}
