package me.lavainmc.bekit;

import me.lavainmc.bekit.commands.CommandTab;
import me.lavainmc.bekit.listeners.ChestClickListener;
import me.lavainmc.bekit.listeners.WorldJoinListener;
import me.lavainmc.bekit.managers.ChestGuiManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

public final class Main extends JavaPlugin {

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

        // 监听器注册
        getServer().getPluginManager().registerEvents(new WorldJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestClickListener(this), this);
        // 指令注册
        getCommand("bekit").setExecutor(new CommandManager());   // 注册CommandManager
        getCommand("bekit").setTabCompleter(new CommandTab());   // 注册tab命令补全
        getCommand("bekitedit").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player))
            {
                sender.sendMessage("该命令仅限玩家执行!");
                return true;
            }
            if (!sender.hasPermission("bekit.menu"))
            {
                sender.sendMessage(Objects.requireNonNull(getConfig().getString("msg-no-permission")));
                return true;
            }
            new ChestGuiManager(this, (Player) sender).open();
            return true;
        });

        // 插件加载info
        getLogger().info("BeKit 插件已装载!");
        getLogger().info("Plugin made by Lavainmc(Lava不是岩浆) !");
    }

    // 子命令
    public class CommandManager implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

            if (!(sender instanceof Player))
            {
                sender.sendMessage("§c该命令只能由玩家执行!");
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 0)
            {
                // 主命令逻辑
                sender.sendMessage("§bBeKit§7>> §f使用方法: /bekit <操作>");
                return true;
            }

            switch (args[0].toLowerCase())
            {
                // 保存套件
                case "savekit":
                    if (!player.hasPermission("bekit.savekit"))
                    {
                        player.sendMessage(Objects.requireNonNull(getConfig().getString
                                ("msg-no-permission")
                        ));
                        return true;
                    }
                    deleteKit(player);
                    saveInventory(player);
                    return true;

                // 加载套件
                case "loadkit":
                    if (!player.hasPermission("bekit.loadkit"))
                    {
                        player.sendMessage(Objects.requireNonNull(getConfig().getString
                                ("msg-no-permission")
                        ));
                        return true;
                    }
                    loadKit(player);
                    return true;

                // 加载默认套件
                case "loaddefaultkit":
                    if (!player.hasPermission("bekit.loadkit"))
                    {
                        player.sendMessage(Objects.requireNonNull(getConfig().getString
                                ("msg-no-permission")
                        ));
                        return true;
                    }
                    loadDefaultKit(player);
                    return true;

                // 删除套件
                case "deletekit":
                    if (!player.hasPermission("bekit.deletekit"))
                    {
                        player.sendMessage(Objects.requireNonNull(getConfig().getString
                                ("msg-no-permission")
                        ));
                        return true;
                    }
                    deleteKit(player);
                    return true;

                // 配置文件重载
                case "reload":
                    if (!player.hasPermission("bekit.reload"))
                    {
                        player.sendMessage(Objects.requireNonNull(getConfig().getString
                                ("msg-no-permission")
                        ));
                        return true;
                    }
                    reloadConfig(player);
                    return true;

                // 关于
                case "about":
                    sender.sendMessage("§bBeKit §7版本: §e" + getDescription().getVersion());
                    sender.sendMessage("§fCreate By §eLava不是岩浆");
                    return true;

                // 指令帮助
                case "help":
                    sender.sendMessage("§7--[ §bBeKit §f指令帮助§7 ]--");
                    sender.sendMessage("§f /bekitedit §7打开套件编辑界面");
                    sender.sendMessage("§f /bekit savekit §7保存Kit");
                    sender.sendMessage("§f /bekit loadkit §7加载自定义Kit");
                    sender.sendMessage("§f /bekit loaddefaultkit §7加载默认Kit");
                    sender.sendMessage("§f /bekit deletekit §7删除你的Kit");
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void onDisable() {
        // 插件卸载info
        getLogger().info("BeKit 已卸载! see u again!");
    }


    // 异步保存
    public void saveInventory(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                dosaveInventory(player);
                player.sendMessage(Objects.requireNonNull(getConfig().getString
                        ("msg-save-success")
                ));
            }
            catch (Exception e) {
                player.sendMessage(Objects.requireNonNull(getConfig().getString
                        ("msg-save-fail")
                ));
                e.printStackTrace();
            }
        });
    }

    // 执行保存
    public void dosaveInventory(Player player) {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File playerFile = new File(dataFolder, "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        // 加载背包
        ItemStack[] inventory = player.getInventory().getContents();
        for (int i = 0; i < 37; i++) {
            if (inventory[i] != null) {
                config.set("inventory." + i, inventory[i]);
            }
        }
        ItemStack[] armor = player.getInventory().getArmorContents();
        config.set("armor.boots", armor[0]);
        config.set("armor.leggings", armor[1]);
        config.set("armor.chestplate", armor[2]);
        config.set("armor.helmet", armor[3]);

        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        if (!offhandItem.getType().isAir()) {
            config.set("offhand", offhandItem);
        }
        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(Objects.requireNonNull(getConfig().getString
                    ("msg-save-fail")
            ));
        }
    }

    // 加载套装
    public void loadKit(Player player) {
        File playerFile = new File(getDataFolder(), "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        if (!playerFile.exists()) {
            loadDefaultKit(player);
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        // 加载背包
        ItemStack[] inventory = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            if (config.contains("inventory." + i)) {
                inventory[i] = config.getItemStack("inventory." + i);
            }
        }
        player.getInventory().setContents(inventory);

        ItemStack[] armor = new ItemStack[4];
        armor[0] = config.getItemStack("armor.boots");
        armor[1] = config.getItemStack("armor.leggings");
        armor[2] = config.getItemStack("armor.chestplate");
        armor[3] = config.getItemStack("armor.helmet");
        player.getInventory().setArmorContents(armor);

        if (config.contains("offhand")) {
            ItemStack offhandItem = config.getItemStack("offhand");
            player.getInventory().setItemInOffHand(offhandItem);
        }
    }

    // 默认套装
    public void loadDefaultKit(Player player) {
        File playerFile = new File(getDataFolder(), "default_" + player.getWorld().getName() + ".yml");
        if (!playerFile.exists()) {
            player.sendMessage("§bBeKit§7>> §c加载默认套件失败, 请联系管理员!");
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        // 背包
        ItemStack[] inventory = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            if (config.contains("inventory." + i)) {
                inventory[i] = config.getItemStack("inventory." + i);
            }
        }
        player.getInventory().setContents(inventory);

        // 护甲
        ItemStack[] armor = new ItemStack[4];
        armor[0] = config.getItemStack("armor.boots");
        armor[1] = config.getItemStack("armor.leggings");
        armor[2] = config.getItemStack("armor.chestplate");
        armor[3] = config.getItemStack("armor.helmet");
        player.getInventory().setArmorContents(armor);
        // 副手
        if (config.contains("offhand")) {
            ItemStack offhandItem = config.getItemStack("offhand");
            player.getInventory().setItemInOffHand(offhandItem);
        }
    }

    // 删除套装
    public void deleteKit(Player player) {
        File playerFile = new File(
                getDataFolder(), "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        if (playerFile.delete()) {
            player.sendMessage(Objects.requireNonNull(getConfig().getString
                    ("msg-delete-success")
            ));
        } else {
            player.sendMessage(Objects.requireNonNull(getConfig().getString
                    ("msg-delete-fail")
            ));
        }
    }

    public void reloadConfig(Player player) {
        this.reloadConfig();
        player.sendMessage("§bBeKit§7>> §e已尝试重载配置文件!");
    }

    // 文件生成
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




