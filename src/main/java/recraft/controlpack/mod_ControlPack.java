package recraft.controlpack;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Session;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class mod_ControlPack
{
	public static mod_ControlPack instance;
	public static Minecraft mc;
	private int toggleCounter = 10;

	private boolean chat_insertedPosition = false;

	public int lastMeasureDistance;
	public boolean measureDistanceState;
	public boolean measureDistanceStateMoving;
	public int measureDistanceStartX;
	public int measureDistanceStartZ;
	public double measureDistanceRemaining;
	
	public float frontView_rotationPitch;
	public float frontView_rotationYaw;
	public float pitchSpeed = 0.2F;
	public float yawSpeed = 0.2F;
	
	private Rectangle lastBounds;
	private boolean lastFullscreen;
	private boolean lastPositionExists;
	private int isEating = -1;
	private boolean swapBack;
	private int swapBackTo;
	private boolean altKey;
	public boolean cameraStandMode;
	public boolean renderingWorld;
	public EntityLivingBase wrappedEntity;
	private boolean previouslyPlacedBlock;
	private int previouslyPlacedBlockID;

	public mod_ControlPack()
		throws Exception
	{
		instance = this;
		mc = Minecraft.getMinecraft();

		new ControlPackInventoryUtils();
		ControlPackKeybindingController.initKeybindings();
		ControlPackOptionsController.initOptions();

	}

	public static String translate(String key)
	{
		return ControlPackOptionsController.translate(key);
	}




	public void applyLastWindowSize() {
		if ((ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.WINDOWRESTORE)) && (this.lastPositionExists))
		{
			try
			{
				if (this.lastFullscreen) {
					if (!Display.isFullscreen()) {
						mc.toggleFullscreen();
					}

				}
				else
				{
					Display.setLocation(this.lastBounds.x, this.lastBounds.y);
					Display.setDisplayMode(new DisplayMode(this.lastBounds.width, this.lastBounds.height));

					mc.displayWidth = Display.getWidth();
					mc.displayHeight = Display.getHeight();

					if (mc.displayWidth <= 0)
					{
						mc.displayWidth = 1;
					}

					if (mc.displayHeight <= 0)
					{
						mc.displayHeight = 1;
					}

					if (mc.currentScreen != null) {
						ScaledResolution var3 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
						int var4 = var3.getScaledWidth();
						int var5 = var3.getScaledHeight();
						mc.currentScreen.setWorldAndResolution(mc, var4, var5);
					}
				}
			}
			catch (Exception ex)
			{
			}
		}
	}

	public void checkGame()
	{
		try
		{
			Rectangle bounds = new Rectangle(Display.getX(), Display.getY(), Display.getWidth(), Display.getHeight());

			if ((!bounds.equals(this.lastBounds)) || (this.lastFullscreen != Display.isFullscreen())) {
				this.lastFullscreen = Display.isFullscreen();

				if (!this.lastFullscreen) {
					this.lastBounds = bounds;
				}
				ControlPackOptionsController.instance.saveOptions();
			}
		}
		catch (Exception ex) {
			System.out.println("Unable to check window state. " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void load()
	{
		applyLastWindowSize();
	}

	public void resetPlayerKeyState() {
		ControlPackToggleController.applyToggleToKey(mc.gameSettings.keyBindAttack);
		ControlPackToggleController.applyToggleToKey(mc.gameSettings.keyBindUseItem);


		ControlPackToggleController.applyToggleToKey(mc.gameSettings.keyBindJump);
		ControlPackToggleController.applyToggleToKey(mc.gameSettings.keyBindForward);
		ControlPackToggleController.applyToggleToKey(mc.gameSettings.keyBindBack);
		ControlPackToggleController.applyToggleToKey(mc.gameSettings.keyBindSneak);
	}

	public void turnOffToggles() {
		ControlPackToggleController.toggleKey(mc.gameSettings.keyBindForward, false);
		ControlPackToggleController.toggleKey(mc.gameSettings.keyBindBack, false);
		ControlPackToggleController.toggleKey(mc.gameSettings.keyBindSneak, false);
		ControlPackToggleController.toggleKey(mc.gameSettings.keyBindAttack, false);
		ControlPackToggleController.toggleKey(mc.gameSettings.keyBindUseItem, false);
		ControlPackToggleController.toggleKey(mc.gameSettings.keyBindJump, false);
	}

	private void cancelMeasureDistance()
	{
		if (this.measureDistanceState) {
			this.measureDistanceState = false;
			if (this.measureDistanceStateMoving) {
				this.measureDistanceStateMoving = false;
				ControlPackToggleController.toggleKey(mc.gameSettings.keyBindForward, false);
				ControlPackToggleController.toggleKey(mc.gameSettings.keyBindBack, false);
				resetPlayerKeyState();
			}
		}
	}

	public boolean swapToFood()
	{
		for (int i = 0; i < 9; i++) {
			ItemStack possibleBlock = mc.thePlayer.inventory.mainInventory[i];
			if ((possibleBlock != null) && ((possibleBlock.getItem() instanceof ItemFood)))
			{
				this.swapBackTo = mc.thePlayer.inventory.currentItem;
				mc.thePlayer.inventory.currentItem = i;
				return true;
			}
		}
		return false;
	}

	public void placeTorch() {
		for (int i = 0; i < 9; i++) {
			ItemStack possibleBlock = mc.thePlayer.inventory.mainInventory[i];
			if ((possibleBlock != null) && (Block.getBlockFromItem(possibleBlock.getItem()) instanceof BlockTorch)) {
				this.swapBack = true;
				this.swapBackTo = mc.thePlayer.inventory.currentItem;
				mc.thePlayer.inventory.currentItem = i;

				KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
				return;
			}
		}

		for (int i = 0; i < 9; i++) {
			ItemStack possibleBlock = mc.thePlayer.inventory.mainInventory[i];
			if ((possibleBlock != null) && ((Block.getBlockFromItem(possibleBlock.getItem()) instanceof BlockRedstoneTorch))) {
				this.swapBack = true;
				this.swapBackTo = mc.thePlayer.inventory.currentItem;
				mc.thePlayer.inventory.currentItem = i;

				KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
				return;
			}
		}
	}

	public void runAutoBlockOnExpend() {
		ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
		if ((currentItem == null) || (currentItem.stackSize <= 0))
		{
			for (int i = 0; i < 9; i++) {
				ItemStack possibleBlock = mc.thePlayer.inventory.mainInventory[i];
				if ((possibleBlock != null) && (Item.getIdFromItem(possibleBlock.getItem()) == this.previouslyPlacedBlockID)) {
					mc.thePlayer.inventory.currentItem = i;
					break;
				}
			}
		}
	}

	public void runAutoBlock()
	{
		ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
		if ((currentItem == null) || (ControlPackInventoryUtils.instance.isTool(currentItem.getItem()))) {
			Integer mode = ControlPackOptionsController.getIntOption(ControlPackEnumOptions.AUTOBLOCKMODE);
			int foundBlockId = -1;
			if (mode.intValue() == 0) {
				for (int i = 0; i < 9; i++) {
					ItemStack possibleBlock = mc.thePlayer.inventory.mainInventory[i];
					if ((possibleBlock != null) && ((possibleBlock.getItem() instanceof ItemBlock))) {
						mc.thePlayer.inventory.currentItem = i;
						break;
					}
				}
			}
			else if (mode.intValue() == 1) {
				for (int i = 8; i >= 0; i--) {
					ItemStack possibleBlock = mc.thePlayer.inventory.mainInventory[i];
					if ((possibleBlock != null) && ((possibleBlock.getItem() instanceof ItemBlock))) {
						mc.thePlayer.inventory.currentItem = i;
						break;
					}
				}
			}
			else if (mode.intValue() < 11) {
				ItemStack possibleBlock = mc.thePlayer.inventory.mainInventory[(mode.intValue() - 2)];
				if ((possibleBlock != null) && ((possibleBlock.getItem() instanceof ItemBlock)))
					mc.thePlayer.inventory.currentItem = (mode.intValue() - 2);
			}
		}
	}

	public String getLocation(boolean shouldFormat) {
		if ((mc == null) || (mc.thePlayer == null)) return "";
		Vec3 currentPos = mc.thePlayer.getPosition(1.0F);
		int currentX = (int)Math.ceil(currentPos.xCoord) - 1;
		int currentY = (int)Math.ceil(currentPos.yCoord) - 1;
		int currentZ = (int)Math.ceil(currentPos.zCoord) - 1;
		if (shouldFormat) {
			return ControlPackOptionsController.getStringOption(ControlPackEnumOptions.COORDINATE_FORMAT).replace("{X}", "" + currentX).replace("{Y}", "" + currentY).replace("{Z}", "" + currentZ);
		}

		return currentX + ", " + currentZ + ", " + currentY;
	}

	public boolean onTickInGUI(float f, Minecraft mc, GuiScreen currentScreen)
	{
		if ((currentScreen instanceof GuiChat)) {
			if ((Keyboard.isKeyDown(ControlPackKeybindingController.instance.keyBindSayLocation.getKeyCode())) && (!this.chat_insertedPosition)) {
				try {
					Field field;
					try {
						field = currentScreen.getClass().getDeclaredField("a");
					}
					catch (NoSuchFieldException ex) {
						field = currentScreen.getClass().getDeclaredField("inputField");
					}
					field.setAccessible(true);
					GuiTextField tf = (GuiTextField)field.get(currentScreen);
					tf.setText(tf.getText() + " " + getLocation(true) + " ");
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

				this.chat_insertedPosition = true;
			}
			else if ((!Keyboard.isKeyDown(ControlPackKeybindingController.instance.keyBindSayLocation.getKeyCode()))) {
				this.chat_insertedPosition = false;
			}
		}
		return true;
	}

	public boolean onTickInGame(float f, Minecraft mc)
	{
		if (mc.currentScreen != null) {
			this.altKey = false;
		}
		checkGame();

		if ((this.previouslyPlacedBlock) && (this.previouslyPlacedBlockID != -1)) {
			this.previouslyPlacedBlock = false;
			if (ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.AUTOBLOCK)) {
				runAutoBlockOnExpend();
			}
		}

		if (mc.gameSettings.keyBindUseItem.getIsKeyPressed())
		{
			this.previouslyPlacedBlock = true;
			ItemStack stack = mc.thePlayer.inventory.getCurrentItem();
			this.previouslyPlacedBlockID = (stack == null ? -1 : Item.getIdFromItem(stack.getItem()));
		}

		if ((ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.HOLDTOATTACK)) && (mc.gameSettings.keyBindAttack.getIsKeyPressed())) {
			if (--this.toggleCounter <= 0)
			{
				ControlPackToggleController.setPressTime(mc.gameSettings.keyBindAttack, 1);
				this.toggleCounter = 10;
			}
		}
		else {
			this.toggleCounter = 10;
		}

		if (this.swapBack) {
			this.swapBack = false;
			mc.thePlayer.inventory.currentItem = this.swapBackTo;
		}

		if (mc.currentScreen != null) {
			ControlPackToggleController.unpressKey(mc.gameSettings.keyBindUseItem);

			ControlPackToggleController.toggleKey(mc.gameSettings.keyBindAttack, false);
		}


		if (this.measureDistanceState) {
			Vec3 currentPos = mc.thePlayer.getPosition(1.0F);
			int currentX = (int)(currentPos.xCoord < 0.0D ? Math.ceil(currentPos.xCoord) : Math.floor(currentPos.xCoord));
			int currentY = (int)(currentPos.yCoord < 0.0D ? Math.ceil(currentPos.yCoord) : Math.floor(currentPos.yCoord));
			int currentZ = (int)(currentPos.zCoord < 0.0D ? Math.ceil(currentPos.zCoord) : Math.floor(currentPos.zCoord));

			double traveled = Math.max(Math.abs(this.measureDistanceStartX - currentX), Math.abs(this.measureDistanceStartZ - currentZ));
			this.measureDistanceRemaining = (this.lastMeasureDistance - traveled);
			if (traveled >= this.lastMeasureDistance) {
				cancelMeasureDistance();
			}
			else if (!this.measureDistanceStateMoving) {
				this.measureDistanceStateMoving = true;
				resetPlayerKeyState();
			}

		}


		return true;
	}

	private void openGUIRunDistance() {
		GuiMeasure guiMeasure = new GuiMeasure(String.valueOf(this.lastMeasureDistance));
		mc.displayGuiScreen(guiMeasure);
	}

	public void moveByDistance(int numTiles) {
		this.lastMeasureDistance = numTiles;
		Vec3 pos = mc.thePlayer.getPosition(1.0F);
		this.measureDistanceStartX = ((int)(pos.xCoord < 0.0D ? Math.ceil(pos.xCoord) : Math.floor(pos.xCoord)));
		this.measureDistanceStartZ = ((int)(pos.zCoord < 0.0D ? Math.ceil(pos.zCoord) : Math.floor(pos.zCoord)));
		this.measureDistanceState = true;
		ControlPackToggleController.toggleKey(mc.gameSettings.keyBindForward, true);
	}

	public void toggleThirdPersonView()
	{
		if (mc.gameSettings.thirdPersonView >= 2)
		{
			if (mc.gameSettings.debugCamEnable)
			{
				mc.gameSettings.debugCamEnable = false;
			}
			else
			{
				mc.gameSettings.debugCamEnable = true;

				mc.gameSettings.thirdPersonView = 1;
			}
		}
	}

	public boolean handleInputEvent(int code, boolean down)
	{
		if (mc.currentScreen != null) return false;

		if ((code == 56) || (code == 184)) {
			this.altKey = down;
		}

		if ((down) && (code == mc.gameSettings.keyBindAttack.getKeyCode()))
		{
			ControlPackInventoryUtils.instance.runAutoTool(true);
		}

		if ((code == 63) && (down) && (ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.FRONTVIEW))) {
			toggleThirdPersonView();
			return true;
		}

		if (code == ControlPackKeybindingController.instance.keyBindEat.getKeyCode()) {
			if (down) {
				if (swapToFood()) {
					this.isEating = mc.thePlayer.inventory.currentItem;
					ControlPackToggleController.toggleKey(mc.gameSettings.keyBindUseItem, true);
				}
			}
			else {
				this.isEating = -1;
				this.swapBack = true;
				ControlPackToggleController.toggleKey(mc.gameSettings.keyBindUseItem, false);
			}
		}
		else if ((this.isEating != -1) && (this.isEating != mc.thePlayer.inventory.currentItem)) {
			this.isEating = -1;
			this.swapBack = true;
			ControlPackToggleController.toggleKey(mc.gameSettings.keyBindUseItem, false);
		}

		if ((down) && (code == ControlPackKeybindingController.instance.keyBindTorch.getKeyCode())) {
			placeTorch();
		}

		if ((down) && (code == ControlPackKeybindingController.instance.keyBindSprint.getKeyCode()) && (mc.gameSettings.keyBindForward.isPressed())) {
			mc.thePlayer.setSprinting(true);
		}

		if ((down) && (this.altKey) && (code == 46))
		{
			ControlPackOptionsController.setOption(ControlPackEnumOptions.WELCOMENAG, Boolean.valueOf(false));
			ControlPackOptionsController.instance.saveOptions();
			mc.displayGuiScreen(new GuiControlPack());
			this.altKey = false;
			return true;
		}

		if ((down) && (this.altKey) && (code == 20))
		{
			boolean enabled = !ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.AUTOTOOL);
			ControlPackOptionsController.setOption(ControlPackEnumOptions.AUTOTOOL, Boolean.valueOf(enabled));
			mc.thePlayer.addChatMessage(new ChatComponentText("ControlPack: Auto Tool " + (enabled ? "ENABLED" : "DISABLED")));
			ControlPackOptionsController.instance.saveOptions();
			return true;
		}
		if ((down) && (this.altKey) && (code == 31))
		{
			boolean enabled = !ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.AUTOSWORD);
			ControlPackOptionsController.setOption(ControlPackEnumOptions.AUTOSWORD, Boolean.valueOf(enabled));
			mc.thePlayer.addChatMessage(new ChatComponentText("ControlPack: Auto Sword " + (enabled ? "ENABLED" : "DISABLED")));
			ControlPackOptionsController.instance.saveOptions();
			return true;
		}
		if ((down) && (this.altKey) && (code == 48))
		{
			boolean enabled = !ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.AUTOBLOCK);
			ControlPackOptionsController.setOption(ControlPackEnumOptions.AUTOBLOCK, Boolean.valueOf(enabled));
			mc.thePlayer.addChatMessage(new ChatComponentText("ControlPack: Auto Block " + (enabled ? "ENABLED" : "DISABLED")));
			ControlPackOptionsController.instance.saveOptions();
			return true;
		}
		if ((down) && (this.altKey) && (code == 19))
		{
			int mode = ControlPackOptionsController.getIntOption(ControlPackEnumOptions.AUTOTOOLMODE);
			mode++;
			ControlPackOptionsController.setOption(ControlPackEnumOptions.AUTOTOOLMODE, Integer.valueOf(mode));
			mc.thePlayer.addChatMessage(new ChatComponentText("ControlPack: Auto Tool Mode = " + getIntOptionDesc(ControlPackEnumOptions.AUTOTOOLMODE, Integer.valueOf(mode))));
			ControlPackOptionsController.instance.saveOptions();
			return true;
		}



		if (((this.measureDistanceState) && (code == mc.gameSettings.keyBindForward.getKeyCode())) || (code == mc.gameSettings.keyBindBack.getKeyCode())) {
			cancelMeasureDistance();
		}

		if (down) {

			if ((ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.AUTOBLOCK)) && (code == mc.gameSettings.keyBindUseItem.getKeyCode()))
			{
				runAutoBlock();

				this.previouslyPlacedBlock = true;
				ItemStack stack = mc.thePlayer.inventory.getCurrentItem();
				this.previouslyPlacedBlockID = (stack == null ? -1 : Item.getIdFromItem(stack.getItem()));
			}

			if (code == ControlPackKeybindingController.instance.keyBindWalkDistance.getKeyCode()) {
				openGUIRunDistance();
				return true;
			}

			if (code == ControlPackKeybindingController.instance.keyBindToggleMine.getKeyCode()) {
				ControlPackToggleController.toggleKey(mc.gameSettings.keyBindAttack);
				return true;
			}

			if (code == ControlPackKeybindingController.instance.keyBindToggleUse.getKeyCode()) {
				ControlPackToggleController.toggleKey(mc.gameSettings.keyBindUseItem);
				return true;
			}

			if (code == ControlPackKeybindingController.instance.keyBindToggleSneak.getKeyCode()) {
				ControlPackToggleController.toggleKey(mc.gameSettings.keyBindSneak);
				return true;
			}
			if (code == ControlPackKeybindingController.instance.keyBindToggleRun.getKeyCode()) {
				cancelMeasureDistance();

				if (mc.gameSettings.keyBindBack.isPressed()) {
					ControlPackToggleController.toggleKey(mc.gameSettings.keyBindBack);
				}
				else {
					ControlPackToggleController.toggleKey(mc.gameSettings.keyBindForward);
				}

				return true;
			}
			if (code == ControlPackKeybindingController.instance.keyBindToggleJump.getKeyCode()) {
				ControlPackToggleController.toggleKey(mc.gameSettings.keyBindJump);

				return true;
			}

			if ((code == mc.gameSettings.keyBindForward.getKeyCode()) || (code == mc.gameSettings.keyBindBack.getKeyCode())) {
				cancelMeasureDistance();
			}
		}
		return false;
	}

	public String getOptionDesc(ControlPackEnumOptions option)
	{
		String s = translate(option.getLocKey()) + ": ";
		if (option.getIsBool()) {
			boolean value = ControlPackOptionsController.getBooleanOption(option);
			s = s + (value ? "ON" : "OFF");
		}
		else if (option.getIsFloat()) {
			float value = ControlPackOptionsController.getFloatOption(option);
			s = s + (int)(value * 100.0F) + "%";
		}
		else {
			Integer value = ControlPackOptionsController.getIntOption(option);
			s = s + " " + getIntOptionDesc(option, value);
		}
		return s;
	}

	private String getIntOptionDesc(ControlPackEnumOptions option, Integer value) {
		if (option == ControlPackEnumOptions.AUTOTOOLMODE) {
			return value.intValue() == 2 ? "Leftmost" : value.intValue() == 1 ? "Strongest" : value.intValue() == 0 ? "Weakest" : "Rightmost";
		}
		if (option == ControlPackEnumOptions.AUTOBLOCKMODE) {
			if (value.intValue() == 0) {
				return "Leftmost";
			}
			if (value.intValue() == 1) {
				return "Rightmost";
			}
			return "Slot #" + (value.intValue() - 1);
		}
		if ((option == ControlPackEnumOptions.STATUSLOCATION) || (option == ControlPackEnumOptions.COORDINATESLOCATION)) {
			if (value.intValue() == 0) {
				return "Top Left";
			}
			if (value.intValue() == 1) {
				return "Top Right";
			}
			if (value.intValue() == 2) {
				return "Bottom Left";
			}
			if (value.intValue() == 3) {
				return "Bottom Right";
			}
			if (value.intValue() == 4) {
				return "OFF";
			}
		}
		return "";
	}


}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.mod_ControlPack
 * JD-Core Version:		0.6.2
 */