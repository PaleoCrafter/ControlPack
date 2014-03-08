package com.uyjulian.minecraft.ControlPack;

import net.minecraft.client.settings.KeyBinding;

public enum ControlPackEnumKeybinding {
	AlternateLeft("key.altleft", 29),
	AlternateRight("key.altright", 41),
	ToggleSneak("key.toggleSneak", 58),
	ToggleRun("key.autoRun", 19),
	ToggleJump("key.autoJump", 36),
	ToggleMine("key.toggleMine", 50),
	ToggleUse("key.toggleUse", 49),
	WalkDistance("key.walkDistance", 13),
	LookBehind("key.lookBehind", -98),
	ToggleGamma("key.toggleGamma", 48),
	StartSprinting("key.startSprint", 33),
	PlaceTorch("key.placeTorch", 47),
	EatFood("key.eatFood", 199),
	SayLocation("key.saylocation", 210);
	
	private final String name;
	private final int keycode;
	
	private ControlPackEnumKeybinding(String curName, int curKeycode) {
		this.name = curName;
		this.keycode = curKeycode;
	}
	
	public KeyBinding getKeybinding() {
		return new KeyBinding(this.name, this.keycode, "ControlPack");
	}
}
