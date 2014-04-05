package recraft.controlpack;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.profiler.Profiler;

public class ControlPackPrivateValues {

	public static void setPrivateValueByType(Class<Minecraft> sourceClass, Object source, Class<Profiler> fieldClass, Object newValue)
	{
		Field[] fields = sourceClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.getType() == fieldClass) {
				try {
					field.setAccessible(true);
					field.set(source, newValue);
				}
				catch (IllegalAccessException ex)
				{
					throw new RuntimeException("setPrivateValue: ", ex);
				}
				catch (IllegalArgumentException ex) {
					throw new RuntimeException("setPrivateValue: ", ex);
				}
				catch (SecurityException ex) {
					throw new RuntimeException("setPrivateValue: ", ex);
				}
				return;
			}
		}
		throw new RuntimeException("setPrivateValueByType: Could not find type " + fieldClass.getName());
	}

	public static void setPrivateValue(Class<EntityRenderer> var0, Object var1, String nonObfName, String obfName, Object var3)
	{
		try
		{
			Field field;
			try
			{
				field = var0.getDeclaredField(obfName);
			}
			catch (NoSuchFieldException ex) {
				field = var0.getDeclaredField(nonObfName);
			}

			field.setAccessible(true);
			field.set(var1, var3);
		}
		catch (IllegalAccessException ex)
		{
			throw new RuntimeException("setPrivateValue: ", ex);
		}
		catch (IllegalArgumentException ex) {
			throw new RuntimeException("setPrivateValue: ", ex);
		}
		catch (SecurityException ex) {
			throw new RuntimeException("setPrivateValue: ", ex);
		}
		catch (NoSuchFieldException ex) {
			throw new RuntimeException("setPrivateValue: ", ex);
		}
	}
}
