package recraft.old;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiMeasure extends GuiScreen
{
	private GuiTextField measureDistance;
	private String defaultDistance;

	public GuiMeasure(String defaultDist)
	{
		this.defaultDistance = defaultDist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		int i = this.height / 4 + 48;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, i, "Go"));
		this.measureDistance = new GuiTextField(this.fontRendererObj, this.width / 2 - 25, i - 35, 50, 20);
		this.measureDistance.setText("0".equalsIgnoreCase(this.defaultDistance) ? "" : this.defaultDistance);
		this.measureDistance.setFocused(true);
		this.measureDistance.setMaxStringLength(7);
	}

	@Override
	protected void keyTyped(char c, int i) {
		if ((this.measureDistance.isFocused()) && (((c >= '0') && (c <= '9')) || (i == 14))) {
			this.measureDistance.textboxKeyTyped(c, i);
		}
		if (c == '\r') {
			actionPerformed((GuiButton)this.buttonList.get(0));
		}
		super.keyTyped(c, i);
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		this.measureDistance.mouseClicked(i, j, k);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		this.measureDistance.drawTextBox();
		super.drawScreen(i, j, f);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if (guibutton.id == 0)
		{
			int distance = 0;
			try {
				distance = Integer.parseInt(this.measureDistance.getText());
			}
			catch (NumberFormatException ex) {
			}
			if (distance > 0) {
				mod_ControlPack.instance.moveByDistance(distance);
			}
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
		}
	}
}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.GuiMeasure
 * JD-Core Version:		0.6.2
 */