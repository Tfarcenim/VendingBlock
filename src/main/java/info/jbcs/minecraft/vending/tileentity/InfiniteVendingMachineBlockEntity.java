package info.jbcs.minecraft.vending.tileentity;

import info.jbcs.minecraft.vending.Utils;
import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.inventory.InfiniteVendingMachineContainer;
import info.jbcs.minecraft.vending.inventory.VendingMachineContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class InfiniteVendingMachineBlockEntity extends TileEntity implements INamedContainerProvider {

	public ItemStackHandler selling = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			markDirty();
		}
	};
	public ItemStackHandler buying = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			markDirty();
		}
	};

	public boolean isInfinite() {
		return true;
	}

	private UUID ownerUUID;
	private ITextComponent ownerName;

	public InfiniteVendingMachineBlockEntity() {
		super(Vending.Objects.infinite_vending_machine);
	}

	public InfiniteVendingMachineBlockEntity(TileEntityType<?> type) {
		super(type);
	}

	public UUID getOwnerUUID() {
		return ownerUUID != null ? ownerUUID : Util.DUMMY_UUID;
	}

	public boolean isOwner(PlayerEntity player) {
		return ownerUUID.equals(PlayerEntity.getUUID(player.getGameProfile()));
	}

	public void setOwnerUUID(UUID name) {
		ownerUUID = name;
		ownerName = world.getServer().getPlayerList().getPlayerByUUID(ownerUUID).getName();
	}

	@Override
	public void read(BlockState state, CompoundNBT nbttagcompound) {

		buying.deserializeNBT(nbttagcompound.getCompound("buy"));
		selling.deserializeNBT(nbttagcompound.getCompound("sell"));

		ownerUUID = nbttagcompound.getUniqueId("uuid");
		ownerName = new StringTextComponent(nbttagcompound.getString("name"));
		super.read(state,nbttagcompound);
	}

	@Override
	@Nonnull
	public CompoundNBT write(CompoundNBT nbt) {

		nbt.put("buy", buying.serializeNBT());
		nbt.put("sell", selling.serializeNBT());

		nbt.putUniqueId("uuid", ownerUUID);
		nbt.putString("name", ownerName.getUnformattedComponentText());
		return super.write(nbt);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Utils.markBlockForUpdate(world, pos);
	}

	@Override
	@Nonnull
	public CompoundNBT getUpdateTag() {
		CompoundNBT updateTag = super.getUpdateTag();
		write(updateTag);
		return updateTag;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT var1 = new CompoundNBT();
		this.write(var1);
		return new SUpdateTileEntityPacket(pos, 1, var1);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(getBlockState(),pkt.getNbtCompound());
	}

	public void vend(PlayerEntity player, Direction face, boolean simulate) {
		if (vendingMachineHasRequiredItems(player) && playerHasRequiredItems(player)) {
			process(player,face);
		}
	}

	public boolean playerHasRequiredItems(PlayerEntity player) {
		InvWrapper wrapper = new InvWrapper(player.inventory);
		ItemStack required = buying.getStackInSlot(0);
		ItemStackHandler temp = new ItemStackHandler(1);

		for (int i = 0; i < 36; i++) {
			ItemStack extracted = wrapper.extractItem(i, required.getCount(), true);
			if (extracted.isItemEqual(required)) {
				temp.insertItem(0, extracted, false);
				if (temp.getStackInSlot(0).getCount() >= required.getCount()) return true;
			}
		}
		player.sendMessage(new TranslationTextComponent("text.vending.insufficient_funds"), Util.DUMMY_UUID);
		return false;
	}

	public boolean vendingMachineHasRequiredItems(PlayerEntity player){
		return true;
	}

	public void process(PlayerEntity player, Direction face) {
		removeItemsFromPlayer(player);
		vendItems(player,face);
	}

	public void vendItems(PlayerEntity player, Direction face){
		ItemStack sold = selling.getStackInSlot(0);
		ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), sold.copy());
		Utils.throwAtPlayer(player,face,itemEntity,pos);
	}

	public void removeItemsFromPlayer(PlayerEntity player) {
		InvWrapper wrapper = new InvWrapper(player.inventory);
		ItemStack required = buying.getStackInSlot(0);

		int left = required.getCount();
		for (int i = 0; i < 36; i++) {
			ItemStack extracted = wrapper.extractItem(i, required.getCount(), true);
			if (!extracted.isEmpty()) {
				if (extracted.isItemEqual(required)) {
					left -= extracted.getCount();
					wrapper.extractItem(i, required.getCount(), false);
					if (left == 0) return;
				}
			}
		}
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.vending.vending_machine");
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_) {
		return new InfiniteVendingMachineContainer(id, playerInventory, pos);
	}

	public ITextComponent getOwnerName() {
		return ownerName;
	}

	protected class TileStackHandler extends ItemStackHandler {

		protected TileStackHandler(int slots) {
			super(slots);
		}

		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			markDirty();
		}
	}

    /*
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? LazyOptional.of(() -> inventory).cast() : super.getCapability(capability, facing);
    }*/
}

