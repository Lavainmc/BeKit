package me.lavainmc.bekit.listeners;

import me.lavainmc.bekit.BeKit;

import me.lavainmc.bekit.managers.ChestGuiManager;
import me.lavainmc.bekit.managers.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ChestClickListener implements Listener {

    private final BeKit plugin;
    private final ChestGuiManager menu;
    private final KitManager kit;

    public ChestClickListener(BeKit plugin, ChestGuiManager menu, KitManager kit) {

        this.plugin = plugin;
        this.menu = menu;
        this.kit = kit;
    }



    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("套件编辑"))
        {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null)
            {
                switch (clickedItem.getItemMeta().getDisplayName())
                {
                    case "§a保存当前套件":
                        event.setCancelled(true);
                        player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1, 1);
                        this.kit.saveKit(player);
                        this.menu.setMenu();
                        break;
                    case "§e加载默认套件":
                        player.sendMessage("§bBeKit§7>> §a已加载默认套件");
                        event.setCancelled(true);
                        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1, 1);
                        this.kit.loadDefaultKit(player);
                        /*
                        旧版防dupe方法
                        player.closeInventory();
                        this.menu.open(player);
                        */
                        // 这是新的
                        this.menu.setMenu();
                        break;
                    case "§c删除已有套件":
                        event.setCancelled(true);
                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                        this.kit.deleteKit(player);
                        this.kit.loadDefaultKit(player);
                        this.menu.setMenu();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
