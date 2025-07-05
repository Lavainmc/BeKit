package me.lavainmc.bekit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {

        // 确保插件数据文件夹存在
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        // 生成/加载配置文件
        generatefile("readme.txt");
        generatefile("default_example.yml");
        getLogger().info("示例套件已生成");
        // 插件加载info
        getLogger().info("BeKit 插件已装载!");
        getLogger().info("Plugin made by Lavainmc(Lava不是岩浆) !");
        // 指令注册
        getCommand("bekit").setExecutor(new commandmanager());   // 此处注册commandmanager
        getCommand("bekit").setTabCompleter(new commandtab());   // 此处注册tab命令补全
    }

    @Override
    public void onDisable() {
        // 插件卸载info
        getLogger().info("BeKit 已卸载! see u again!");
    }

    public class commandmanager implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            Player player = (Player) sender;
            if (args.length == 0) {
                // 主命令逻辑
                sender.sendMessage("§bBeKit§7>> §f使用方法: /bekit <操作>");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "savekit":
                    // 覆盖并保存套件
                    if (!player.hasPermission("bekit.savekit")) {
                        player.sendMessage("§c你没有权限执行此命令!");
                        return true;
                    }
                    deletekit(player);
                    saveinventory(player);
                    return true;
                case "loadkit":
                    // 加载套件
                    if (!player.hasPermission("bekit.loadkit")) {
                        player.sendMessage("§c你没有权限执行此命令!");
                        return true;
                    }
                    loadkit(player);
                    return true;
                case "loaddefaultkit":
                    // 加载默认套件
                    if (!player.hasPermission("bekit.loadkit")) {
                        player.sendMessage("§c你没有权限执行此命令!");
                        return true;
                    }
                    loaddefaultkit(player);
                    return true;
                case "deletekit":
                    // 删除套件
                    if (!player.hasPermission("bekit.deletekit")) {
                        player.sendMessage("§c你没有权限执行此命令!");
                        return true;
                    }
                    deletekit(player);
                    return true;
                case "about":
                    // 关于
                    sender.sendMessage("§bBeKit §7版本: §eAlpha §fv0.1");
                    sender.sendMessage("§fCreate By §eLava不是岩浆");
                    return true;
                case "help":
                    // help
                    sender.sendMessage("§7--[ §bBeKit §f指令帮助§7 ]--");
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
    //异步保存插件
    public void saveinventory(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                dosaveinventory(player);
                player.sendMessage("§bBeKit§7>> §a套件保存成功!");
            } catch (Exception e) {
                player.sendMessage("§bBeKit§7>> §c套件保存失败!");
                e.printStackTrace();
            }
        });
    }
    public void dosaveinventory(Player player) {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File playerFile = new File(dataFolder, "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        //背包
        ItemStack[] inventory = player.getInventory().getContents();
        for (int i = 0; i < 36; i++) {
            if (inventory[i] != null) {
                config.set("inventory." + i, inventory[i]);
            }
        }
        //盔甲栏+副手
        ItemStack[] armor = player.getInventory().getArmorContents();
        config.set("armor.boots", armor[0]);
        config.set("armor.leggings", armor[1]);
        config.set("armor.chestplate", armor[2]);
        config.set("armor.helmet", armor[3]);

        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        if (offhandItem != null && !offhandItem.getType().isAir()) {
            config.set("offhand", offhandItem);
        }
        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("§bBeKit§7>> §c保存失败!");
        }
    }

    //加载玩家套件
    public void loadkit(Player player) {
        File playerFile = new File(getDataFolder(), "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        if (!playerFile.exists()) {
            loaddefaultkit(player);
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        //加载主背包
        ItemStack[] inventory = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            if (config.contains("inventory." + i)) {
                inventory[i] = config.getItemStack("inventory." + i);
            }
        }
        player.getInventory().setContents(inventory);

        //加载装备栏
        ItemStack[] armor = new ItemStack[4];
        armor[0] = config.getItemStack("armor.boots");
        armor[1] = config.getItemStack("armor.leggings");
        armor[2] = config.getItemStack("armor.chestplate");
        armor[3] = config.getItemStack("armor.helmet");
        player.getInventory().setArmorContents(armor);
        //加载副手
        if (config.contains("offhand")) {
            ItemStack offhandItem = config.getItemStack("offhand");
            player.getInventory().setItemInOffHand(offhandItem);
        }
    }

    //加载原始套件
    public void loaddefaultkit(Player player) {
        File playerFile = new File(getDataFolder(), "default_" + player.getWorld().getName() + ".yml");
        if (!playerFile.exists()) {
            player.sendMessage("§bBeKit§7>> §c加载默认套件失败, 请联系管理员!");
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        //加载主背包
        ItemStack[] inventory = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            if (config.contains("inventory." + i)) {
                inventory[i] = config.getItemStack("inventory." + i);
            }
        }
        player.getInventory().setContents(inventory);

        //加载装备栏
        ItemStack[] armor = new ItemStack[4];
        armor[0] = config.getItemStack("armor.boots");
        armor[1] = config.getItemStack("armor.leggings");
        armor[2] = config.getItemStack("armor.chestplate");
        armor[3] = config.getItemStack("armor.helmet");
        player.getInventory().setArmorContents(armor);
        //加载副手
        if (config.contains("offhand")) {
            ItemStack offhandItem = config.getItemStack("offhand");
            player.getInventory().setItemInOffHand(offhandItem);
        }
    }

    //删除套件
    public void deletekit(Player player) {
        File playerFile = new File(
                getDataFolder(), "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        if (playerFile.delete()) {
            player.sendMessage("§bBeKit§7>> §a已删除你的套件!");
        } else {
            player.sendMessage("§bBeKit§7>> §c未能删除套件!");
        }
    }

    //文件生成
    private void generatefile(String fileName) {
        File file = new File(getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                // 尝试复制默认文件
                InputStream inputStream = getResource(fileName);
                if (inputStream != null) {
                    Files.copy(inputStream, file.toPath());
                } else {
                    file.createNewFile();
                }

            } catch (IOException e) {
                getLogger().severe("无法创建文件 " + fileName + ": " + e.getMessage());
            }
        }
    }

}




