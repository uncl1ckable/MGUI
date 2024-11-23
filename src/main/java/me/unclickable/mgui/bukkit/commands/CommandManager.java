package me.unclickable.mgui.bukkit.commands;

import me.unclickable.mgui.bukkit.load.LoadManagerInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements LoadManagerInterface<CommandManager> {

    private static final Map<String, CommandExecutor> EXECUTORS = new HashMap<>();

    @Override
    public CommandManager getInstance() {
        return this;
    }

    @Override
    public void initialize() {
        EXECUTORS.put("mgui", new CommandMGUI());

        EXECUTORS.forEach((s, executor) -> {
            PluginCommand command = Bukkit.getPluginCommand(s);
            if(command != null) command.setExecutor(executor);
        });
    }

}
