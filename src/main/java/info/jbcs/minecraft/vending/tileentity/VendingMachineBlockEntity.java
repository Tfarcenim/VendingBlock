package info.jbcs.minecraft.vending.tileentity;

import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.inventory.InfiniteVendingMachineContainer;
import info.jbcs.minecraft.vending.inventory.VendingMachineContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VendingMachineBlockEntity extends InfiniteVendingMachineBlockEntity {

	public ItemStackHandler inventory = new TileStackHandler(18);

	@Override
	public boolean isInfinite(){
		return false;
	}

	@Override
	public boolean vendingMachineHasRequiredItems(PlayerEntity player) {
		ItemStack required = selling.getStackInSlot(0);
		ItemStackHandler temp = new ItemStackHandler(1);

		for (int i = 9; i < 18; i++) {
			ItemStack extracted = inventory.extractItem(i, required.getCount(), true);
			if (!extracted.isEmpty()) {
				temp.insertItem(0, extracted, false);
				if (temp.getStackInSlot(0).getCount() >= required.getCount()) return true;
				break;
			}
		}
		player.sendMessage(new TranslationTextComponent("text.vending.out_of_stock"), Util.DUMMY_UUID);
		return false;
	}

	public void removeSoldItems() {
		ItemStack required = selling.getStackInSlot(0);
		int left = required.getCount();
		for (int i = 9; i < 18; i++) {
			ItemStack extracted = inventory.extractItem(i, required.getCount(), true);
			if (!extracted.isEmpty()) {
				if (extracted.isItemEqual(required)) {
					left -= extracted.getCount();
					inventory.extractItem(i, required.getCount(), false);
					if (left == 0) return;
				}
			}
		}
	}

	public void process(PlayerEntity player, Direction face) {
		super.process(player, face);
		removeSoldItems();
		addBoughtItems();
	}

	public void addBoughtItems() {
		ItemStack bought = buying.getStackInSlot(0);
		ItemStack rem = bought.copy();
		for (int i = 0; i < 27;i++) {
			rem = inventory.insertItem(i,rem,false);
			if (rem.isEmpty())break;
		}
	}

	public VendingMachineBlockEntity() {
		super(Vending.Objects.vending_machine);
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_) {
		return new VendingMachineContainer(id, playerInventory, pos);
	}

	@Override
	public void read(BlockState state,CompoundNBT nbttagcompound) {
		super.read(state,nbttagcompound);
		inventory.deserializeNBT(nbttagcompound.getCompound("inv"));
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		nbt.put("inv", inventory.serializeNBT());
		return super.write(nbt);
	}

	/*
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? LazyOptional.of(() -> inventory).cast() : super.getCapability(capability, facing);
    }*/
}

