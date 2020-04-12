package info.jbcs.minecraft.vending.tileentity;

import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.inventory.InfiniteVendingMachineContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;

public class InfiniteVendingMachineBlockEntity extends VendingMachineBlockEntity {

	@Override
	public boolean isInfinite(){
		return true;
	}

	@Override
	public boolean vendingMachineHasRequiredItems(PlayerEntity player) {
		return true;
	}

	@Override
	public void removeSoldItems() {
	}

	public InfiniteVendingMachineBlockEntity() {
		super(Vending.Objects.infinite_vending_machine);
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_) {
		return new InfiniteVendingMachineContainer<>(id, playerInventory, pos);
	}


    /*
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? LazyOptional.of(() -> inventory).cast() : super.getCapability(capability, facing);
    }*/
}

