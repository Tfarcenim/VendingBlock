package info.jbcs.minecraft.vending.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import info.jbcs.minecraft.vending.Utils;
import info.jbcs.minecraft.vending.inventory.WrenchContainer;
import info.jbcs.minecraft.vending.network.PacketHandler;
import info.jbcs.minecraft.vending.network.server.C2SVerifyNameMessage;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

public class WrenchScreen extends ContainerScreen<WrenchContainer> implements IContainerListener {
	private TextFieldWidget input;

	public WrenchScreen(WrenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(MatrixStack matrices, int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground(matrices);
		super.render(matrices,p_render_1_, p_render_2_, p_render_3_);
		RenderSystem.disableBlend();
		this.input.render(matrices,p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredTooltip(matrices,p_render_1_, p_render_2_);
	}

	private void onEdited(String text) {
		//if (!isValid(this.input.getText())){

		//}
	}

	public boolean isValid(String name) {
		try {

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrices,int a, int b) {
		FontRenderer font = this.font;
		font.drawString(matrices,net.minecraft.client.resources.I18n.format("gui.vending.change_owner").trim(), 21, 37, 0x404040);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 *
	 * @param partialTicks
	 * @param mouseX
	 * @param mouseY
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrices,float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Utils.bind("vending:textures/wrench-gui.png");
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		int x = 25;
		int y = 48;
		int x1 = 134;
		int y1 = 30;
		this.blit(matrices,i, j, 0, 0, this.xSize, this.ySize);
		fill(matrices,i+x,j+y,i+126+x,j+12+y,0xff000000);
	}
	@Override
	protected void init() {
		super.init();

		this.minecraft.keyboardListener.enableRepeatEvents(true);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.input = new TextFieldWidget(this.font, i + 29, j + 50, 120, 12,new TranslationTextComponent("container.repair"));
		this.input.setCanLoseFocus(false);
		this.input.changeFocus(true);
		this.input.setTextColor(-1);
		this.input.setDisabledTextColour(-1);
		this.input.setEnableBackgroundDrawing(false);
		this.input.setMaxStringLength(35);
		this.input.setResponder(this::onEdited);
		this.children.add(this.input);
		this.container.addListener(this);
		this.setFocusedDefault(this.input);


		this.addButton(new Button(guiLeft + 62, guiTop + 100 ,30,20,new StringTextComponent("Save"),
						b -> {
							PacketHandler.dispatcher.sendToServer(new C2SVerifyNameMessage(input.getText()));
						}));

	}

	/**
	 * update the crafting window inventory with the items in the list
	 *
	 * @param containerToSend
	 * @param itemsList
	 */
	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {

	}

	/**
	 * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
	 * contents of that slot.
	 *
	 * @param containerToSend
	 * @param slotInd
	 * @param stack
	 */
	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {

	}

	/**
	 * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
	 * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
	 * value. Both are truncated to shorts in non-local SMP.
	 *
	 * @param containerIn
	 * @param varToUpdate
	 * @param newValue
	 */
	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

	}

	public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (keyCode == GLFW_KEY_ESCAPE) {
			this.minecraft.player.closeScreen();
		}

		if (keyCode == GLFW_KEY_TAB) {
			//getSuggestions();
		}

		return this.input.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_) || this.input.canWrite() || super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
	}

}
