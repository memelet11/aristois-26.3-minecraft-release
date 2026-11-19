package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventBlockBreakingSpeed;
import me.deftware.client.framework.event.events.EventSneakingCheck;
import me.deftware.client.framework.event.events.EventSpectator;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayerEntity {

    @Shadow @Final private Abilities abilities;

    @Redirect(method = "maybeBackOffFromEdge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isStayingOnGroundSurface()Z"))
    private boolean sneakingCheck(Player self) {
        if (self == Minecraft.getInstance().player) {
            EventSneakingCheck event = new EventSneakingCheck(self.isShiftKeyDown());
            event.broadcast();
            return event.isSneaking();
        }
        return self.isShiftKeyDown();
    }

    @Inject(method = "getDestroySpeed", at = @At(value = "RETURN"), cancellable = true)
    public void onGetBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        EventBlockBreakingSpeed event = new EventBlockBreakingSpeed().broadcast();
        cir.setReturnValue(cir.getReturnValue() * event.getMultiplier());
    }

    @Unique
    private Double getExtendedReachOverride() {
        var map = GameMap.INSTANCE;
        if (!map.get(GameKeys.EXTENDED_REACH, false)) {
            return null;
        }
        Player self = (Player) (Object) this;
        boolean allowed = self instanceof net.minecraft.client.player.LocalPlayer
                || (self instanceof net.minecraft.server.level.ServerPlayer sp
                    && sp.level().getServer() != null
                    && sp.level().getServer().isSingleplayer());
        if (!allowed) {
            return null;
        }
        Number value = map.get(GameKeys.BLOCK_REACH_DISTANCE, null);
        return value != null ? value.doubleValue() : 5.0D;
    }

    @Inject(method = "blockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetBlockReachDistance(CallbackInfoReturnable<Double> cir) {
        Double override = getExtendedReachOverride();
        if (override != null) {
            cir.setReturnValue(override);
        }
    }

    @Inject(method = "entityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetEntityReachDistance(CallbackInfoReturnable<Double> cir) {
        Double override = getExtendedReachOverride();
        if (override != null) {
            cir.setReturnValue(override);
        }
    }

    @Inject(method = "isWithinAttackRange", at = @At("HEAD"), cancellable = true)
    private void onIsWithinAttackRange(ItemStack stack, AABB box, double tolerance, CallbackInfoReturnable<Boolean> cir) {
        Double override = getExtendedReachOverride();
        if (override != null && box != null) {
            Player self = (Player) (Object) this;
            double maxRange = override + tolerance;
            cir.setReturnValue(box.distanceToSqr(self.getEyePosition()) < maxRange * maxRange);
        }
    }

    @Inject(method = "isSpectator", at = @At("RETURN"), cancellable = true)
    private void onIsSpectator(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractClientPlayer) {
            EventSpectator event = new EventSpectator(cir.getReturnValue());
            event.broadcast();
            cir.setReturnValue(event.isSpectator());
        }
    }

}
