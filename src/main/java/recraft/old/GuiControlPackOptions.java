package recraft.old;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiControlPackOptions extends GuiScreen
{
	protected ControlPackEnumOptions[] options;
	protected List<GuiTextFieldCP> textFields;
	private GuiScreen parentScreen;
	protected String screenTitle;
	@SuppressWarnings("unused")
	private int buttonId;

	public GuiControlPackOptions(GuiScreen parent)
	{
		this.textFields = new ArrayList<GuiTextFieldCP>();
		this.parentScreen = parent;
		this.screenTitle = mod_ControlPack.translate("controlPack.optionsTitle");
		this.buttonId = -1;
		this.options = ControlPackOptionsController.instance.allOptions;
	}

	private int func_20080_j()
	{
		return this.width / 2 - 155;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		int i = func_20080_j();
		int j = 0;

		for (int k = 0; k < this.options.length; k++) {
			ControlPackEnumOptions option = this.options[k];
			String optionDesc = mod_ControlPack.instance.getOptionDesc(option);
			if (option.getIsString())
			{
				//buttonList.add(new GuiSmallButtonCP(100 + option.getOrdinal(), i + (j % 2) * 160, height / 6 + 24 * (j >> 1), option, mod_ControlPack.instance.getOptionDesc(option)));
				GuiTextFieldCP field = new GuiTextFieldCP(this.fontRendererObj, mod_ControlPack.instance.getOptionDesc(option), i, this.height / 6 + 24 * j - 30, 300, 20, option);
				field.setText(ControlPackOptionsController.getStringOption(option));
				this.textFields.add(field);
				if (option == ControlPackEnumOptions.ITEM_SWORDS) {
					field.isIdList = true;
				}
			}
			else if (!option.getIsFloat()) {
				this.buttonList.add(new GuiSmallButtonCP(100 + option.getOrdinal(), i + j % 2 * 160, this.height / 6 + 24 * (j >> 1) - 30, option, optionDesc));
			}

			j++;
		}

		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 9 + 190, 200, 20, mod_ControlPack.translate("gui.done")));
	}

	private void saveFields()
	{
		// text fields don't update automatically
		for (int i = 0; i < this.textFields.size(); i++) {
			GuiTextFieldCP tf = this.textFields.get(i);
			ControlPackEnumOptions option = tf.option;
			String value = ControlPackOptionsController.getStringOption(option);
			ControlPackOptionsController.setOption(option, tf.getText());
		}
		ControlPackOptionsController.instance.saveOptions();
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 200) {
			saveFields();
			this.mc.displayGuiScreen(this.parentScreen);
		}
		else if ((guibutton instanceof GuiSmallButtonCP)) {
			GuiSmallButtonCP optionButton = (GuiSmallButtonCP)guibutton;
			ControlPackEnumOptions cpOption = optionButton.getOption();
			if (cpOption.getIsBool()) {
				Boolean value = ControlPackOptionsController.getBooleanOption(cpOption);
				ControlPackOptionsController.setOption(cpOption, Boolean.valueOf(!value.booleanValue()));
			}
			ControlPackOptionsController.instance.saveOptions();
			guibutton.displayString = mod_ControlPack.instance.getOptionDesc(optionButton.getOption());
		}
	}

	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 5, 16777215);
		super.drawScreen(i, j, f);
		for (int i2 = 0; i2 < this.textFields.size(); i2++) {
			GuiTextFieldCP tf = this.textFields.get(i2);
			tf.drawTextBox();
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		for (int l = 0; l < this.textFields.size(); l++)
			if (this.textFields.get(l) != null)
				((GuiTextField)this.textFields.get(l)).mouseClicked(i, j, k);
	}

	@Override
	protected void keyTyped(char c, int code)
	{
		for (int i = 0; i < this.textFields.size(); i++) {
			if ((this.textFields.get(i) != null) && (((GuiTextField)this.textFields.get(i)).isFocused())) {
				keyTyped(this.textFields.get(i), c, code);
			}
		}
		super.keyTyped(c, code);
	}

	private void keyTyped(GuiTextField field, char c, int code) {
		if (((field instanceof GuiTextFieldCP)) && (((GuiTextFieldCP)field).isIdList)) {
			if ((c == ',') || ((c >= '0') && (c <= '9')) || (code == 14)) {
				field.textboxKeyTyped(c, code);
			}
		}
		else
			field.textboxKeyTyped(c, code);
	}
}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.GuiControlPackOptions
 * JD-Core Version:		0.6.2
 */