package me.deftware.client.framework.input;

import com.mojang.blaze3d.platform.InputConstants;
import me.deftware.mixin.imp.IMixinKeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public enum MinecraftKeyBind {

	SNEAK(Minecraft.getInstance().options.keyShift),
	USE_ITEM(Minecraft.getInstance().options.keyUse),
	JUMP(Minecraft.getInstance().options.keyJump),
	SPRINT(Minecraft.getInstance().options.keySprint),
	FORWARD(Minecraft.getInstance().options.keyUp),
	BACK(Minecraft.getInstance().options.keyDown),
	LEFT(Minecraft.getInstance().options.keyLeft),
	RIGHT(Minecraft.getInstance().options.keyRight),
	ATTACK(Minecraft.getInstance().options.keyAttack);

	private final KeyMapping bind;

	public boolean isHeld() {
		InputConstants.Key key = ((IMixinKeyBinding) this.bind).getInput();
		if (key == null || key.equals(InputConstants.UNKNOWN) || key.getValue() < 0) {
			return false;
		}
		if (key.getType() == InputConstants.Type.MOUSE) {
			int glfwButton = org.lwjgl.glfw.GLFW.sdlButtonToGlfwButton(key.getValue());
			if (glfwButton == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
					|| glfwButton == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT
					|| glfwButton == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
				var window = Minecraft.getInstance().getWindow();
				return window != null && org.lwjgl.glfw.GLFW.glfwGetMouseButton(window.handle(), glfwButton) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
			}
			return this.bind.isDown();
		}
		return InputConstants.isKeyDown(key.getValue());
	}

	public boolean isPressed() {
		return this.bind.isDown();
	}

	public void setPressed(boolean state) {
		this.bind.setDown(state);
	}

	MinecraftKeyBind(KeyMapping bind) {
		this.bind = bind;
	}

}
