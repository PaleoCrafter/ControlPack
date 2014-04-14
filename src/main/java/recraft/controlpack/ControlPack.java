package recraft.controlpack;

import java.util.Arrays;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;


public class ControlPack  extends DummyModContainer
{
	public static final String modid = "controlpack";
	public static final String modname = "ControlPack";
	public static final String version = "1.7.2-6.0";

	public static ControlPack instance;
	
	public ControlPack()
	{
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = modid;
		meta.name = modname;
		meta.version = version;
		meta.authorList = Arrays.asList("Ordinastie");
		meta.url = "";
		meta.description = "";
		
		instance = this;
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}

	@Subscribe
	public static void preInit(FMLPreInitializationEvent event)
	{
		if (event.getSide() == Side.CLIENT)
		{
			MinecraftForge.EVENT_BUS.register(new CPControls());
			MinecraftForge.EVENT_BUS.register(new CPPlayerEvent());
		
		//	FMLCommonHandler.instance().bus().register(new PlayerEvent());
		}
	}

	@Subscribe
	public void serverStart(FMLServerStartingEvent event)
	{
		MinecraftServer server = MinecraftServer.getServer();
		((ServerCommandManager) server.getCommandManager()).registerCommand(new CPCommand());

	}

}
