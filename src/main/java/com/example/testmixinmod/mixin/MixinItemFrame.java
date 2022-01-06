package com.example.testmixinmod.mixin;

import com.example.testmixinmod.TestEventHook;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class MixinItemFrame extends HangingEntity {
    protected MixinItemFrame(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/ItemFrame;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V",
                    ordinal = 0),
            cancellable = true)
            // 以下のメソッドをItemFrame#interact内の最初のthis.playSound(…)の直前に注入する
    private void fireRotateItemInItemFrameEvent(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        // （アイテムフレームがプレイヤーから右クリックされてアイテムフレーム内のアイテムを回転させる処理の直前）
        // RotateItemInItemFrameEventを発生させる
        // thisは実行時にMixinによってItemFrameのインスタンスになる
        if (TestEventHook.onRotatingItemInItemFrame(ItemFrame.class.cast(this), player, hand)) {
            // イベントがキャンセルされたときはItemFrame#interactの処理を中断してInteractionResult.CONSUMEを返す
            cir.setReturnValue(InteractionResult.CONSUME);
            cir.cancel();
            // （アイテムフレーム内のアイテムを回転させる処理がキャンセルされる）
        }
    }
}
