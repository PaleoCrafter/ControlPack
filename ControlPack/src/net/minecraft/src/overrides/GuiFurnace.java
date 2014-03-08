package net.minecraft.src.overrides;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFurnace extends GuiContainer
{
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
	private TileEntityFurnace furnaceInventory;

	public GuiFurnace(InventoryPlayer par1InventoryPlayer, TileEntityFurnace par2TileEntityFurnace)
	{
		super(new ContainerFurnace(par1InventoryPlayer, par2TileEntityFurnace));
		this.furnaceInventory = par2TileEntityFurnace;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = this.furnaceInventory.isInvNameLocalized() ? this.furnaceInventory.getInvName() : I18n.getString(this.furnaceInventory.getInvName());
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (this.furnaceInventory.isBurning())
		{
			int i1 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}

		int i1 = this.furnaceInventory.getCookProgressScaled(24);
		drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}

	private void putItBack(ItemStack stack, Slot emptySlot)
	{
		if ((stack != null) && (emptySlot != null))
			addToSendQueue(emptySlot.slotNumber, 0, null);
	}

	protected void mouseClicked(int row, int col, int mouseButton)
	{
		if ((mod_ControlPack.instance == null) || (!((Boolean)mod_ControlPack.instance.booleanOptions.get(ControlPackEnumOptions.SMARTFURNACE)).booleanValue())) {
			super.mouseClicked(row, col, mouseButton);
			return;
		}
		if ((mouseButton != 0) || ((!Keyboard.isKeyDown(42)) && (!Keyboard.isKeyDown(54))) || (getSlotAtPosition(row, col) == null) || (getSlotAtPosition(row, col).getStack() == null))
		{
			super.mouseClicked(row, col, mouseButton);
			return;
		}
		ItemStack currentItemStack = this.mc.thePlayer.inventory.getItemStack();
		Slot emptySlot = null;

		if (currentItemStack != null)
		{
			emptySlot = findEmptySlot();
			if (emptySlot == null) {
				super.mouseClicked(row, col, mouseButton);
				return;
			}

			addToSendQueue(emptySlot.slotNumber, 0, null);
		}
		Slot slot = getSlotAtPosition(row, col);
		ItemStack itemStack = slot.getStack();

		if (slot.slotNumber < 3)
		{
			dumpLoad(slot);
			putItBack(currentItemStack, emptySlot);
			return;
		}

		ItemStack fuelStack = this.furnaceInventory.getStackInSlot(1);
		ItemStack burningStack = this.furnaceInventory.getStackInSlot(0);

		boolean canSmelt = (itemStack != null) && (FurnaceRecipes.smelting().getSmeltingResult(itemStack.getItem().itemID) != null);

		canSmelt = (canSmelt) && ((burningStack == null) || ((burningStack.itemID == itemStack.itemID) && ((!itemStack.getItem().getHasSubtypes()) || (burningStack.getItemDamage() == itemStack.getItemDamage()))));
		if (canSmelt)
		{
			if (burningStack == null)
			{
				this.furnaceInventory.setInventorySlotContents(0, itemStack.copy());
				slot.putStack(null);

				addToSendQueue(slot.slotNumber, 0, itemStack);

				addToSendQueue(0, 0, null);

				putItBack(currentItemStack, emptySlot);
			}
			else if (burningStack.stackSize + itemStack.stackSize <= itemStack.getMaxStackSize())
			{
				ItemStack burningStackCopy = burningStack.copy();
				burningStack.stackSize += itemStack.stackSize;
				this.furnaceInventory.setInventorySlotContents(0, burningStack);
				slot.putStack(null);

				addToSendQueue(slot.slotNumber, 0, itemStack);

				addToSendQueue(0, 0, burningStackCopy);

				putItBack(currentItemStack, emptySlot);
			}
			else
			{
				int remainingSizeLeft = itemStack.getMaxStackSize() - burningStack.stackSize;
				ItemStack burningStackcopy = burningStack.copy();
				ItemStack itemStackCopy = itemStack.copy();
				burningStack.stackSize = itemStack.getMaxStackSize();
				this.furnaceInventory.setInventorySlotContents(0, burningStack);
				slot.decrStackSize(remainingSizeLeft);

				addToSendQueue(slot.slotNumber, 0, itemStackCopy);

				addToSendQueue(0, 0, burningStackcopy);

				addToSendQueue(slot.slotNumber, 0, null);

				putItBack(currentItemStack, emptySlot);
			}
		}
		else {
			int burnTime = TileEntityFurnace.getItemBurnTime(itemStack);
			if (burnTime > 0)
			{
				if ((fuelStack != null) && ((fuelStack.itemID != itemStack.itemID) || (fuelStack.getItemDamage() != itemStack.getItemDamage()))) {
					putItBack(currentItemStack, emptySlot);
					super.mouseClicked(row, col, mouseButton);
					return;
				}
				float smeltsPerItem = burnTime / 200.0F;
				int stacksNeeded = burningStack == null ? 0 : (int)Math.ceil(burningStack.stackSize / smeltsPerItem);
				int existingFuelSize = fuelStack == null ? 0 : fuelStack.stackSize;

				if (existingFuelSize < stacksNeeded) {
					int toDrop = Math.min(itemStack.stackSize, stacksNeeded - existingFuelSize);
					if (fuelStack == null)
					{
						ItemStack itemStackCopy = itemStack.copy();
						ItemStack fuelStackCopy = itemStack.copy();
						fuelStackCopy.stackSize = 0;
						fuelStack = itemStack.splitStack(toDrop);
						this.furnaceInventory.setInventorySlotContents(1, fuelStack);

						addToSendQueue(slot.slotNumber, 0, itemStackCopy);
						if (itemStack.stackSize <= 0)
						{
							slot.putStack(null);

							addToSendQueue(1, 0, null);
						}
						else
						{
							addToSendQueueDrop1(1, toDrop, fuelStackCopy);

							addToSendQueue(slot.slotNumber, 0, null);
						}
					}
					else {
						toDrop = Math.min(toDrop, fuelStack.getMaxStackSize() - fuelStack.stackSize);

						ItemStack itemStackCopy = itemStack.copy();
						ItemStack fuelStackCopy = fuelStack.copy();

						fuelStack.stackSize += toDrop;
						slot.decrStackSize(toDrop);

						addToSendQueue(slot.slotNumber, 0, itemStackCopy);
						if (slot.getStack() != null)
						{
							addToSendQueueDrop1(1, toDrop, fuelStackCopy);

							addToSendQueue(slot.slotNumber, 0, null);
						}
						else
						{
							addToSendQueue(1, 0, itemStackCopy);
						}

					}

				}

				putItBack(currentItemStack, emptySlot);
			}
			else {
				putItBack(currentItemStack, emptySlot);
				super.mouseClicked(row, col, mouseButton);
			}
		}
		slot.onSlotChanged();
		this.furnaceInventory.onInventoryChanged();
		this.inventorySlots.detectAndSendChanges();
	}

	private Slot findEmptySlot() {
		for (int i = 3; i < this.inventorySlots.inventorySlots.size(); i++) {
			Slot slot = this.inventorySlots.getSlot(i);
			if ((slot != null) && (!slot.getHasStack())) {
				return slot;
			}
		}
		return null;
	}

	private void dumpLoad(Slot fromSlot) {
		ItemStack stack = fromSlot.getStack();

		boolean holdingStack = false;
		for (int i = 3; i < this.inventorySlots.inventorySlots.size(); i++) {
			Slot slot = this.inventorySlots.getSlot(i);
			if (slot != null)
			{
				ItemStack existingStack = slot.getStack();
				if ((existingStack != null) && (existingStack.itemID == stack.itemID) && (existingStack.getItemDamage() == stack.getItemDamage()))
				{
					int canDrop = existingStack.getMaxStackSize() - existingStack.stackSize;
					if (canDrop > 0) {
						if (stack.stackSize <= canDrop)
						{
							ItemStack existingStackCopy = existingStack.copy();
							ItemStack stackCopy = stack.copy();

							existingStack.stackSize += stack.stackSize;
							stack.stackSize = 0;
							fromSlot.putStack(null);

							if (!holdingStack)
							{
								addToSendQueue(fromSlot.slotNumber, 0, stackCopy);
								holdingStack = true;
							}

							addToSendQueue(slot.slotNumber, 0, existingStackCopy);
							return;
						}

						ItemStack existingStackCopy = existingStack.copy();
						ItemStack stackCopy = stack.copy();

						existingStack.stackSize += canDrop;
						stack.stackSize -= canDrop;

						if (!holdingStack)
						{
							addToSendQueue(fromSlot.slotNumber, 0, stackCopy);
							holdingStack = true;
						}

						addToSendQueue(slot.slotNumber, 0, existingStackCopy);
					}
				}
			}
		}
		for (int i = 3; i < this.inventorySlots.inventorySlots.size(); i++) {
			Slot slot = this.inventorySlots.getSlot(i);
			if (slot != null)
			{
				ItemStack existingStack = slot.getStack();
				if (existingStack == null)
				{
					ItemStack stackCopy = stack.copy();

					slot.putStack(stack.copy());
					stack.stackSize = 0;
					fromSlot.putStack(null);

					if (!holdingStack)
					{
						addToSendQueue(fromSlot.slotNumber, 0, stackCopy);
						holdingStack = true;
					}

					addToSendQueue(slot.slotNumber, 0, null);
					return;
				}
			}
		}
	}

	private void addToSendQueue(int slotNumber, int mouseButton, ItemStack stack) { short actionnum = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);

		this.mc.getNetHandler().addToSendQueue(new Packet102WindowClick(this.inventorySlots.windowId, slotNumber, mouseButton, 0, stack, actionnum));
	}

	private void addToSendQueueDrop1(int slotNumber, int count, ItemStack stack)
	{
		for (int i = 0; i < count; i++) {
			ItemStack copy = stack.copy();
			addToSendQueue(slotNumber, 1, copy.stackSize == 0 ? null : copy);
			stack.stackSize += 1;
		}
	}

	private Slot getSlotAtPosition(int i, int j) {
		for (int k = 0; k < this.inventorySlots.inventorySlots.size(); k++) {
			Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);
			if (getIsMouseOverSlot(slot, i, j)) {
				return slot;
			}
		}

		return null;
	}

	private boolean getIsMouseOverSlot(Slot slot, int i, int j)
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		i -= k;
		j -= l;
		return (i >= slot.xDisplayPosition - 1) && (i < slot.xDisplayPosition + 16 + 1) && (j >= slot.yDisplayPosition - 1) && (j < slot.yDisplayPosition + 16 + 1);
	}
}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.GuiFurnace
 * JD-Core Version:		0.6.2
 */