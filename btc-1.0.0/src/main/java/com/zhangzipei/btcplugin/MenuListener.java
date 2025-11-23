package com.zhangzipei.btcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        // 使用 getOriginalTitle() 替代已弃用的 getTitle()
        String title = event.getView().getOriginalTitle();
        Player player = (Player) event.getWhoClicked();
        EconomyManager economy = BTCPlugin.getInstance().getEconomyManager();

        // 检查点击的是否是我们的菜单（通过标题判断）
        if (clickedInventory == null || !title.contains("菜单")) {
            return;
        }

        // 非常重要：取消点击事件，防止玩家把菜单里的物品拿出来
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        // 处理点击"每日签到"按钮
        if (clickedItem.getType() == Material.WRITABLE_BOOK && title.contains("主菜单")) {
            // TODO: 这里应该加入日期判断逻辑，防止重复签到。目前是每次点击都给钱。
            economy.addBalance(player, 10);
            player.sendMessage("§a[成功] §f签到成功！你获得了 §e10 BTC§f。");
            player.closeInventory(); // 关闭菜单
            // 重新打开菜单，让玩家看到新余额
            MenuManager.openMainMenu(player);
        }

        // 处理点击"我的钱包"按钮
        if (clickedItem.getType() == Material.GOLD_INGOT && title.contains("主菜单")) {
            MenuManager.openTransferPlayerList(player); // 打开玩家列表
        }

        // 处理在玩家列表中选择了一个玩家（头颅）
        if (clickedItem.getType() == Material.PLAYER_HEAD && title.contains("选择转账对象")) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta.hasDisplayName()) {
                String targetName = meta.getDisplayName().replace("§b", "");
                Player target = Bukkit.getPlayerExact(targetName);
                if (target != null && !target.equals(player)) {
                    player.sendMessage("§6[输入] §f请在聊天栏中输入要转账给 §b" + targetName + " §f的BTC数量：");
                    player.closeInventory();
                    // 注意：这里需要实现一个步骤，监听玩家接下来在聊天栏输入的数字。
                    // 由于实现起来稍复杂，这属于进阶功能。你可以先完成基础菜单，再通过查阅Bukkit Wiki来完善"聊天输入"功能。
                    // 基本思路是：用一个HashMap临时记录玩家和他们的转账目标，然后监听 AsyncPlayerChatEvent 来获取金额。
                }
            }
        }
    }
}