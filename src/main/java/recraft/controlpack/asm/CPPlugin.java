package recraft.controlpack.asm;

import java.util.Map;

import recraft.controlpack.ControlPack;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class CPPlugin implements IFMLLoadingPlugin
{

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { CPTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass()
	{
		return ControlPack.class.getName();
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

}
