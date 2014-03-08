package com.uyjulian.minecraft.ControlPack;

import net.minecraft.client.settings.KeyBinding;

public class ControlPackKeybindingController {
	public static ControlPackKeybindingController instance;
	public KeyBinding[] keyBindings;
	public KeyBinding keyBindAlternateLeft;
	public KeyBinding keyBindAlternateRight;
	public KeyBinding keyBindToggleSneak;
	public KeyBinding keyBindToggleRun;
	public KeyBinding keyBindToggleJump;
	public KeyBinding keyBindToggleMine;
	public KeyBinding keyBindToggleUse;
	public KeyBinding keyBindWalkDistance;
	public KeyBinding keyBindLookBehind;
	public KeyBinding keyBindToggleGamma;
	public KeyBinding keyBindSprint;
	public KeyBinding keyBindTorch;
	public KeyBinding keyBindEat;
	public KeyBinding keyBindSayLocation;
	public KeyBinding keyBindWaypoints;
	
	public ControlPackKeybindingController() {
		instance = this;
		this.keyBindAlternateLeft = new KeyBinding("key.altleft", 29, "ControlPack");
		this.keyBindAlternateRight = new KeyBinding("key.altright", 41, "ControlPack");
		this.keyBindToggleSneak = new KeyBinding("key.toggleSneak", 58, "ControlPack");
		this.keyBindToggleRun = new KeyBinding("key.autoRun", 19, "ControlPack");
		this.keyBindToggleJump = new KeyBinding("key.autoJump", 36, "ControlPack");
		this.keyBindToggleMine = new KeyBinding("key.toggleMine", 50, "ControlPack");
		this.keyBindToggleUse = new KeyBinding("key.toggleUse", 49, "ControlPack");
		this.keyBindWalkDistance = new KeyBinding("key.walkDistance", 13, "ControlPack");
		this.keyBindLookBehind = new KeyBinding("key.lookBehind", -98, "ControlPack");
		this.keyBindToggleGamma = new KeyBinding("key.toggleGamma", 48, "ControlPack");
		this.keyBindSprint = new KeyBinding("key.startSprint", 33, "ControlPack");
		this.keyBindTorch = new KeyBinding("key.placeTorch", 47, "ControlPack");
		this.keyBindEat = new KeyBinding("key.eatFood", 199, "ControlPack");
		this.keyBindWaypoints = new KeyBinding("key.waypoints", 52, "ControlPack");
		this.keyBindSayLocation = new KeyBinding("key.saylocation", 210, "ControlPack");
		this.keyBindings = new KeyBinding[] { 
			this.keyBindSprint, 
			this.keyBindTorch, 
			this.keyBindAlternateLeft, 
			this.keyBindAlternateRight, 
			this.keyBindEat, 
			this.keyBindToggleSneak, 
			this.keyBindToggleRun, 
			this.keyBindToggleJump, 
			this.keyBindToggleMine, 
			this.keyBindToggleUse, 
			this.keyBindWalkDistance, 
			this.keyBindLookBehind, 
			this.keyBindToggleGamma, 
			this.keyBindSayLocation, 
			this.keyBindWaypoints 
		};
	}
	
	public static void initKeybindings() {
		new ControlPackKeybindingController();
	}
}
