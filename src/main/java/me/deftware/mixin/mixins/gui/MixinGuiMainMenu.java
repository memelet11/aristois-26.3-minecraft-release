package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinGuiMainMenu {

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    public void render(GuiGraphicsExtractor matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (GameMap.INSTANCE.get(GameKeys.EMC_MAIN_MENU_OVERLAY, true)) {
            var font = Minecraft.getInstance().font;
            matrixStack.text(font, FrameworkConstants.toDataString(), 2, 2, 0xFFFFFFFF);
            matrixStack.text(font, "Aristois 26.3 | Updated Nov 16, 2026 | Maintained by Davy", 2, 12, 0xFFAAAAAA);
        }
    }

}
