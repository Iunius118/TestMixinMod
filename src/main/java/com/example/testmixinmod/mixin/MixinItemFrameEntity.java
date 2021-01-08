package com.example.testmixinmod.mixin;

import com.example.testmixinmod.TestEventHook;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class MixinItemFrameEntity extends HangingEntity {
    protected MixinItemFrameEntity(EntityType<? extends HangingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "processInitialInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/item/ItemFrameEntity;playSound(Lnet/minecraft/util/SoundEvent;FF)V",
                    ordinal = 0),
            cancellable = true) // 以下のメソッドをItemFrameEntity#processInitialInteract内の最初のthis.playSound(…)の直前に注入する
    private void onRotatingItemInItemFrame(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir) {
        // （アイテムフレーム内のアイテムを回転させる処理の直前）
        // RotateItemInItemFrameEventを発生させる
        if (TestEventHook.onRotatingItemInItemFrame(ItemFrameEntity.class.cast(this), player, hand)) {  // 実行時にはMixinによってthisがItemFrameEntityのインスタンスになる
            // イベントがキャンセルされたときはprocessInitialInteractの処理を中止してActionResultType.CONSUMEを返す
            cir.setReturnValue(ActionResultType.CONSUME);
            cir.cancel();
            // （アイテムフレーム内のアイテムを回転させる処理がキャンセルされる）
        }
    }
}
