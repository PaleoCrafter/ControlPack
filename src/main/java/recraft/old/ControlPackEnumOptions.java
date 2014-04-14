package recraft.old;

public enum ControlPackEnumOptions
{
	LESSRAIN("options.lessrain", false, true), // dead, but left in here just so the enum is stable...
	LOOKBEHINDBACK("options.lookbehind-withback", false, true), 
	FRONTVIEW("options.front3rdperson", false, true), 
	WINDOWRESTORE("options.windowrestoration", false, true), 
	SMARTFURNACE("options.smartfurnace", false, true), 
	HOLDTOATTACK("options.holdtoattack", false, true), 
	SOUNDMANAGER("options.soundmanager", false, true), 
	CORPSELOCATION("options.corpselocation", false, true), 

	AUTOTOOL("options.autotool", false, true), 
	AUTOTOOLMODE("options.autotoolmode", false, false), //int
	AUTOTOOLSWORD("options.autotoolsword", false, true), 
	AUTOBLOCKMODE("options.autoblockmode", false, false), //int
	AUTOSWORD("options.autosword", false, true), 
	AUTOBLOCK("options.autoblock", false, true), 
	WELCOMENAG("options.welcomenag", false, true), 
	VOIDFOG("options.voidfog", false, true), // dead, but left in here just so the enum is stable...
	USECOUNT("options.usecount", false, true), 
	STATUSLOCATION("options.statuslocation", false, false), //int
	COORDINATESLOCATION("options.coordinateslocation", false, false), //int

	ITEM_SWORDS("options.item_swords", false, false, true), //string
	COORDINATE_FORMAT("options.coordinateformat", false, false, true); //string

	private final String name;
	private final boolean isFloat;
	private final boolean isBool;
	private final boolean isString;

	public static ControlPackEnumOptions getOption(int i) { 
		ControlPackEnumOptions[] aenumoptions = values();
		int j = aenumoptions.length;
		for (int k = 0; k < j; k++) {
			ControlPackEnumOptions enumoptions = aenumoptions[k];
			if (enumoptions.getOrdinal() == i) {
				return enumoptions;
			}
		}
		return null; }

	public static ControlPackEnumOptions getOption(String name)
	{
		ControlPackEnumOptions[] aenumoptions = values();
		int j = aenumoptions.length;
		for (int k = 0; k < j; k++) {
			ControlPackEnumOptions enumoptions = aenumoptions[k];
			if (enumoptions.getName().equals(name)) {
				return enumoptions;
			}
		}
		return null;
	}

	private ControlPackEnumOptions(String name, boolean isFloat, boolean isBool, boolean isString) {
		this.name = name;
		this.isFloat = isFloat;
		this.isBool = isBool;
		this.isString = isString;
	}
	private ControlPackEnumOptions(String name, boolean isFloat, boolean isBool) {
		this.name = name;
		this.isFloat = isFloat;
		this.isBool = isBool;
		this.isString = false;
	}

	public boolean getIsFloat() {
		return this.isFloat;
	}

	public boolean getIsBool() {
		return this.isBool;
	}

	public boolean getIsString() {
		return this.isString;
	}

	public int getOrdinal() {
		return ordinal();
	}

	public String getName() {
		return this.name;
	}

	public String getLocKey() {
		return this.name;
	}
}

/* Location:					 /Users/julian/Documents/mcmodding/moddecompile/bon/
 * Qualified Name:		 net.minecraft.src.ControlPackEnumOptions
 * JD-Core Version:		0.6.2
 */