package me.lavainmc.bekit.managers;

import me.lavainmc.bekit.BeKit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandManager implements CommandExecutor {

    private final BeKit plugin;
    private final KitManager kit;
    private final ChestGuiManager menu;

    public CommandManager(BeKit plugin, KitManager kit, ChestGuiManager menu) {
        this.plugin = plugin;
        this.kit = kit;
        this.menu = menu;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (plugin == null) {
            sender.sendMessage("§c插件初始化错误，请联系管理员！");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该命令只能由玩家执行!");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage("§bBeKit§7>> §f使用方法: /bekit <操作>");
            return true;
        }

        String noPermissionMsg = plugin.getConfig().getString("msg-no-permission", "§c你没有权限执行此命令！");

        switch (args[0].toLowerCase()) {
            // 套件编辑界面
            case "editkit":
                if (!player.hasPermission("bekit.menu")) {
                    player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                            ("msg-no-permission")
                    ));
                    return true;
                }
                this.menu.open(player);
                this.kit.loadKit(player);
                return true;
            // 保存套件
            case "savekit":
                if (!player.hasPermission("bekit.savekit")) {
                    player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString
                            ("msg-no-permission")
                    ));
                    return true;
                }
                this.kit.saveKit(player);
                return true;
            // 加载套件
            case "loadkit":
                if (!player.hasPermission("bekit.loadkit")) {
                    player.sendMessage(noPermissionMsg);
                    return true;
                }
                this.kit.loadKit(player);
                return true;
            // 加载默认套件
            case "loaddefaultkit":
                if (!player.hasPermission("bekit.loadkit")) {
                    player.sendMessage(noPermissionMsg);
                    return true;
                }
                this.kit.loadDefaultKit(player);
                return true;
            // 加载指定套件
            case "loadnamedkit":
                if (args.length < 2) {
                    sender.sendMessage("§c用法: /bekit loadnamedkit <指定套装>");
                    return true;
                }
                if (!player.hasPermission("bekit.loadnamedkit")) {
                    player.sendMessage(noPermissionMsg);
                    return true;
                }
                String loadName = args[1];
                this.kit.loadNamedKit(player, loadName);
                return true;
            // 从默认区域加载指定套件
            case "loadnameddefaultkit":
                if (args.length < 2) {
                    sender.sendMessage("§c用法: /bekit loadnameddefaultkit <指定套装>");
                    return true;
                }
                if (!player.hasPermission("bekit.loadnamedkit")) {
                    player.sendMessage(noPermissionMsg);
                    return true;
                }
                String loadDefaultName = args[1];
                this.kit.loadNamedDefaultKit(player, loadDefaultName);
                return true;
            // 删除套件
            case "deletekit":
                if (!player.hasPermission("bekit.deletekit")) {
                    player.sendMessage(noPermissionMsg);
                    return true;
                }
                this.kit.deleteKit(player);
                return true;
            // 配置文件重载
            case "reload":
                if (!player.hasPermission("bekit.reload")) {
                    player.sendMessage(noPermissionMsg);
                    return true;
                }
                this.plugin.reloadConfig(player);
                return true;
            // 关于
            case "about":
                sender.sendMessage("§bBeKit §7版本: §e" + this.plugin.getDescription().getVersion());
                sender.sendMessage("§fCreate By §eLava不是岩浆");
                return true;
            // 指令帮助
            case "help":
                sender.sendMessage("§7--[ §bBeKit §f指令帮助§7 ]--");
                sender.sendMessage("§f /bekit editkit §7打开套件编辑界面");
                sender.sendMessage("§f /bekit savekit §7保存Kit");
                sender.sendMessage("§f /bekit loadkit §7加载你的自定义Kit");
                sender.sendMessage("§f /bekit loaddefaultkit §7加载默认Kit");
                sender.sendMessage("§f /bekit loadnamedkit §7加载你指定的自定义Kit");
                sender.sendMessage("§f /bekit loadnameddefaultkit §7加载你指定的默认Kit");
                sender.sendMessage("§f /bekit deletekit §7删除你的Kit");
                return true;
            default:
                return false;
        }
    }
}