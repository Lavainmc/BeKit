package me.lavainmc.bekit.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class ChestGuiManager {

    private final JavaPlugin plugin;
    private final Player player;
    private Inventory gui;

    public ChestGuiManager(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.gui = Bukkit.createInventory(null, 27, "套件编辑");
    }

    // 打开菜单
    public void open() {
        if (!plugin.getConfig().getBoolean("chest-menu", true)) {
            return;
        }
        Bukkit.dispatchCommand(player, "bekit loadkit");
        player.sendMessage("§bBeKit§7>> §f已自动加载已有套件, 请在界面中编辑套件!");
        player.openInventory(gui);
        setMenu();
    }


    private void setMenu() {
        gui.clear();
        ItemStack saveItem = createMenuItem(
                Material.WRITABLE_BOOK,
                "§a保存当前套件",
                Arrays.asList("§7点击将当前背包保存为套件"),
                11
        );
        ItemStack loadItem = createMenuItem(
                Material.BOOK,
                "§e加载默认套件",
                Arrays.asList("§7点击加载默认套件"),
                13
        );
        ItemStack deleteItem = createMenuItem(
                Material.LAVA_BUCKET,
                "§c删除已有套件",
                Arrays.asList("§7点击删除拥有的套件"),
                15
        );
    }

    // 创建菜单项
    private ItemStack createMenuItem(Material material, String name, List<String> lore, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.setItem(slot, item);
        return item;
    }
}
