package me.lavainmc.bekit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldJoinListener implements Listener {

    private final JavaPlugin plugin;
    public WorldJoinListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World newWorld = player.getWorld();

        // 检查世界是否在配置列表中
        if (plugin.getConfig().getStringList("givable-worlds").contains(newWorld.getName())) {
            player.addAttachment(plugin, "bekit.loadkit", true);
            Bukkit.dispatchCommand(player, "bekit loadkit");
            player.removeAttachment(player.addAttachment(plugin, "bekit.loadkit", true));
            player.sendMessage("§bBeKit§7>> §f已自动加载套装");
        }
    }
}
