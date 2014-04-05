package recraft.controlpack;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class ControlPackOptionsController {
	public ControlPackEnumOptions[] allOptions = { 
		ControlPackEnumOptions.AUTOTOOL, 
		ControlPackEnumOptions.AUTOTOOLMODE, 
		ControlPackEnumOptions.AUTOBLOCK, 
		ControlPackEnumOptions.AUTOBLOCKMODE, 
		ControlPackEnumOptions.AUTOSWORD, 
		ControlPackEnumOptions.LOOKBEHINDBACK, 
		ControlPackEnumOptions.FRONTVIEW, 
		ControlPackEnumOptions.WINDOWRESTORE, 
		ControlPackEnumOptions.SMARTFURNACE, 
		ControlPackEnumOptions.HOLDTOATTACK, 
		ControlPackEnumOptions.USECOUNT, 
		ControlPackEnumOptions.STATUSLOCATION, 
		ControlPackEnumOptions.COORDINATESLOCATION, 
		ControlPackEnumOptions.CORPSELOCATION 
	};

	public ControlPackEnumOptions[] itemOptions = { 
		ControlPackEnumOptions.ITEM_SWORDS, 
		ControlPackEnumOptions.COORDINATE_FORMAT 
	};

	public Map<ControlPackEnumOptions, Float> floatOptions;
	public Map<ControlPackEnumOptions, Boolean> booleanOptions;
	public Map<ControlPackEnumOptions, String> stringOptions;
	public Map<ControlPackEnumOptions, Integer> intOptions;
	public Map<ControlPackEnumOptions, Integer> intOptionsMaxValue;
	public Map<String, ControlPackEnumOptions> volumeSettingsMap;
	private static Map<String, String> locStrings = new HashMap<String, String>();
	private File optionsFile;
	public ControlPackWindowStateRestoreData windowStateRestoreData;
	public static ControlPackOptionsController instance;
	
	public ControlPackOptionsController() {
		this.optionsFile = new File(Minecraft.getMinecraft().mcDataDir, "controlpack.txt");
		loadOptions();
		
		locStrings.put("gui.done", "Done");

		locStrings.put("key.walkDistance", "Run Distance");
		locStrings.put("key.walkDistance", "Run Distance");
		locStrings.put("key.lookBehind", "Look Behind");
		locStrings.put("key.toggleMine", "Toggle Mining");
		locStrings.put("key.toggleUse", "Toggle Use Item");
		locStrings.put("key.autoJump", "Toggle Jump");
		locStrings.put("key.autoRun", "Toggle Run");
		locStrings.put("key.toggleSneak", "Toggle Sneak");
		locStrings.put("key.altleft", "Swap L (Hold)");
		locStrings.put("key.altright", "Swap R (Hold)");
		locStrings.put("key.toggleGamma", "Toggle Brightness");
		locStrings.put("key.startSprint", "Start Sprinting");
		locStrings.put("key.placeTorch", "Place Torch");
		locStrings.put("key.eatFood", "Eat Food");
		locStrings.put("key.saylocation", "Chat Location");
		locStrings.put("key.waypoints", "Waypoints");

		locStrings.put("measure.go", "Go");
		locStrings.put("controlPack.title", "Welcome to ControlPack");
		locStrings.put("controlPack.volumeTitle", "ControlPack Settings - SFX Volume");
		locStrings.put("controlPack.optionsTitle", "ControlPack Settings - Options");
		locStrings.put("controlPack.itemOptionsTitle", "ControlPack Settings - Other Options");
		locStrings.put("controlPack.bindingsTitle", "ControlPack Settings - Key Bindings");
		locStrings.put("controlPack.waypointsTitle", "ControlPack Settings - Waypoints");

		locStrings.put("options.controlPack", "ControlPack...");
		locStrings.put("options.volume", "SFX Volume...");
		locStrings.put("options.cpbindings", "Extra Key Bindings...");
		locStrings.put("options.cpoptions", "Configure Options...");
		locStrings.put("options.cpwaypoints", "Waypoints...");
		locStrings.put("options.itemSettings", "Other Options...");
		locStrings.put("options.set", "Set");

		locStrings.put("options.item_swords", "Sword Item Ids");
		locStrings.put("options.coordinateformat", "Coord. Format");

		locStrings.put("options.lessrain", "Render Less Rain");
		locStrings.put("options.lookbehind-withback", "LookBehind w/ Back+Fwd");
		locStrings.put("options.front3rdperson", "Better 3rd Person View");
		locStrings.put("options.windowrestoration", "Auto Window Restore");
		locStrings.put("options.smartfurnace", "Smart Furnace");
		locStrings.put("options.holdtoattack", "Hold to Attack");
		locStrings.put("options.corpselocation", "Auto Corpse Waypoint");
		locStrings.put("options.volume_rain", "Rain Falling");
		locStrings.put("options.volume_piston", "Piston In & Out");
		locStrings.put("options.volume_splash", "Splashing Water");
		locStrings.put("options.volume_water", "Flowing Water");
		locStrings.put("options.volume_door", "Door & Chest");
		locStrings.put("options.volume_explode", "Explosion");
		locStrings.put("options.volume_bow", "Shoot With Bow");
		locStrings.put("options.volume_portal", "Near a Portal");
		locStrings.put("options.volume_eatdrink", "Eat & Drink");

		locStrings.put("options.volume_animals", "Peaceful Animals");
		locStrings.put("options.volume_slime", "Slimes");
		locStrings.put("options.volume_minecart", "Minecarts");
		locStrings.put("options.volume_hit", "Take Damage");
		locStrings.put("options.volume_dig", "Dug/Put Block");
		locStrings.put("options.volume_step", "Walk/Dig Sound");

		locStrings.put("options.autotool", "Auto Tool");
		locStrings.put("options.autotoolmode", "Auto Tool Mode");
		locStrings.put("options.autosword", "Auto Sword");
		locStrings.put("options.autoblock", "Auto Block");
		locStrings.put("options.autoblockmode", "Auto Block Mode");
		locStrings.put("options.voidfog", "Render Void Fog");
		locStrings.put("options.usecount", "Uses Remaining");
		locStrings.put("options.statuslocation", "Status Overlay");
		locStrings.put("options.coordinateslocation", "My Coordinates");

		locStrings.put("options.waypointhud1", "HUD");
		locStrings.put("options.waypointhud2", "HUD");
		locStrings.put("options.waypointhud3", "HUD");
		locStrings.put("options.waypointhud4", "HUD");
		locStrings.put("options.waypointhud5", "HUD");

		locStrings.put("options.waypointhudnether1", "HUD");
		locStrings.put("options.waypointhudnether2", "HUD");
		locStrings.put("options.waypointhudnether3", "HUD");
		locStrings.put("options.waypointhudnether4", "HUD");
		locStrings.put("options.waypointhudnether5", "HUD");

	}
	

	public static String translate(String key)
	{
		return locStrings.get(key);
	}
	
	public void loadOptions() {
		try {


			this.floatOptions = new HashMap<ControlPackEnumOptions, Float>();

			this.stringOptions = new HashMap<ControlPackEnumOptions, String>();

			this.stringOptions.put(ControlPackEnumOptions.ITEM_SWORDS, "");
			this.stringOptions.put(ControlPackEnumOptions.COORDINATE_FORMAT, "{X}, {Z}, {Y}");

			this.booleanOptions = new HashMap<ControlPackEnumOptions, Boolean>();

			this.booleanOptions.put(ControlPackEnumOptions.FRONTVIEW, Boolean.valueOf(true));
			this.booleanOptions.put(ControlPackEnumOptions.WINDOWRESTORE, Boolean.valueOf(true));
			this.booleanOptions.put(ControlPackEnumOptions.SMARTFURNACE, Boolean.valueOf(true));
			this.booleanOptions.put(ControlPackEnumOptions.HOLDTOATTACK, Boolean.valueOf(false));
			this.booleanOptions.put(ControlPackEnumOptions.LOOKBEHINDBACK, Boolean.valueOf(true));
			this.booleanOptions.put(ControlPackEnumOptions.AUTOTOOL, Boolean.valueOf(true));
			this.booleanOptions.put(ControlPackEnumOptions.AUTOSWORD, Boolean.valueOf(true));
			this.booleanOptions.put(ControlPackEnumOptions.AUTOBLOCK, Boolean.valueOf(true));

			this.booleanOptions.put(ControlPackEnumOptions.USECOUNT, Boolean.valueOf(true));
			this.booleanOptions.put(ControlPackEnumOptions.WELCOMENAG, Boolean.valueOf(true));

			this.booleanOptions.put(ControlPackEnumOptions.CORPSELOCATION, Boolean.valueOf(true));

			this.intOptions = new HashMap<ControlPackEnumOptions, Integer>();
			this.intOptions.put(ControlPackEnumOptions.AUTOTOOLMODE, Integer.valueOf(0));
			this.intOptions.put(ControlPackEnumOptions.AUTOBLOCKMODE, Integer.valueOf(0));
			this.intOptions.put(ControlPackEnumOptions.STATUSLOCATION, Integer.valueOf(0));
			this.intOptions.put(ControlPackEnumOptions.COORDINATESLOCATION, Integer.valueOf(1));

			this.intOptionsMaxValue = new HashMap<ControlPackEnumOptions, Integer>();
			this.intOptionsMaxValue.put(ControlPackEnumOptions.AUTOTOOLMODE, Integer.valueOf(3));
			this.intOptionsMaxValue.put(ControlPackEnumOptions.AUTOBLOCKMODE, Integer.valueOf(10));
			this.intOptionsMaxValue.put(ControlPackEnumOptions.STATUSLOCATION, Integer.valueOf(4));
			this.intOptionsMaxValue.put(ControlPackEnumOptions.COORDINATESLOCATION, Integer.valueOf(4));

			if (!this.optionsFile.exists()) {
				return;
			}
			windowStateRestoreData = new ControlPackWindowStateRestoreData();
			BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
			for (String s = ""; (s = bufferedreader.readLine()) != null; ) {
				try {
					String[] as = s.split(":");
					if (as.length >= 2)
					{
						if (as[0].equals("last-fullscreen")) {
							windowStateRestoreData.fullScreen = as[1].equals("true");
						}
						else if (as[0].equals("last-pos-x")) {
							windowStateRestoreData.x = Integer.parseInt(as[1]);
							windowStateRestoreData.positionExists = true;
						}
						else if (as[0].equals("last-pos-y")) {
							windowStateRestoreData.y = Integer.parseInt(as[1]);
							windowStateRestoreData.positionExists = true;
						}
						else if (as[0].equals("last-width")) {
							windowStateRestoreData.width = Integer.parseInt(as[1]);
							windowStateRestoreData.positionExists = true;
						}
						else if (as[0].equals("last-height")) {
							windowStateRestoreData.height = Integer.parseInt(as[1]);
							windowStateRestoreData.positionExists = true;
						}
						else if (as[0].equals("last-windowstate")) {
							windowStateRestoreData.windowState = Integer.parseInt(as[1]);
						}
						else {
							ControlPackEnumOptions option = ControlPackEnumOptions.getOption(as[0]);
							if (option != null) {
								if (option.getIsBool()) {
									this.booleanOptions.put(option, Boolean.valueOf(as[1].equals("true")));
								}
								else if (option.getIsFloat()) {
									this.floatOptions.put(option, Float.valueOf(Float.parseFloat(as[1])));
								}
								else if (option.getIsString()) {
									this.stringOptions.put(option, as[1]);
								}
								else {
									this.intOptions.put(option, Integer.valueOf(Integer.parseInt(as[1])));
								}
							}
						}

					}
				} catch (Exception ex) {
					System.out.println("Skipping bad controlpack option: " + s + " --> " + ex.toString());
				}
			}

			bufferedreader.close();
		}
		catch (Exception exception)
		{
			System.out.println("Failed to load options");
			exception.printStackTrace();
		}
	}

	public void saveOptions() {
		try {
			PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
			printwriter.println("last-fullscreen:" + windowStateRestoreData.fullScreen);
			printwriter.println("last-pos-x:" + windowStateRestoreData.x);
			printwriter.println("last-pos-y:" + windowStateRestoreData.y);
			printwriter.println("last-width:" + windowStateRestoreData.width);
			printwriter.println("last-height:" + windowStateRestoreData.height);
			printwriter.println("last-windowstate:" + windowStateRestoreData.windowState);


			Enumeration<ControlPackEnumOptions> keys = Collections.enumeration(this.floatOptions.keySet());
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				ControlPackEnumOptions option = (ControlPackEnumOptions)key;
				Float value = this.floatOptions.get(option);
				printwriter.println(option.getName() + ":" + value);
			}

			keys = Collections.enumeration(this.booleanOptions.keySet());
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				ControlPackEnumOptions option = (ControlPackEnumOptions)key;
				Boolean value = this.booleanOptions.get(option);
				printwriter.println(option.getName() + ":" + value);
			}

			keys = Collections.enumeration(this.intOptions.keySet());
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				ControlPackEnumOptions option = (ControlPackEnumOptions)key;
				int value = this.intOptions.get(option).intValue();
				printwriter.println(option.getName() + ":" + value);
			}

			keys = Collections.enumeration(this.stringOptions.keySet());
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				ControlPackEnumOptions option = (ControlPackEnumOptions)key;
				String value = this.stringOptions.get(option);
				printwriter.println(option.getName() + ":" + value);
			}

			printwriter.close();
		}
		catch (Exception exception) {
			System.out.println("Failed to save controlpack options");
			exception.printStackTrace();
		}
	}

	public static void initOptions() throws Exception {
		if (instance != null)
			throw new Exception();
		instance = new ControlPackOptionsController();
	}
	
	public static boolean getBooleanOption(ControlPackEnumOptions enumoptionz) {
		return instance.booleanOptions.get(enumoptionz);
	}
	
	public static int getIntOption(ControlPackEnumOptions enumoptionz) {
		if (instance.intOptionsMaxValue.get(enumoptionz) != null) {
			if (instance.intOptions.get(enumoptionz) > instance.intOptionsMaxValue.get(enumoptionz)) {
				return 0;
			}
			return instance.intOptionsMaxValue.get(enumoptionz);
		}
		return instance.intOptions.get(enumoptionz);
	}
	
	public static float getFloatOption(ControlPackEnumOptions enumoptionz) {
		return instance.floatOptions.get(enumoptionz);
	}
	
	public static String getStringOption(ControlPackEnumOptions enumoptionz) {
		return instance.stringOptions.get(enumoptionz);
	}
	
	public static void setOption(ControlPackEnumOptions enumoptionz, boolean curval) {
		instance.booleanOptions.put(enumoptionz, curval);
	}
	
	public static void setOption(ControlPackEnumOptions enumoptionz, int curval) {
		if (instance.intOptionsMaxValue.get(enumoptionz) != null) {
			Integer value = curval;
			Integer maxValue = instance.intOptionsMaxValue.get(enumoptionz);
			Integer localInteger1 = value; 
			Integer localInteger2 = value = Integer.valueOf(value.intValue() + 1);
			if (value.intValue() > maxValue.intValue()) {
				value = Integer.valueOf(0);
			}
			instance.intOptions.put(enumoptionz, value);
			return;
		}
		instance.intOptions.put(enumoptionz, curval);
	}
	
	public static void setOption(ControlPackEnumOptions enumoptionz, float curval) {
		instance.floatOptions.put(enumoptionz, curval);
	}
	
	public static void setOption(ControlPackEnumOptions enumoptionz, String curval) {
		instance.stringOptions.put(enumoptionz, curval);
	}
	
}
