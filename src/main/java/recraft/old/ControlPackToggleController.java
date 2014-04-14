package recraft.old;

import net.minecraft.client.settings.KeyBinding;

public class ControlPackToggleController {
	public static void setPressTime(KeyBinding keybinding, int pressTime) {
		keybinding.pressTime = pressTime;
	}
	
	public static int getPressTime(KeyBinding keybinding) {
		return keybinding.pressTime;
	}
	
	public static void unpressKey(KeyBinding keybinding) {
		keybinding.unpressKey();
	}
	
	public static void toggleKey(KeyBinding keybinding, boolean state) {
		keybinding.toggle(state);
	}
	
	public static void toggleKey(KeyBinding keybinding) {
		keybinding.toggle();
	}
	
	public static void applyToggleToKey(KeyBinding keybinding) {
		keybinding.applyToggle();
	}
	
}
