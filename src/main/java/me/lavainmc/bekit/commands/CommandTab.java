package me.lavainmc.bekit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("savekit");
            completions.add("loadkit");
            completions.add("loaddefaultkit");
            completions.add("deletekit");
            completions.add("about");
            completions.add("help");
            completions.add("reload");
        }
        return completions.stream()
                .filter(s -> s.startsWith(args[args.length - 1]))
                .collect(Collectors.toList());

    }
}
