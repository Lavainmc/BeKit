package me.lavainmc.bekit.managers;

import me.lavainmc.bekit.BeKit;
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
    private Inventory gui;

    public ChestGuiManager(BeKit plugin) {
        this.plugin = plugin;
    }

    // 打开inv方法
    public void open(Player player) {
        if (!plugin.getConfig().getBoolean("chest-menu", true)) {
            player.sendMessage("§c套件编辑界面已禁用！");
            return;
        }
        this.gui = Bukkit.createInventory(null, 27, "套件编辑");
        setMenu();
        player.openInventory(gui);
    }


    public void setMenu() {
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
