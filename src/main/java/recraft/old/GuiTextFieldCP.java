package recraft.old;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldCP extends GuiTextField
{
	public ControlPackEnumOptions option;
	public String label;
	private int e;
	private int xpos;
	private int ypos;
	private FontRenderer a;
	public boolean isIdList;

	public GuiTextFieldCP(FontRenderer thefontRenderer, String thelabel, int x, int y, int width, int h, ControlPackEnumOptions opt)
	{
		super(thefontRenderer, x + 80, y, width - 80, h);
		this.xpos = x;
		this.ypos = y;
		this.label = thelabel;
		this.option = opt;
		this.a = thefontRenderer;
		this.e = h;
	}

	@Override
	public void drawTextBox() {
		super.drawTextBox();
		this.a.drawStringWithShadow(this.label, this.xpos, this.ypos + this.e / 2, -1);
	}
}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.GuiTextFieldCP
 * JD-Core Version:		0.6.2
 */