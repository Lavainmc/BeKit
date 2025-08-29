package me.lavainmc.bekit.managers;

import me.lavainmc.bekit.BeKit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class KitManager {

    private final BeKit plugin;

    public KitManager(BeKit plugin, KitManager kit) {
        this.plugin = plugin;
    }

    // save kit void
    public void saveKit(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                doSaveKit(player);
                player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                        ("msg-save-success")
                ));
            }
            catch (Exception e) {
                player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                        ("msg-save-fail")
                ));
                e.printStackTrace();
            }
        });
    }

    // do save task
    public void doSaveKit(Player player) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File playerFile = new File(dataFolder, "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        // remove old kit
        deleteKit(player);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        ItemStack[] inventory = player.getInventory().getContents();
        for (int i = 0; i < 36; i++) {
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
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                    ("msg-save-fail")
            ));
        }
    }

    // load
    public void loadKit(Player player) {
        File playerFile = new File(plugin.getDataFolder(), "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        if (!playerFile.exists()) {
            loadDefaultKit(player);
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                    ("msg-load-fail")
            ));
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

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

    // load default
    public void loadDefaultKit(Player player) {
        File playerFile = new File(plugin.getDataFolder(), "default_" + player.getWorld().getName() + ".yml");
        if (!playerFile.exists()) {
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                    ("msg-load-default-fail")
            ));
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

    // load named kit
    public void loadNamedKit(Player player, String loadName) {
        File playerFile = new File(plugin.getDataFolder(), "players/" + player.getName() + "/" + loadName + ".yml");
        if (!playerFile.exists()) {
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                    ("msg-load-named-fail")
            ));
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

    //load named kit in default area
    public void loadNamedDefaultKit(Player player, String loadDefaultName) {
        File playerFile = new File(plugin.getDataFolder(), "default_" + loadDefaultName + ".yml");
        if (!playerFile.exists()) {
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                    ("msg-load-default-fail")
            ));
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



    // 删除套装
    public void deleteKit(Player player) {
        File playerFile = new File(
                plugin.getDataFolder(), "players/" + player.getName() + "/" + player.getWorld().getName() + ".yml");
        if (playerFile.delete()) {
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                    ("msg-delete-success")
            ));
        } else {
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                    ("msg-delete-fail")
            ));
        }
    }
}
