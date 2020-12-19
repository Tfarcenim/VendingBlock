package info.jbcs.minecraft.vending.network.server;

import info.jbcs.minecraft.vending.Utils;
import info.jbcs.minecraft.vending.init.VendingItems;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageWrench {
    private int x, y, z;
    private boolean infinite;
    private UUID ownerUUID;

    @SuppressWarnings("unused")
    public MessageWrench() {
    }

    public MessageWrench(PacketBuffer buffer) {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        infinite = buffer.readBoolean();
        ownerUUID = buffer.readUniqueId();
    }

    public MessageWrench(TileEntity tileEntityVendingMachine, boolean infinite, UUID ownerUUID) {
        InfiniteVendingMachineBlockEntity entity = (InfiniteVendingMachineBlockEntity) tileEntityVendingMachine;
        BlockPos blockPos = entity.getPos();
        x = blockPos.getX();
        y = blockPos.getY();
        z = blockPos.getZ();
        this.infinite = infinite;
        this.ownerUUID = ownerUUID;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBoolean(infinite);
        buffer.writeUniqueId(ownerUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player.inventory.getCurrentItem().getItem() != VendingItems.WRENCH)
                return;
            TileEntity tileEntity = player.world.getTileEntity(new BlockPos(x, y, z));
            if (!(tileEntity instanceof InfiniteVendingMachineBlockEntity)) {
                player.connection.disconnect(new StringTextComponent("no cheating!"));
            return;
            }
            InfiniteVendingMachineBlockEntity entity = (InfiniteVendingMachineBlockEntity) tileEntity;
            entity.setOwnerUUID(ownerUUID);
            Utils.markBlockForUpdate(player.world, new BlockPos(x, y, z));
        });
    }
}
