package info.jbcs.minecraft.vending.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotItemHandlerUnconditioned extends SlotItemHandler {

  public SlotItemHandlerUnconditioned(AutomationSensitiveItemStackHandler inv, int index, int xPosition, int yPosition) {
    super(inv, index, xPosition, yPosition);
  }

  @Override
  public AutomationSensitiveItemStackHandler getItemHandler() {
    return (AutomationSensitiveItemStackHandler)super.getItemHandler();
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    if (stack.isEmpty() || !this.getItemHandler().canAccept(this.getSlotIndex(), stack, false)) return false;

    ItemStack currentStack = this.getItemHandler().getStackInSlot(this.getSlotIndex());
    this.getItemHandler().setStackInSlot(this.getSlotIndex(), ItemStack.EMPTY);
    ItemStack remainder = this.getItemHandler().insertItem(this.getSlotIndex(), stack, true, false);
    this.getItemHandler().setStackInSlot(this.getSlotIndex(), currentStack);
    return remainder.isEmpty() || remainder.getCount() < stack.getCount();
  }

  /**
   * Helper fnct to get the stack in the slot.
   */
  @Override
  @Nonnull
  public ItemStack getStack() {
    return this.getItemHandler().getStackInSlot(this.getSlotIndex());
  }

  @Override
  public void putStack(ItemStack stack) {
    this.getItemHandler().setStackInSlot(this.getSlotIndex(), stack);
    this.onSlotChanged();
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    ItemStack maxAdd = stack.copy();
    maxAdd.setCount(stack.getMaxStackSize());
    ItemStack currentStack = this.getItemHandler().getStackInSlot(this.getSlotIndex());
    this.getItemHandler().setStackInSlot(this.getSlotIndex(), ItemStack.EMPTY);
    ItemStack remainder = this.getItemHandler().insertItem(this.getSlotIndex(), maxAdd, true, false);
    this.getItemHandler().setStackInSlot(this.getSlotIndex(), currentStack);
    return stack.getMaxStackSize() - remainder.getCount();
  }

  @Override
  public boolean canTakeStack(PlayerEntity playerIn) {
    return !this.getItemHandler().extractItem(this.getSlotIndex(), 1, true, false).isEmpty();
  }

  @Override
  public ItemStack decrStackSize(int amount) {
    return this.getItemHandler().extractItem(this.getSlotIndex(), amount, false, false);
  }
}
