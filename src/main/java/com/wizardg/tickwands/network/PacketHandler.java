package com.wizardg.tickwands.network;

import com.wizardg.tickwands.TickWands;
import com.wizardg.tickwands.network.packets.ClientMessagePacket;
import com.wizardg.tickwands.network.payloads.ClientMessagePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/*
* Credits: Direwolf20's JDT for how payloads/packets should be made
*/

public class PacketHandler {
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(TickWands.MODID);

        //Server payloads


        //Client Payloads
        registrar.playToClient(ClientMessagePayload.TYPE, ClientMessagePayload.STREAM_CODEC, ClientMessagePacket.get()::handle);
    }
}
