package com.uyjulian.minecraft.ControlPack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class ControlPackInventoryUtils {
	private Minecraft mc;
	public int swappedInventoryState;
	public static ControlPackInventoryUtils instance;
	
	public ControlPackInventoryUtils() {
		instance = this;
		mc = Minecraft.getMinecraft();
	}

	boolean isTool(Item item)
	{
		return (item != null) && (((item instanceof ItemTool)) || ((item instanceof ItemShears)));
	}

	private float blockStrength(Block block, Item item)
	{
		if (block.getBlockHardness(mc.theWorld, 1, 1, 1) < 0.0F) {
			return 0.0F;
		}
		if ((item == null) || (!canHarvestBlock(item, block)) || ((getIsHarvestable(block)) && (item.func_150893_a(new ItemStack(block), block) == 1.0F))) {
			return 1.0F / block.getBlockHardness(mc.theWorld, 1, 1, 1) / 100.0F;
		}

		return item.func_150893_a(new ItemStack(block), block) / block.getBlockHardness(mc.theWorld, 1, 1, 1) / 30.0F;
	}

	private boolean canHarvestBlock(Item item, Block block)
	{
		return canHarvestBlock(item, block, false);
	}

	private boolean canHarvestBlock(Item item, Block block, boolean forReals) {
		if ((!forReals) && (getIsHarvestable(block))) return true;
		boolean canHarvest = item.func_150897_b(block);
		if (canHarvest) return true;

		if ((item instanceof ItemShears)) {
			return ((block instanceof BlockVine)) || ((block instanceof BlockLeaves)) || ((block instanceof BlockColored));
		}
		return false;
	}

	private boolean getIsHarvestable(Block block) {
		if (!block.getMaterial().isToolNotRequired()) {
			return false;
		}
		if (((block instanceof BlockVine)) || ((block instanceof BlockLeaves))) {
			return false;
		}
		return true;
	}

	private boolean isSword(Item item) {
		if ((item instanceof ItemSword)) return true;
		String swordIdList = "," + ControlPackOptionsController.getStringOption(ControlPackEnumOptions.ITEM_SWORDS) + ",";
		return swordIdList.contains("," + Item.getIdFromItem(item) + ",");
	}

	public void ensureSwordSelected() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if ((stack != null) && 
				(isSword(stack.getItem()))) {
				if (i != mc.thePlayer.inventory.currentItem) {
					mc.thePlayer.inventory.currentItem = i;
				}
				return;
			}

		}

		ItemStack currentStack = mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem];
		if ((currentStack != null) && (isTool(currentStack.getItem())))
			swapToHand();
	}

	private boolean swapToHand()
	{
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack == null) {
				mc.thePlayer.inventory.currentItem = i;
				return true;
			}
		}

		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack != null) {
				Item item = stack.getItem();
				if ((item != null) && (!isSword(item)) && (!isTool(item))) {
					mc.thePlayer.inventory.currentItem = i;
					return true;
				}
			}
		}
		return false;
	}

	private boolean isBetterTool(Block block, Item tool, Item testTool) {
		if (!getIsHarvestable(block))
		{
			if (!canHarvestBlock(testTool, block)) {
				return false;
			}

			if ((tool == null) || (!canHarvestBlock(tool, block))) {
				return true;
			}

			float testStrength = blockStrength(block, testTool);
			float existingStrength = blockStrength(block, tool);
			Integer mode = ControlPackOptionsController.getIntOption(ControlPackEnumOptions.AUTOTOOLMODE);
			if (mode.intValue() == 0) {
				if (testStrength < existingStrength) {
					return true;
				}
				if (testStrength == existingStrength)
				{
					if (testTool.getMaxDamage() < tool.getMaxDamage())
						return true;
				}
			}
			else
			{
				if (testStrength > existingStrength) {
					return true;
				}
				if (testStrength == existingStrength)
				{
					if (testTool.getMaxDamage() > tool.getMaxDamage())
						return true;
				}
			}
		}
		else
		{
			float testStrength = blockStrength(block, testTool);
			float existingStrength = blockStrength(block, tool);
			float handStrength = blockStrength(block, null);

			if (testStrength <= handStrength) {
				return false;
			}

			if ((tool == null) || (existingStrength == handStrength))
			{
				return true;
			}

			Integer mode = ControlPackOptionsController.getIntOption(ControlPackEnumOptions.AUTOTOOLMODE);
			if (mode.intValue() == 0) {
				if (testStrength < existingStrength) {
					return true;
				}
				if ((testStrength == existingStrength) && 
					(testTool.getMaxDamage() < tool.getMaxDamage())) {
					return true;
				}
			}
			else
			{
				if (testStrength > existingStrength) {
					return true;
				}
				if ((testStrength == existingStrength) && 
					(testTool.getMaxDamage() < tool.getMaxDamage())) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isBetterSword(Item tool, Item testTool)
	{
		if (tool == null) {
			return true;
		}

		Integer mode = ControlPackOptionsController.getIntOption(ControlPackEnumOptions.AUTOTOOLMODE);
		if (mode.intValue() == 0) {
			if (testTool.getMaxDamage() < tool.getMaxDamage()) {
				return true;
			}

		}
		else if (testTool.getMaxDamage() > tool.getMaxDamage()) {
			return true;
		}

		return false;
	}

	private boolean swapToFirstTool(Block block, Item currentItem) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack != null) {
				Item item = stack.getItem();
				if ((item != null) && (isTool(item)))
				{
					if (blockStrength(block, item) > blockStrength(block, null)) {
						mc.thePlayer.inventory.currentItem = i;
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean swapToFirstSword() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack != null) {
				Item item = stack.getItem();
				if ((item != null) && (isSword(item)))
				{
					mc.thePlayer.inventory.currentItem = i;
					return true;
				}
			}
		}
		return false;
	}

	private boolean swapToLastTool(Block block, Item currentItem) {
		for (int i = 8; i >= 0; i--) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack != null) {
				Item item = stack.getItem();
				if ((item != null) && (isTool(item)))
				{
					if (blockStrength(block, item) > blockStrength(block, null)) {
						mc.thePlayer.inventory.currentItem = i;
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean swapToLastSword() {
		for (int i = 8; i >= 0; i--) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack != null) {
				Item item = stack.getItem();
				if ((item != null) && (isSword(item)))
				{
					mc.thePlayer.inventory.currentItem = i;
					return true;
				}
			}
		}
		return false;
	}

	private boolean shouldUseSword(Block block) {
		return block instanceof BlockWeb;
	}

	private boolean swapToBestSword() {
		Integer mode = ControlPackOptionsController.getIntOption(ControlPackEnumOptions.AUTOTOOLMODE);

		if (mode.intValue() == 2)
		{
			return swapToFirstSword();
		}
		if (mode.intValue() == 3)
		{
			return swapToLastSword();
		}

		int currentItemIndex = -1;
		Item currentItem = null;

		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack != null) {
				Item item = stack.getItem();
				if ((item != null) && (isSword(item)))
				{
					if (isBetterSword(currentItem, item)) {
						currentItem = item;
						currentItemIndex = i;
					}
				}
			}
		}
		if (currentItemIndex != -1) {
			mc.thePlayer.inventory.currentItem = currentItemIndex;
			return true;
		}
		return false;
	}

	private boolean swapToBestTool(Block block, Item currentItem, int currentItemIndex) {
		Integer mode = ControlPackOptionsController.getIntOption(ControlPackEnumOptions.AUTOTOOLMODE);

		if (mode.intValue() == 2)
		{
			return swapToFirstTool(block, currentItem);
		}
		if (mode.intValue() == 3)
		{
			return swapToLastTool(block, currentItem);
		}

		int originalIndex = currentItemIndex;

		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
			if (stack != null) {
				Item item = stack.getItem();
				if ((item != null) && (isTool(item)))
				{
					if (isBetterTool(block, currentItem, item)) {
						currentItem = item;
						currentItemIndex = i;
					}
				}
			}
		}
		if (currentItemIndex != originalIndex) {
			mc.thePlayer.inventory.currentItem = currentItemIndex;
			return true;
		}
		return false;
	}

	public void ensureCorrectToolSelected(Block block)
	{
		int inventoryIndex = mc.thePlayer.inventory.currentItem;
		ItemStack currentStack = mc.thePlayer.inventory.getCurrentItem();
		Item currentItem = currentStack == null ? null : currentStack.getItem();
		boolean easilyBreakable = block.getBlockHardness(mc.theWorld, 1, 1, 1) <= 0.20001F;
		boolean harvestable = getIsHarvestable(block);
		boolean hasSword = (currentItem != null) && (isSword(currentItem));
		boolean hasTool = isTool(currentItem);
		boolean hasHandOrItem = (currentItem == null) || ((!hasSword) && (!hasTool));

		if ((easilyBreakable) && (harvestable)) {
			if (!hasHandOrItem)
			{
				swapToHand();
			}
			return;
		}

		if (hasHandOrItem) {
			swapToBestTool(block, null, -1);
		}
		else if (hasSword) {
			if (!swapToBestTool(block, null, -1)) {
				swapToHand();
			}
		}
		else if (hasTool)
		{
			if (!swapToBestTool(block, currentItem, inventoryIndex))
			{
				if (!canHarvestBlock(currentItem, block, true))
				{
					if (getIsHarvestable(block))
					{
						if (blockStrength(block, currentItem) == blockStrength(block, null))
						{
							swapToHand();
						}
					}
					else
					{
						swapToHand();
					}
				}

			}

		}

		if (shouldUseSword(block)) {
			currentStack = mc.thePlayer.inventory.getCurrentItem();
			currentItem = currentStack == null ? null : currentStack.getItem();
			hasSword = (currentItem != null) && (isSword(currentItem));
			hasTool = isTool(currentItem);
			hasHandOrItem = (currentItem == null) || ((!hasSword) && (!hasTool));
			if ((!hasSword) && (hasHandOrItem))
				swapToBestSword();
		}
	}

	public void runAutoTool(boolean proactive)
	{
		if ((this.swappedInventoryState == 0) && ((proactive) || (mc.gameSettings.keyBindAttack.getIsKeyPressed())) && (mc.objectMouseOver != null))
		{
			if ((!mc.playerController.isInCreativeMode()) && 
				(ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.AUTOTOOL)) && 
					(mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK)) {
				Block block = mc.theWorld.getBlock(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ);
				ensureCorrectToolSelected(block);
			}
			else if ((ControlPackOptionsController.getBooleanOption(ControlPackEnumOptions.AUTOSWORD)) && 
				(mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY) && 
					((mc.objectMouseOver.entityHit instanceof EntityLivingBase))) {
				ensureSwordSelected();
			}
		}
	}
	
	public void handleInputEvent(int code, boolean down) {
		
		if (code == ControlPackKeybindingController.instance.keyBindAlternateLeft.getKeyCode()) {
			if (down) {
				this.swappedInventoryState = -1;
				mc.thePlayer.inventory.changeCurrentItem(1);
			}
			else if (this.swappedInventoryState != 0) {
				mc.thePlayer.inventory.changeCurrentItem(this.swappedInventoryState);
				this.swappedInventoryState = 0;
			}
		}
		if (code == ControlPackKeybindingController.instance.keyBindAlternateRight.getKeyCode()) {
			if (down) {
				this.swappedInventoryState = 1;
				mc.thePlayer.inventory.changeCurrentItem(-1);
			}
			else if (this.swappedInventoryState != 0) {
				mc.thePlayer.inventory.changeCurrentItem(this.swappedInventoryState);
				this.swappedInventoryState = 0;
			}
		}
	}

}
