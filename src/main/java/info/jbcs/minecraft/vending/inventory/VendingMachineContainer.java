package info.jbcs.minecraft.vending.inventory;

import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.tileentity.VendingMachineBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class VendingMachineContainer<T extends VendingMachineBlockEntity> extends Container {
    public final IInventory playerInventory;
    public final T blockEntity;
    public int playerSlotsCount;

    public VendingMachineContainer(int id, PlayerInventory inv, BlockPos pos) {
        super(Vending.Objects.container_vending_machine, id);
        playerInventory = inv;
        blockEntity = (T)inv.player.world.getTileEntity(pos);

        int startX = 8;
        int startY1 = 17;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new SlotItemHandlerUnconditioned(blockEntity.inventory, y * 3 + x, 54 + startX + x * 18, startY1 + y * 18));
            }
        }

        for (int y = 3; y < 6; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new SlotItemHandlerUnconditioned(blockEntity.inventory, y * 3 + x, 54 +startX + x * 18, startY1 + 13 + y * 18));
            }
        }

        addSlot(new SlotItemHandler(blockEntity.buying, 0, startX + 29, 35));
        addSlot(new SlotItemHandler(blockEntity.selling, 0, startX + 29, 102));

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
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity entityplayer, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i < playerSlotsCount) {
                if (!mergeItemStack(itemstack1, playerSlotsCount, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!mergeItemStack(itemstack1, 0, playerSlotsCount, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
