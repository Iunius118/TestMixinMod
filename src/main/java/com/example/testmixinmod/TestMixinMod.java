package com.example.testmixinmod;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("testmixinmod")
public class TestMixinMod {
    public static final Logger LOGGER = LogManager.getLogger();

    public TestMixinMod() {
        // イベントリスナーを登録
        // TestEventHook.RotateItemInItemFrameEvent発生時にTestMixinMod#onRotatingItemInItemFrameが呼ばれるようになる
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRotatingItemInItemFrame(TestEventHook.RotateItemInItemFrameEvent event) {
        Player player = event.getPlayer();

        if (player.isSteppingCarefully()) {
            // プレイヤーがスニーク状態のとき
            ItemFrame itemFrame = event.getItemFrame();
            ItemStack stack = itemFrame.getItem();
            String itemName = stack.getItem().getName(stack).getString();
            String playerName = player.getDisplayName().getString();

            LOGGER.info("{} in an item frame at {} was rotated by {}.", itemName, itemFrame.blockPosition(), playerName);
        } else {
            // プレイヤーがスニーク状態でないとき
            // イベントをキャンセルしてアイテムフレーム内のアイテムを回転させないようにする
            event.setCanceled(true);
            LOGGER.info("Only sneaking player can rotate the item in the item frame.");
        }
    }
}
