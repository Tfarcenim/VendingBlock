package info.jbcs.minecraft.vending;

import info.jbcs.minecraft.vending.block.VendingMachineBlock;
import info.jbcs.minecraft.vending.gui.InfiniteVendingMachineScreen;
import info.jbcs.minecraft.vending.gui.VendingMachineScreen;
import info.jbcs.minecraft.vending.gui.WrenchScreen;
import info.jbcs.minecraft.vending.init.VendingBlocks;
import info.jbcs.minecraft.vending.init.VendingItems;
import info.jbcs.minecraft.vending.init.VendingSoundEvents;
import info.jbcs.minecraft.vending.inventory.InfiniteVendingMachineContainer;
import info.jbcs.minecraft.vending.inventory.VendingMachineContainer;
import info.jbcs.minecraft.vending.inventory.WrenchContainer;
import info.jbcs.minecraft.vending.network.PacketHandler;
import info.jbcs.minecraft.vending.renderer.VendingMachineBlockEntityRenderer;
import info.jbcs.minecraft.vending.settings.Settings;
import info.jbcs.minecraft.vending.tileentity.VendingMachineBlockEntity;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static info.jbcs.minecraft.vending.settings.Settings.CLIENT_SPEC;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(Vending.MODID)

public class Vending {
    public static final String MODID = "vending";

    public static ItemGroup tabVending = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(VendingBlocks.STONE_VENDING_MACHINE);
        }
    };
    public static Settings settings;
    public static final Logger logger = LogManager.getLogger();

    public Vending() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        iEventBus.addListener(this::init);
        iEventBus.addListener(this::client);
        EVENT_BUS.addListener(this::mining);
        EVENT_BUS.addListener(this::breakBlock);
    }

    public void init(FMLCommonSetupEvent event) {
        PacketHandler.registerPackets();
    }

    public void client(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Objects.container_vending_machine, VendingMachineScreen::new);
        ScreenManager.registerFactory(Objects.infinite_container_vending_machine, InfiniteVendingMachineScreen::new);
        ScreenManager.registerFactory(Objects.container_wrench, WrenchScreen::new);

        RenderTypeLookup.setRenderLayer(VendingBlocks.STONE_VENDING_MACHINE, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(VendingBlocks.INFINITE_STONE_VENDING_MACHINE, RenderType.cutout());
        ClientRegistry.bindTileEntityRenderer(Objects.vending_machine, VendingMachineBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Objects.infinite_vending_machine, VendingMachineBlockEntityRenderer::new);

    }

    public void mining(PlayerEvent.BreakSpeed e){
        BlockPos pos = e.getPos();
        PlayerEntity player = e.getPlayer();
        World world = player.world;
        Block block = e.getState().getBlock();
        if (block instanceof VendingMachineBlock) {
            InfiniteVendingMachineBlockEntity vending = (InfiniteVendingMachineBlockEntity)world.getTileEntity(pos);
            if (vending != null) {
                if (!vending.isOwner(player)){
                    e.setNewSpeed(0);
                }
            }
        }
    }

    public void breakBlock(BlockEvent.BreakEvent e){
        BlockPos pos = e.getPos();
        PlayerEntity player = e.getPlayer();
        World world = player.world;
        Block block = e.getState().getBlock();
        if (block instanceof VendingMachineBlock) {
            InfiniteVendingMachineBlockEntity vending = (InfiniteVendingMachineBlockEntity)world.getTileEntity(pos);
            if (vending != null) {
                if (!vending.isOwner(player) && !player.abilities.isCreativeMode){
                    e.setCanceled(true);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEventHandler {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(VendingBlocks.STONE_VENDING_MACHINE,VendingBlocks.INFINITE_STONE_VENDING_MACHINE);
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            register(VendingItems.WRENCH,"wrench",event.getRegistry());
            Item.Properties properties = new Item.Properties().group(tabVending);
                event.getRegistry().register(new BlockItem(VendingBlocks.STONE_VENDING_MACHINE,properties)
                        .setRegistryName(VendingBlocks.STONE_VENDING_MACHINE.getRegistryName()));
					event.getRegistry().register(new BlockItem(VendingBlocks.INFINITE_STONE_VENDING_MACHINE,properties)
									.setRegistryName(VendingBlocks.INFINITE_STONE_VENDING_MACHINE.getRegistryName()));
        }

        @SubscribeEvent
        public static void registerBlockentities(RegistryEvent.Register<TileEntityType<?>> e){
            register(TileEntityType.Builder.create(InfiniteVendingMachineBlockEntity::new,VendingBlocks.INFINITE_STONE_VENDING_MACHINE).build(null),
                    "infinite_vending_machine",e.getRegistry());
            register(TileEntityType.Builder.create(VendingMachineBlockEntity::new,VendingBlocks.STONE_VENDING_MACHINE).build(null),
                    "vending_machine",e.getRegistry());
        }

        @SubscribeEvent
        public static void registerMenus(RegistryEvent.Register<ContainerType<?>> e) {
            register(IForgeContainerType.create((windowId, inv, data) ->
                    new VendingMachineContainer(windowId, inv, data.readBlockPos())),"vending_machine",e.getRegistry());

            register(IForgeContainerType.create((windowId, inv, data) ->
                    new InfiniteVendingMachineContainer(windowId, inv, data.readBlockPos())),"infinite_vending_machine",e.getRegistry());

            register(IForgeContainerType.create((windowId, inv, data) ->
                    new WrenchContainer(windowId, inv, data.readBlockPos())),"wrench",e.getRegistry());
        }

            @SubscribeEvent
        public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(VendingSoundEvents.SOUNDS);
        }

        private static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
            registry.register(obj.setRegistryName(new ResourceLocation(MODID, name)));
        }

    }

    public static class Objects {
        @ObjectHolder(MODID+":vending_machine")
        public static final TileEntityType<InfiniteVendingMachineBlockEntity> vending_machine = null;

        @ObjectHolder(MODID+":vending_machine")
        public static final ContainerType<VendingMachineContainer> container_vending_machine = null;

        @ObjectHolder(MODID+":infinite_vending_machine")
        public static final TileEntityType<VendingMachineBlockEntity> infinite_vending_machine = null;

        @ObjectHolder(MODID+":infinite_vending_machine")
        public static final ContainerType<InfiniteVendingMachineContainer> infinite_container_vending_machine = null;

        @ObjectHolder(MODID+":wrench")
        public static final ContainerType<WrenchContainer> container_wrench = null;
    }
}



