package info.jbcs.minecraft.vending.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class AbstractVendingMachineContainer extends Container {

    protected AbstractVendingMachineContainer(@Nullable ContainerType<?> type, int id) {
        super(type, id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }


    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, PlayerEntity player) {
        ItemStack itemstack = ItemStack.EMPTY;
        PlayerInventory playerinventory = player.inventory;
        if (clickType == ClickType.QUICK_CRAFT) {
            int i1 = this.dragEvent;
            this.dragEvent = getDragEvent(dragType);
            if ((i1 != 1 || this.dragEvent != 2) && i1 != this.dragEvent) {
                this.resetDrag();
            } else if (playerinventory.getItemStack().isEmpty()) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(dragType);
                if (isValidDragMode(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot selectedSlot = this.inventorySlots.get(slotId);
                ItemStack mouseStack = playerinventory.getItemStack();
                if (selectedSlot != null && canAddItemToSlot(selectedSlot, mouseStack, true) &&
                        selectedSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(selectedSlot)) {
                    this.dragSlots.add(selectedSlot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack mouseStackCopy = playerinventory.getItemStack().copy();
                    int count = playerinventory.getItemStack().getCount();

                    for (Slot slot8 : this.dragSlots) {
                        ItemStack mouseStack = playerinventory.getItemStack();
                        if (slot8 != null && canAddItemToSlot(slot8, mouseStack, true) && slot8.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(slot8)) {
                            ItemStack mouseStackCopyCopy = mouseStackCopy.copy();
                            int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                            computeStackSize(this.dragSlots, this.dragMode, mouseStackCopyCopy, j3);
                            int k3 = Math.min(mouseStackCopyCopy.getMaxStackSize(), slot8.getItemStackLimit(mouseStackCopyCopy));
                            if (mouseStackCopyCopy.getCount() > k3) {
                                mouseStackCopyCopy.setCount(k3);
                            }

                            if (slot8 instanceof GhostSlot) {

                            } else {
                                count -= mouseStackCopyCopy.getCount() - j3;
                            }
                            slot8.putStack(mouseStackCopyCopy);
                        }
                    }

                    mouseStackCopy.setCount(count);
                    playerinventory.setItemStack(mouseStackCopy);
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!playerinventory.getItemStack().isEmpty()) {
                    if (dragType == 0) {
                        player.dropItem(playerinventory.getItemStack(), true);
                        playerinventory.setItemStack(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.dropItem(playerinventory.getItemStack().split(1), true);
                    }
                }
            } else if (clickType == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot5 = this.inventorySlots.get(slotId);
                if (slot5 == null || !slot5.canTakeStack(player)) {
                    return ItemStack.EMPTY;
                }

                for (ItemStack itemstack8 = this.transferStackInSlot(player, slotId); !itemstack8.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack8); itemstack8 = this.transferStackInSlot(player, slotId)) {
                    itemstack = itemstack8.copy();
                }
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot clickedSlot = this.inventorySlots.get(slotId);
                if (clickedSlot != null) {
                    ItemStack slotStack = clickedSlot.getStack();
                    ItemStack mouseStack = playerinventory.getItemStack();
                    if (!slotStack.isEmpty()) {
                        itemstack = slotStack.copy();
                    }

                    if (slotStack.isEmpty()) {
                        if (!mouseStack.isEmpty() && clickedSlot.isItemValid(mouseStack)) {
                            int j2 = dragType == 0 ? mouseStack.getCount() : 1;
                            if (j2 > clickedSlot.getItemStackLimit(mouseStack)) {
                                j2 = clickedSlot.getItemStackLimit(mouseStack);
                            }
                            if (clickedSlot instanceof GhostSlot) {
                                ItemStack stack = mouseStack.copy();
                                stack.setCount(1);
                                clickedSlot.putStack(stack);
                            } else {
                                clickedSlot.putStack(mouseStack.split(j2));
                            }
                        }
                    } else if (clickedSlot.canTakeStack(player)) {
                        if (mouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                clickedSlot.putStack(ItemStack.EMPTY);
                                playerinventory.setItemStack(ItemStack.EMPTY);
                            } else {
                                int k2 = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                                if (clickedSlot instanceof GhostSlot) {
                                    clickedSlot.decrStackSize(1);
                                } else {
                                    playerinventory.setItemStack(clickedSlot.decrStackSize(k2));
                                }
                                if (slotStack.isEmpty()) {
                                    clickedSlot.putStack(ItemStack.EMPTY);
                                }

                                clickedSlot.onTake(player, playerinventory.getItemStack());
                            }
                        } else if (clickedSlot.isItemValid(mouseStack)) {
                            if (areItemsAndTagsEqual(slotStack, mouseStack)) {
                                if (clickedSlot instanceof GhostSlot) {

                                } else {
                                    int l2 = dragType == 0 ? mouseStack.getCount() : 1;
                                    if (l2 > clickedSlot.getItemStackLimit(mouseStack) - slotStack.getCount()) {
                                        l2 = clickedSlot.getItemStackLimit(mouseStack) - slotStack.getCount();
                                    }

                                    if (l2 > mouseStack.getMaxStackSize() - slotStack.getCount()) {
                                        l2 = mouseStack.getMaxStackSize() - slotStack.getCount();
                                    }
                                    mouseStack.shrink(l2);
                                    slotStack.grow(l2);
                                }
                            } else if (mouseStack.getCount() <= clickedSlot.getItemStackLimit(mouseStack)) {
                                if (clickedSlot instanceof GhostSlot) {
                                    ItemStack stack = mouseStack.copy();
                                    stack.setCount(1);
                                    clickedSlot.putStack(stack);
                                } else {
                                    clickedSlot.putStack(mouseStack);
                                    playerinventory.setItemStack(slotStack);
                                }
                            } else if (clickedSlot instanceof GhostSlot) {
                                ItemStack stack = mouseStack.copy();
                                stack.setCount(1);
                                clickedSlot.putStack(stack);
                            }
                        } else if (mouseStack.getMaxStackSize() > 1 && areItemsAndTagsEqual(slotStack, mouseStack) && !slotStack.isEmpty()) {
                            int i3 = slotStack.getCount();
                            if (i3 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                                mouseStack.grow(i3);
                                slotStack = clickedSlot.decrStackSize(i3);
                                if (slotStack.isEmpty()) {
                                    clickedSlot.putStack(ItemStack.EMPTY);
                                }

                                clickedSlot.onTake(player, playerinventory.getItemStack());
                            }
                        }
                    }

                    clickedSlot.onSlotChanged();
                }
            }
        } else if (clickType == ClickType.SWAP) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack playerStack = playerinventory.getStackInSlot(dragType);
            ItemStack slotStack = slot.getStack();
            if (!playerStack.isEmpty() || !slotStack.isEmpty()) {
                if (playerStack.isEmpty()) {
                    if (slot.canTakeStack(player)) {
                        if (slot instanceof GhostSlot) {
                            slot.putStack(ItemStack.EMPTY);
                        } else {
                            playerinventory.setInventorySlotContents(dragType, slotStack);
                            slot.putStack(ItemStack.EMPTY);
                            slot.onTake(player, slotStack);
                        }
                    }
                } else if (slotStack.isEmpty()) {
                    if (slot.isItemValid(playerStack)) {
                        int i = slot.getItemStackLimit(playerStack);
                        if (playerStack.getCount() > i) {
                            slot.putStack(playerStack.split(i));
                        } else {
                            if (slot instanceof GhostSlot) {
                                slot.putStack(playerStack);
                            } else {
                                slot.putStack(playerStack);
                                playerinventory.setInventorySlotContents(dragType, ItemStack.EMPTY);
                            }
                        }
                    }
                } else if (slot.canTakeStack(player) && slot.isItemValid(playerStack)) {
                    int l1 = slot.getItemStackLimit(playerStack);
                    if (playerStack.getCount() > l1) {
                        slot.putStack(playerStack.split(l1));
                        slot.onTake(player, slotStack);
                        if (!playerinventory.addItemStackToInventory(slotStack)) {
                            player.dropItem(slotStack, true);
                        }
                    } else {
                        if (slot instanceof GhostSlot) {
                            slot.putStack(playerStack);
                        } else {
                            slot.putStack(playerStack);
                            playerinventory.setInventorySlotContents(dragType, slotStack);
                            slot.onTake(player, slotStack);
                        }
                    }
                }
            }
        } else if (clickType == ClickType.CLONE && player.abilities.isCreativeMode && playerinventory.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot4 = this.inventorySlots.get(slotId);
            if (slot4 != null && slot4.getHasStack()) {
                ItemStack itemstack7 = slot4.getStack().copy();
                itemstack7.setCount(itemstack7.getMaxStackSize());
                playerinventory.setItemStack(itemstack7);
            }
        } else if (clickType == ClickType.THROW && playerinventory.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot3 = this.inventorySlots.get(slotId);
            if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(player)) {
                ItemStack itemstack6 = slot3.decrStackSize(dragType == 0 ? 1 : slot3.getStack().getCount());
                slot3.onTake(player, itemstack6);
                player.dropItem(itemstack6, true);
            }
        } else if (clickType == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot2 = this.inventorySlots.get(slotId);
            ItemStack mouseStack = playerinventory.getItemStack();
            if (!mouseStack.isEmpty() && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
                int j1 = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                int i2 = dragType == 0 ? 1 : -1;

                for (int j = 0; j < 2; ++j) {
                    for (int k = j1; k >= 0 && k < this.inventorySlots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); k += i2) {
                        Slot slot1 = this.inventorySlots.get(k);
                        if (slot1.getHasStack() && canAddItemToSlot(slot1, mouseStack, true) && slot1.canTakeStack(player) && this.canMergeSlot(mouseStack, slot1)) {
                            ItemStack itemstack3 = slot1.getStack();
                            if (j != 0 || itemstack3.getCount() != itemstack3.getMaxStackSize()) {
                                int l = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack3.getCount());
                                ItemStack itemstack4 = slot1.decrStackSize(l);
                                mouseStack.grow(l);
                                if (itemstack4.isEmpty()) {
                                    slot1.putStack(ItemStack.EMPTY);
                                }

                                slot1.onTake(player, itemstack4);
                            }
                        }
                    }
                }
            }

            this.detectAndSendChanges();
        }

        return itemstack;
    }

}
