package com.zhangzipei.btcplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BTCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查执行者是否是玩家
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能执行此命令。");
            return true;
        }
        Player player = (Player) sender;
        // 打开主菜单
        MenuManager.openMainMenu(player);
        return true;
    }
}