package me.unclickable.mgui.bukkit.commands;

import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.gui.GUIManager;
import me.unclickable.mgui.bukkit.load.LoadManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandMGUI implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) return true;

        if(p.isOp() && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            MGUI.getInstance().getLoadManager().reload();
            return true;
        }

        LoadManager.getInstance(GUIManager.class)
                .map(GUIManager::getInstance)
                .ifPresent(guiManager -> guiManager.open(p, args[0]));

        return true;
    }

}
