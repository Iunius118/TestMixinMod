package com.example.testmixinmod;

import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class TestEventHook {
    public static boolean onRotatingItemInItemFrame(ItemFrameEntity itemFrameEntity, PlayerEntity playerIn, Hand handIn) {
        return MinecraftForge.EVENT_BUS.post(new RotateItemInItemFrameEvent(itemFrameEntity, playerIn, handIn));
    }

    @Cancelable
    public static class RotateItemInItemFrameEvent extends Event {
        private final ItemFrameEntity itemFrame;
        private final PlayerEntity entityPlayer;
        private final Hand hand;

        public RotateItemInItemFrameEvent(ItemFrameEntity itemFrameEntity, PlayerEntity playerIn, Hand handIn) {
            itemFrame = itemFrameEntity;
            entityPlayer = playerIn;
            hand = handIn;
        }

        public PlayerEntity getPlayer() {
            return entityPlayer;
        }

        public Hand getHand() {
            return hand;
        }

        public ItemFrameEntity getItemFrameEntity() {
            return itemFrame;
        }
    }
}
