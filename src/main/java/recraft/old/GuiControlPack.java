package recraft.old;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiControlPack extends GuiScreen
{
	protected String screenTitle;

	public GuiControlPack()
	{
		this.screenTitle = ("Control Pack");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		this.screenTitle = mod_ControlPack.translate("controlPack.title");

		this.buttonList.add(new GuiButton(203, this.width / 2 - 75, this.height / 9 + 105, 150, 20, mod_ControlPack.translate("options.cpoptions")));

		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 9 + 205, 200, 20, mod_ControlPack.translate("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 203) {
			this.mc.displayGuiScreen(new GuiControlPackOptions(this));
		}
		else if (guibutton.id == 200)
			this.mc.displayGuiScreen(null);
	}

	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		//drawCenteredString(fontRenderer, screenTitle, width / 2, 15, 0xffffff);
		drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 5, 16777215);

		drawCenteredString(this.fontRendererObj, "Here you can configure all of ControlPacks key bindings, turn", this.width / 2, 30, 16777215);
		drawCenteredString(this.fontRendererObj, "features on and off, and adjust sound effect volume levels.", this.width / 2, 45, 16777215);

		drawCenteredString(this.fontRendererObj, "***** ALSO READ \"README.TXT\" THAT SHIPS WITH THE MOD! *****", this.width / 2, 70, 16711680);
		drawCenteredString(this.fontRendererObj, "It contains great information about the features that you", this.width / 2, 85, 11184810);
		drawCenteredString(this.fontRendererObj, " WILL NOT figure out on your own!", this.width / 2, 100, 11184810);
		super.drawScreen(i, j, f);
	}
}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.GuiControlPack
 * JD-Core Version:		0.6.2
 */