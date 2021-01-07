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
            require = 1,
            cancellable = true) // 以下のメソッドをItemFrameEntity#processInitialInteract内の最初のthis.playSound(…)の直前に注入する
    private void onProcessInitialInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> cir) {
        // （アイテムフレーム内のアイテムの回転処理の直前）
        // イベントRotateItemInItemFrameEventを発生させる
        if (TestEventHook.onRotatingItemInItemFrame(ItemFrameEntity.class.cast(this), player, hand)) {  // 実行時にはMixinによってthisがItemFrameEntityのインスタンスになる
            // イベントがキャンセルされたらprocessInitialInteractの処理を中止してActionResultType.CONSUMEを返す
            // （アイテムフレーム内のアイテムの回転がキャンセルされる）
            cir.setReturnValue(ActionResultType.CONSUME);
            cir.cancel();
        }
    }
}
