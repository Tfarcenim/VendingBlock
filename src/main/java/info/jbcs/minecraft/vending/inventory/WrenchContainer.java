package info.jbcs.minecraft.vending.inventory;

import info.jbcs.minecraft.vending.Vending;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class WrenchContainer extends Container {

	private int playerSlotsCount;

	public TileEntity blockEntity;

	public WrenchContainer(int id, PlayerInventory inv, BlockPos pos) {
		super(Vending.Objects.container_wrench, id);
		blockEntity = inv.player.world.getTileEntity(pos);
	}

	/**
	 * Determines whether supplied player can use this container
	 *
	 * @param playerIn
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
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
