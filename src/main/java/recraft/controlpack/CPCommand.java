package recraft.controlpack;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CPCommand extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "controlpack";
	}

	@Override
	public String getCommandUsage(ICommandSender var1)
	{
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params)
	{
		if (params[0] != null)
		{
			switch (params[0])
			{
				default:
					helpCommand(sender);
					break;
			}

		}		
	}
	
	private void helpCommand(ICommandSender sender)
	{
	
	}

	
}
