package info.jbcs.minecraft.vending.network.server;

import info.jbcs.minecraft.vending.Utils;
import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.inventory.VendingMachineContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SMessageLock {

    @SuppressWarnings("unused")
    public C2SMessageLock() {
    }

    public void write(PacketBuffer buffer) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {

    	ctx.get().enqueueWork(() -> {
				ServerPlayerEntity player = ctx.get().getSender();
				Container container = player.openContainer;
				boolean handled = false;
				if (container instanceof VendingMachineContainer) {
					VendingMachineContainer vendingContainer = (VendingMachineContainer)container;

					if (vendingContainer.blockEntity.getOwnerUUID().equals(player.getGameProfile().getId())) {
						//vendingContainer.blockEntity.toggleLock();
						Utils.markBlockForUpdate(player.world, vendingContainer.blockEntity.getPos());
						handled = true;
					}
				}
				if (!handled) Vending.logger.warn("seems like someone is trying to cheat: " +player.getName().getUnformattedComponentText());
			});
    	ctx.get().setPacketHandled(true);
    }
}
