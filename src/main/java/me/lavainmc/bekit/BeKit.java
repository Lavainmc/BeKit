package me.lavainmc.bekit;

import me.lavainmc.bekit.commands.TabCompleter;
import me.lavainmc.bekit.listeners.ChestClickListener;
import me.lavainmc.bekit.listeners.WorldJoinListener;
import me.lavainmc.bekit.managers.ChestGuiManager;

import me.lavainmc.bekit.managers.CommandManager;
import me.lavainmc.bekit.managers.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class BeKit extends JavaPlugin {

    private KitManager kitManager;
    private CommandManager commandManager;
    private ChestGuiManager chestGuiManager;
    private WorldJoinListener worldchangelistener;

    @Override
    public void onEnable() {

        // 生成文件
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        generateFile("config.yml");
        generateFile("readme.txt");
        generateFile("default_example.yml");
        getLogger().info("示例套件已生成");

        // 初始化管理器
        this.kitManager = new KitManager(this, kitManager);
        this.chestGuiManager = new ChestGuiManager(this);
        this.commandManager = new CommandManager(this, kitManager, chestGuiManager);
        this.worldchangelistener = new WorldJoinListener(this, kitManager);

        // 监听器注册
        getServer().getPluginManager().registerEvents(new ChestClickListener(this, chestGuiManager, kitManager), this);
        getServer().getPluginManager().registerEvents(new WorldJoinListener(this, kitManager), this);

        // 指令注册
        getCommand("bekit").setExecutor(commandManager);
        getCommand("bekit").setTabCompleter(new TabCompleter());

        getLogger().info("BeKit 插件已装载!");
        getLogger().info("Plugin made by Lavainmc(Lava不是岩浆) !");
    }

    @Override
    public void onDisable() {
        getLogger().info("BeKit 插件 已卸载! see u again!");
    }

    public void reloadConfig(Player player) {
        reloadConfig();
        player.sendMessage("§bBeKit§7>> §e已尝试重载配置文件!");
    }

    // File
    private void generateFile(String fileName) {
        File file = new File(getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                // 复制文件
                InputStream inputStream = getResource(fileName);
                if (inputStream != null) {
                    Files.copy(inputStream, file.toPath());
                } else {
                    file.createNewFile();
                }
                if (fileName.equals("config.yml")) {
                    if (!getConfig().getStringList("config-version").contains("1.0")) {
                        if (inputStream != null) {
                            Files.copy(inputStream, file.toPath());
                        }
                    }
                }
            } catch (IOException e) {
                getLogger().severe("无法创建文件 " + fileName + ": " + e.getMessage());
            }
        }
    }
}




