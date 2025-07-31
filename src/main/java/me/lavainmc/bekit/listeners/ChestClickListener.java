package me.lavainmc.bekit.listeners;

import me.lavainmc.bekit.Main;

import me.lavainmc.bekit.managers.ChestGuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ChestClickListener implements Listener {

    private final Main plugin;

    public ChestClickListener(Main plugin) {
        this.plugin = plugin;
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
                        player.closeInventory();
                        Bukkit.dispatchCommand(player, "bekit savekit");
                        event.setCancelled(true);
                        break;
                    case "§e加载默认套件":
                        player.sendMessage("§bBeKit§7>> §a已加载默认套件");
                        event.setCancelled(true);
                        player.closeInventory();
                        Bukkit.dispatchCommand(player, "bekitedit");
                        Bukkit.dispatchCommand(player, "bekit loaddefaultkit");
                        break;
                    case "§c删除已有套件":
                        Bukkit.dispatchCommand(player, "bekit deletekit");
                        event.setCancelled(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
