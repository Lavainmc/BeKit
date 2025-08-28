package me.lavainmc.bekit.listeners;

import me.lavainmc.bekit.BeKit;

import me.lavainmc.bekit.managers.ChestGuiManager;
import me.lavainmc.bekit.managers.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ChestClickListener implements Listener {

    private final BeKit plugin;
    private final ChestGuiManager menu;

    public ChestClickListener(BeKit plugin, ChestGuiManager menu) {

        this.plugin = plugin;
        this.menu = menu;
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
                        Bukkit.dispatchCommand(player, "bekit savekit");
                        event.setCancelled(true);
                        this.menu.setMenu();
                        break;
                    case "§e加载默认套件":
                        player.sendMessage("§bBeKit§7>> §a已加载默认套件");
                        Bukkit.dispatchCommand(player, "bekit loaddefaultkit");
                        event.setCancelled(true);
                        /*
                        旧版防dupe方法
                        player.closeInventory();
                        this.menu.open(player);
                        */
                        // 这是新的
                        this.menu.setMenu();
                        break;
                    case "§c删除已有套件":
                        Bukkit.dispatchCommand(player, "bekit deletekit");
                        event.setCancelled(true);
                        this.menu.setMenu();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
