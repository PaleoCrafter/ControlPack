package recraft.controlpack;

import static org.lwjgl.input.Keyboard.*;
import net.malisis.core.MalisisCore;
import net.malisis.core.event.user.KeyboardEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CPControls
{
	public static CPControls instance;
	private Minecraft minecraft = Minecraft.getMinecraft();
		
	private boolean autorun = false;
	private boolean autoSwitchTool = true;
	private boolean autoSwitchTorch = true;
	
	public CPControls()
	{
		instance = this;
	}
	public static CPControls instance()
	{
		return instance != null ? instance : new CPControls();
	}
	
	private boolean keyPressed(KeyBinding key)
	{
		return keyPressed(key.getKeyCode());
	}
	private boolean keyPressed(int key)
	{
		return Keyboard.getEventKey() == key && Keyboard.getEventKeyState();
	}
	
	@SubscribeEvent
	public void onKeyPressed(KeyboardEvent event)
	{
		if(keyPressed(minecraft.gameSettings.keyBindTogglePerspective) && toggleThirdPersonView())
		{
			event.setCanceled(true);
			return;
		}
		
		if(keyPressed(KEY_A))
		{
			autorun = !autorun;
			MalisisCore.Message("Autorun : " + autorun);
			return;
		}
	}
	
	public static boolean getAutorun()
	{
		return instance().autorun;
	}
	public static boolean getAutoSwitchTool()
	{
		return instance().autoSwitchTool;
	}
	public static boolean getAutoSwitchTorch()
	{
		return instance().autoSwitchTorch;
	}
	
	public boolean toggleThirdPersonView()
	{
		MalisisCore.Message(minecraft.gameSettings.thirdPersonView);
		if (minecraft.gameSettings.thirdPersonView >= 2)
		{
			if(!minecraft.gameSettings.debugCamEnable)
			{
				minecraft.gameSettings.debugCamEnable = true;
				return true;
			}
			else
			{
				minecraft.gameSettings.debugCamEnable = false;
				return false;
			}
		}
		return false;
	}
}
