package me.deftware.mixin.mixins.world;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

@Mixin(LevelChunk.class)
public class MixinWorldChunk {

	@Shadow
	@Final
	Level level;

	@Inject(method = "updateBlockEntityTicker", at = @At("HEAD"))
	private <T extends BlockEntity> void test(T blockEntity, CallbackInfo ci) {
		BlockState blockState = blockEntity.getBlockState();
		if (this.level != null && this.level.isClientSide() && blockState.getTicker(this.level, blockEntity.getType()) != null) {
			long pos = blockEntity.getBlockPos().asLong();
			HashMap<Long, BlockEntity> entities = ((me.deftware.client.framework.world.World) level).getInternalLongToBlockEntity();
			if (entities != null) {
				entities.put(pos, blockEntity);
			}
		}
	}

	@Inject(method = "setBlockEntity", at = @At("TAIL"))
	private void emc$onSetBlockEntity(BlockEntity blockEntity, CallbackInfo ci) {
		if (this.level != null && this.level.isClientSide() && blockEntity != null && !blockEntity.isRemoved()) {
			((me.deftware.client.framework.world.World) this.level).emcRegisterBlockEntity(blockEntity);
		}
	}

	@Inject(method = "removeBlockEntity", at = @At("TAIL"))
	private void emc$onRemoveBlockEntity(net.minecraft.core.BlockPos pos, CallbackInfo ci) {
		if (this.level != null && this.level.isClientSide() && pos != null) {
			((me.deftware.client.framework.world.World) this.level).emcRemoveBlockEntity(pos);
		}
	}

	@Inject(method = "clearAllBlockEntities", at = @At("HEAD"))
	private void emc$onClearAllBlockEntities(CallbackInfo ci) {
		if (this.level != null && this.level.isClientSide()) {
			var world = (me.deftware.client.framework.world.World) this.level;
			for (net.minecraft.core.BlockPos pos : ((LevelChunk) (Object) this).getBlockEntities().keySet()) {
				world.emcRemoveBlockEntity(pos);
			}
		}
	}

}
