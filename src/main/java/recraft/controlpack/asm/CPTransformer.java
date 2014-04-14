package recraft.controlpack.asm;

import static org.objectweb.asm.Opcodes.*;
import net.malisis.core.asm.AsmHook;
import net.malisis.core.asm.MalisisClassTransformer;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class CPTransformer extends MalisisClassTransformer
{
	@Override
	public void registerHooks()
	{
		register(autorunHook());		
	}
	
	
	public AsmHook autorunHook()
	{
		AsmHook ah = new AsmHook("net.minecraft.util.MovementInputFromOptions", "updatePlayerMoveState", "()V");
//		if (CPHooks.gameSettings.keyBindForward.getIsKeyPressed() || CPControls.getAutorun())

		LabelNode trueLabel = new LabelNode();
		
		//|| CPControls.getAutorun()
		InsnList insert1 = new InsnList();
		insert1.add(new JumpInsnNode(IFNE, trueLabel));
		insert1.add(new MethodInsnNode(INVOKESTATIC, "recraft/controlpack/CPControls", "getAutorun", "()Z"));
		
		//if (this.gameSettings.keyBindForward.getIsKeyPressed()
		InsnList match1 = new InsnList();
		match1.add(new VarInsnNode(ALOAD, 0)); // this
		match1.add(new FieldInsnNode(GETFIELD, "net/minecraft/util/MovementInputFromOptions", "gameSettings",
				"Lnet/minecraft/client/settings/GameSettings;")); //.gameSettings
		match1.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/settings/GameSettings", "keyBindForward",
				"Lnet/minecraft/client/settings/KeyBinding;")); //.keyBindForward
		match1.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/settings/KeyBinding", "getIsKeyPressed", "()Z")); //.getIsKeyPressed()
		
		//if (this.gameSettings.keyBindBack.getIsKeyPressed()
		InsnList match2 = new InsnList();
		match2.add(new VarInsnNode(ALOAD, 0)); // this
		match2.add(new FieldInsnNode(GETFIELD, "net/minecraft/util/MovementInputFromOptions", "gameSettings",
				"Lnet/minecraft/client/settings/GameSettings;")); //.gameSettings
		match2.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/settings/GameSettings", "keyBindBack",
				"Lnet/minecraft/client/settings/KeyBinding;")); //.keyBindBack
		match2.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/settings/KeyBinding", "getIsKeyPressed", "()Z")); //.getIsKeyPressed()
		
	
		return ah.jumpAfter(match1).insert(insert1).next().insert(trueLabel);
	}
	
}
