package info.jbcs.minecraft.vending.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class GhostSlot extends SlotItemHandler {
	public GhostSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public void putStack(@Nonnull ItemStack stack) {
		super.putStack(stack);
	}

	@Override
	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
		return super.onTake(thePlayer, stack);
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int amount) {
		return super.decrStackSize(amount);
	}
}
