package net.minecraft.src.overrides;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class KeyBinding
{
	public static List keybindArray = new ArrayList();
	public static IntHashMap hash = new IntHashMap();
	public String keyDescription;
	public int keyCode;
	public boolean pressed;
	public int pressTime;
	public boolean toggled;

	public static void onTick(int par0)
	{
		KeyBinding keybinding = (KeyBinding)hash.lookup(par0);

		if (keybinding != null)
		{
			keybinding.pressTime += 1;
		}
	}

	public static void setKeyBindState(int par0, boolean par1)
	{
		mod_ControlPack.improvedChatCompat(par0, par1);

		if ((mod_ControlPack.instance == null) || (!mod_ControlPack.instance.handleInputEvent(par0, par1))) {
			KeyBinding keybinding = (KeyBinding)hash.lookup(par0);
			if (keybinding != null)
			{
				keybinding.pressed = par1;
			}

		}

		if (mod_ControlPack.instance != null)
			mod_ControlPack.instance.resetPlayerKeyState();
	}

	public static void unPressAllKeys()
	{
		Iterator iterator = keybindArray.iterator();

		while (iterator.hasNext())
		{
			KeyBinding keybinding = (KeyBinding)iterator.next();
			keybinding.unpressKey();
		}

		if (mod_ControlPack.instance != null)
			mod_ControlPack.instance.resetPlayerKeyState();
	}

	public static void resetKeyBindingArrayAndHash()
	{
		hash.clearMap();
		Iterator iterator = keybindArray.iterator();

		while (iterator.hasNext())
		{
			KeyBinding keybinding = (KeyBinding)iterator.next();
			hash.addKey(keybinding.keyCode, keybinding);
		}

		if (mod_ControlPack.instance != null)
			mod_ControlPack.instance.resetPlayerKeyState();
	}

	public KeyBinding(String par1Str, int par2)
	{
		this.keyDescription = par1Str;
		this.keyCode = par2;
		keybindArray.add(this);
		hash.addKey(par2, this);
	}

	public boolean isPressed()
	{
		if ((mod_ControlPack.instance != null) && 
			(this == mod_ControlPack.mc.gameSettings.keyBindInventory)) {
			mod_ControlPack.instance.runAutoTool(false);
		}

		if (this.pressTime == 0)
		{
			return false;
		}

		this.pressTime -= 1;
		return true;
	}

	private void unpressKey()
	{
		this.pressTime = 0;
		this.pressed = false;
	}

	public boolean isDown()
	{
		if (this.keyCode >= 0) {
			return Keyboard.isKeyDown(this.keyCode);
		}

		return Mouse.isButtonDown(this.keyCode + 100);
	}

	public void reset() {
		this.pressTime = 0;
		this.pressed = false;
	}
	public void applyToggle() {
		this.pressed = ((this.pressed) || (this.toggled));
	}
	public void toggle(boolean state) {
		if (this.toggled != state) {
			this.toggled = state;
			if (!this.toggled) {
				this.pressed = false;
				this.pressTime = 0;
			}
		}
	}

	public void toggle() { toggle(!this.toggled); }

}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.KeyBinding
 * JD-Core Version:		0.6.2
 */