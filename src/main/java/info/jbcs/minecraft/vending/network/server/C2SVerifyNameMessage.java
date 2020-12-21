package info.jbcs.minecraft.vending.network.server;

import info.jbcs.minecraft.vending.init.VendingItems;
import info.jbcs.minecraft.vending.inventory.WrenchContainer;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SVerifyNameMessage {

    private String name;

    @SuppressWarnings("unused")
    public C2SVerifyNameMessage() {
    }

    public C2SVerifyNameMessage(PacketBuffer buffer) {
        name = buffer.readString(Short.MAX_VALUE);
    }

    public C2SVerifyNameMessage(String name) {
        this.name = name;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeString(name);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (!player.abilities.isCreativeMode || player.inventory.getCurrentItem().getItem() != VendingItems.WRENCH)
                return;

            Container container = player.openContainer;

            if (container instanceof WrenchContainer) {
                ServerPlayerEntity newOwner = player.getServer().getPlayerList().getPlayerByUsername(name);
                if (newOwner == null){
                    player.sendMessage(new StringTextComponent("Invalid Name"), Util.DUMMY_UUID);
                } else {
                    UUID uuid = newOwner.getUniqueID();
                    ((InfiniteVendingMachineBlockEntity)((WrenchContainer)container).blockEntity).setOwnerUUID(uuid);
                }
            }
        });
    }
}
