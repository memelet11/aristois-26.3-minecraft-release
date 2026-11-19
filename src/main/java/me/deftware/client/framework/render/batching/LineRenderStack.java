package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

/**
 * @author Deftware
 */
public class LineRenderStack extends RenderStack<LineRenderStack> {

	public static final long RGB_CYCLE_MS = 4000L;

	private Vec3 eyes;

	public static Color getRgbTracerColor() {
		return getRgbTracerColor(System.currentTimeMillis());
	}

	public static Color getRgbTracerColor(long timeMs) {
		float hue = Math.floorMod(timeMs, RGB_CYCLE_MS) / (float) RGB_CYCLE_MS;
		return Color.getHSBColor(hue, 1.0f, 1.0f);
	}

	@Override
	public LineRenderStack begin(GLX context) {
		return begin(context, GL11.GL_LINES);
	}

	@Override
	public LineRenderStack begin(GLX context, int mode) {
		net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
		if (mc != null && mc.gameRenderer != null && mc.gameRenderer.mainCamera() != null && mc.gameRenderer.mainCamera().isInitialized()) {
			eyes = new Vec3(mc.gameRenderer.mainCamera().forwardVector());
		} else {
			eyes = new Vec3(0.0D, 0.0D, 1.0D);
			if (Minecraft.getMinecraftGame().getCamera() != null)
				eyes = eyes.xRot(-(float) Math.toRadians(Minecraft.getMinecraftGame().getCamera()._getRotationPitch()))
					.yRot(-(float) Math.toRadians(Minecraft.getMinecraftGame().getCamera()._getRotationYaw()));
		}
		return super.begin(context, mode);
	}

	public LineRenderStack drawLine(float x1, float y1, float x2, float y2) {
		// Scale
		if (scaled) {
			x1 *= getScale();
			y1 *= getScale();
			x2 *= getScale();
			y2 *= getScale();
		}
		// Draw
		vertex(x1, y1, 0).next();
		vertex(x2, y2, 0).next();
		return this;
	}

	public void vertex(double x, double y) {
		vertex(x, y, 0).next();
	}

	public LineRenderStack lineToBlockPosition(BlockPosition pos) {
		var cam = Minecraft.getMinecraftGame().getCamera();
		return drawRgbTracerLine(
				pos.getX() + 0.5D - cam._getRenderPosX(),
				pos.getY() + 0.5D - cam._getRenderPosY(),
				pos.getZ() + 0.5D - cam._getRenderPosZ()
		);
	}

	public LineRenderStack lineToEntity(TileEntity entity) {
		return lineToBlockPosition(entity.getBlockPosition());
	}

	public LineRenderStack lineToEntity(Entity entity) {
		var cam = Minecraft.getMinecraftGame().getCamera();
		net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
		net.minecraft.world.entity.Entity mcEntity = entity.getMinecraftEntity();
		double x, y, z;
		if (mc != null && mc.getDeltaTracker() != null && mcEntity != null) {
			float tickDelta = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
			Vec3 pos = mcEntity.getPosition(tickDelta);
			x = pos.x;
			y = pos.y + entity.getHeight() / 2.0F;
			z = pos.z;
		} else {
			x = entity.getPosX();
			y = entity.getPosY() + entity.getHeight() / 2.0F;
			z = entity.getPosZ();
		}
		return drawRgbTracerLine(
				x - cam._getRenderPosX(),
				y - cam._getRenderPosY(),
				z - cam._getRenderPosZ()
		);
	}

	private LineRenderStack drawRgbTracerLine(double x, double y, double z) {
		float prevR = red;
		float prevG = green;
		float prevB = blue;
		float prevA = alpha;
		try {
			Color rgb = getRgbTracerColor();
			red = rgb.getRed() / 255.0F;
			green = rgb.getGreen() / 255.0F;
			blue = rgb.getBlue() / 255.0F;
			alpha = 1.0F;
			return drawLine(x, y, z);
		} finally {
			red = prevR;
			green = prevG;
			blue = prevB;
			alpha = prevA;
		}
	}

	public LineRenderStack drawPoint(double x, double y, double z) {
		vertex(x, y, z).next();
		return this;
	}

	public LineRenderStack drawLine(double x, double y, double z) {
		return drawPoint(eyes.x, eyes.y, eyes.z).drawPoint(x, y, z);
	}

}
