package com.zhangzipei.btcplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class EconomyManager {
    private final BTCPlugin plugin;
    private File file;
    private FileConfiguration config;

    public EconomyManager(BTCPlugin plugin) {
        this.plugin = plugin;
        setupEconomyFile();
    }

    private void setupEconomyFile() {
        // 数据文件将保存在插件的数据文件夹内
        file = new File(plugin.getDataFolder(), "player_balances.yml");
        if (!file.exists()) {
            // 如果文件不存在，创建它
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("无法创建玩家余额文件！");
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    // 获取玩家余额
    public int getBalance(Player player) {
        // 使用玩家的UUID作为键来存储，更安全
        return config.getInt(player.getUniqueId().toString(), 0);
    }

    // 设置玩家余额（内部使用）
    private void setBalance(Player player, int amount) {
        config.set(player.getUniqueId().toString(), amount);
        saveConfig();
    }

    // 给玩家增加余额 (签到用)
    public void addBalance(Player player, int amount) {
        int current = getBalance(player);
        setBalance(player, current + amount);
    }

    // 玩家间转账
    public boolean transfer(Player from, Player to, int amount) {
        int fromBalance = getBalance(from);
        if (fromBalance < amount || amount <= 0) {
            return false; // 余额不足或金额无效
        }
        setBalance(from, fromBalance - amount);
        addBalance(to, amount); // 注意：这里直接给目标加钱，因为addBalance内部会保存。
        return true;
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存玩家余额文件！");
        }
    }
}