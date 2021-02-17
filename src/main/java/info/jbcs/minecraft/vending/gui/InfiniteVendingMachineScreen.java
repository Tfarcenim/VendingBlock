package info.jbcs.minecraft.vending.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import info.jbcs.minecraft.vending.Utils;
import info.jbcs.minecraft.vending.inventory.InfiniteVendingMachineContainer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

public class InfiniteVendingMachineScreen extends ContainerScreen<InfiniteVendingMachineContainer> {

    public InfiniteVendingMachineScreen(InfiniteVendingMachineContainer containerVendingMachine,
																				PlayerInventory inventoryplayer, ITextComponent title) {
        super(containerVendingMachine,inventoryplayer,title);
        ySize += 67;
    }

    @Override
    public void init() {
        super.init();
        /*buttons.clear();
        LockIconButton button = new LockIconButton(guiLeft + 7, guiTop + 63, b -> {
            C2SMessageLock msg = new C2SMessageLock();

            PacketHandler.dispatcher.sendToServer(msg);
            ((LockIconButton)b).setLocked(!container.blockEntity.isOpen());
        });
        button.setLocked(!container.blockEntity.isOpen());
        buttons.add(button);*/
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack,int a, int b) {
        FontRenderer font = this.font;
        font.drawString(stack, I18n.format("gui.vending.sell").trim(), 12, 87, 0x404040);
        font.drawString(stack, I18n.format("gui.vending.buy").trim(), 12, 20, 0x404040);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack,float f, int a, int b) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Utils.bind("vending:textures/infinite-vending-gui.png");
        blit(stack,(width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
    }

    @Override
    public void onClose() {
    }
    
    @Override
    public void render(MatrixStack stack,int mouseX, int mouseY, float partialTicks){
        renderBackground(stack);
        super.render(stack,mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack,mouseX, mouseY);
    }
}
