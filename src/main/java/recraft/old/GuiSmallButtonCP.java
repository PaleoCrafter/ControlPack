package recraft.old;

import net.minecraft.client.gui.GuiButton;

public class GuiSmallButtonCP extends GuiButton
{
	private final ControlPackEnumOptions option;

	public GuiSmallButtonCP(int i, int j, int k, String s)
	{
		this(i, j, k, null, s);
	}

	public GuiSmallButtonCP(int i, int j, int k, int l, int i1, String s) {
		super(i, j, k, l, i1, s);
		this.option = null;
	}

	public GuiSmallButtonCP(int id, int x, int y, int width, int height, ControlPackEnumOptions option, String s) {
		super(id, x, y, width, height, s);
		this.option = option;
	}

	public GuiSmallButtonCP(int i, int j, int k, ControlPackEnumOptions option, String s) {
		super(i, j, k, 150, 20, s);
		this.option = option;
	}

	public ControlPackEnumOptions getOption() {
		return this.option;
	}
}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.GuiSmallButtonCP
 * JD-Core Version:		0.6.2
 */