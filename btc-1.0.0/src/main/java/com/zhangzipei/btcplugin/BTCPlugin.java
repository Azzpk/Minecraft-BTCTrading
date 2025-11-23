package com.zhangzipei.btcplugin; // 确保这里的包名和你在 plugin.yml 里写的一致

import org.bukkit.plugin.java.JavaPlugin;

public final class BTCPlugin extends JavaPlugin {
    private static BTCPlugin instance;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        // 当插件加载时执行
        instance = this;
        this.economyManager = new EconomyManager(this);

        // 注册事件监听器（我们稍后会创建）
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        // 注册命令执行器（我们稍后会创建）
        getCommand("btcgui").setExecutor(new BTCCommand());

        getLogger().info("BTCPlugin 已成功加载！");
        // 保存默认配置文件（如果有的话）
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // 当插件卸载时执行
        getLogger().info("BTCPlugin 已卸载！");
    }

    // 提供一个静态方法，方便在其他类中获取插件实例
    public static BTCPlugin getInstance() {
        return instance;
    }

    // 获取经济管理器的实例
    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}