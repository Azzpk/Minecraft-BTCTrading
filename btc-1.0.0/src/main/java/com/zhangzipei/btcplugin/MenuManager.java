package com.zhangzipei.btcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class MenuManager {

    public static void openMainMenu(Player player) {
        // 创建一个有27格大小（3行）的箱子界面作为主菜单
        Inventory menu = Bukkit.createInventory(player, 27, "§8§lBTC 主菜单");

        // 创建"签到"按钮
        ItemStack signButton = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta signMeta = signButton.getItemMeta();
        signMeta.setDisplayName("§e§l每日签到");
        signMeta.setLore(Arrays.asList("§7点击领取每日10BTC奖励！", "§6轻松积累财富！"));
        signButton.setItemMeta(signMeta);

        // 创建"钱包"按钮
        ItemStack walletButton = new ItemStack(Material.GOLD_INGOT);
        ItemMeta walletMeta = walletButton.getItemMeta();
        walletMeta.setDisplayName("§6§l我的钱包");
        int balance = BTCPlugin.getInstance().getEconomyManager().getBalance(player);
        walletMeta.setLore(Arrays.asList("§7当前余额: §a" + balance + " BTC", "§7点击进行转账操作"));
        walletButton.setItemMeta(walletMeta);

        // 将按钮放入菜单的特定位置
        menu.setItem(11, signButton); // 第二行，左中
        menu.setItem(15, walletButton); // 第二行，右中

        // 可以用染色玻璃板填充空白处，让菜单更好看
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < menu.getSize(); i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, filler);
            }
        }

        player.openInventory(menu);
    }

    public static void openTransferPlayerList(Player sender) {
        // 创建一个54格大小（6行）的箱子界面显示在线玩家
        Inventory playerList = Bukkit.createInventory(sender, 54, "§8§l选择转账对象");

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(sender)) continue; // 不显示自己

            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
            meta.setDisplayName("§b" + target.getName());
            meta.setOwningPlayer(target); // 设置头颅为对应玩家的皮肤
            meta.setLore(Arrays.asList("§7点击向 " + target.getName() + " 转账BTC"));
            playerHead.setItemMeta(meta);

            playerList.addItem(playerHead);
        }
        sender.openInventory(playerList);
    }
}