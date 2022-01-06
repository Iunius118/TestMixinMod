package com.example.testmixinmod;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class TestEventHook {
    /**
     * TestEventHook.RotateItemInItemFrameEventを発生させるメソッド。
     *
     * @see RotateItemInItemFrameEvent
     * @param itemFrame アイテムを回転させられようとしているアイテムフレーム
     * @param playerIn アイテムを回転させようとしたプレイヤー
     * @param handIn アイテムを回転させようとしたプレイヤーの手（利き手か逆の手か）
     * @return イベントがキャンセルされたか。キャンセルされるとtrueが返る
     */
    public static boolean onRotatingItemInItemFrame(ItemFrame itemFrame, Player playerIn, InteractionHand handIn) {
        // ForgeのイベントバスにRotateItemInItemFrameEventのインスタンスをpostしてイベントを発生させる
        return MinecraftForge.EVENT_BUS.post(new RotateItemInItemFrameEvent(itemFrame, playerIn, handIn));
    }

    /**
     * プレイヤーが右クリックでアイテムフレーム内のアイテムを回転させようとしたときに発生するイベント。<br>
     * キャンセル可能。キャンセルするとアイテムフレーム内のアイテムは回転しない。
     */
    @Cancelable
    public static class RotateItemInItemFrameEvent extends Event {
        private final ItemFrame itemFrame;
        private final Player player;
        private final InteractionHand hand;

        public RotateItemInItemFrameEvent(ItemFrame itemFrameIn, Player playerIn, InteractionHand handIn) {
            itemFrame = itemFrameIn;
            player = playerIn;
            hand = handIn;
        }

        public Player getPlayer() {
            return player;
        }

        public InteractionHand getHand() {
            return hand;
        }

        public ItemFrame getItemFrame() {
            return itemFrame;
        }
    }
}
