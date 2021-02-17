package info.jbcs.minecraft.vending.inventory;

import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
import info.jbcs.minecraft.vending.tileentity.VendingMachineBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class InfiniteVendingMachineContainer extends AbstractVendingMachineContainer {
    public final IInventory playerInventory;
    public final InfiniteVendingMachineBlockEntity blockEntity;

    public InfiniteVendingMachineContainer(int id, PlayerInventory inv, BlockPos pos) {
        super(Vending.Objects.infinite_container_vending_machine, id);
        playerInventory = inv;
        blockEntity = (InfiniteVendingMachineBlockEntity) inv.player.world.getTileEntity(pos);

        int startX = 8;

        addSlot(new GhostSlot(blockEntity.buying, 0, startX + 71, 35));
        addSlot(new GhostSlot(blockEntity.selling, 0, startX + 71, 102));

        int startY = 151;

        for (int k = 0; k < 3; k++) {
            for (int j1 = 0; j1 < 9; j1++) {
                addSlot(new Slot(inv, j1 + k * 9 + 9, startX + j1 * 18, startY + k * 18));
            }
        }

        for (int l = 0; l < 9; l++) {
            addSlot(new Slot(inv, l, startX + l * 18, startY + 142 - 84));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity entityplayer) {
        return true;//blockEntity.isUsableByPlayer(entityplayer);
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            ItemStack itemstack = stackInSlot.copy();
            //filter slots
            if (index < 2) {
                slot.putStack(ItemStack.EMPTY);
                return ItemStack.EMPTY;
            } else {
                for (int i = 0 ; i < 2; i++) {
                    ItemStack existing = this.inventorySlots.get(i).getStack();
                    if (existing.isEmpty()) {
                        this.inventorySlots.get(i).putStack(itemstack);
                        break;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }
    
}
