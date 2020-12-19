package info.jbcs.minecraft.vending.inventory;

import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
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

public class VendingMachineContainer extends AbstractVendingMachineContainer {
    public final IInventory playerInventory;
    public final VendingMachineBlockEntity blockEntity;
    public int playerSlotsCount;

    public VendingMachineContainer(int id, PlayerInventory inv, BlockPos pos) {
        super(Vending.Objects.container_vending_machine, id);
        playerInventory = inv;
        blockEntity = (VendingMachineBlockEntity) inv.player.world.getTileEntity(pos);

        int startX = 8;
        int startY1 = 17;

        addSlot(new GhostSlot(blockEntity.buying, 0, startX + 28, 35));
        addSlot(new GhostSlot(blockEntity.selling, 0, startX + 28, 102));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new SlotItemHandler(blockEntity.inventory, y * 3 + x, 54 + startX + x * 18, startY1 + y * 18));
            }
        }

        for (int y = 3; y < 6; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new SlotItemHandler(blockEntity.inventory, y * 3 + x, 54 +startX + x * 18, startY1 + 13 + y * 18));
            }
        }

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
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
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
