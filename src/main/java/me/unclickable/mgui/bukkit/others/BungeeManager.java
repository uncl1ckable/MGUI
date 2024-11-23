package me.unclickable.mgui.bukkit.others;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.load.LoadManagerInterface;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class BungeeManager implements LoadManagerInterface<BungeeManager>, PluginMessageListener {

    private static final String CHANNEL_NAME = "BungeeCord";

    @Override
    public BungeeManager getInstance() {
        return this;
    }

    @Override
    public void initialize() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(MGUI.getInstance(), CHANNEL_NAME);
    }

    @SneakyThrows
    @SuppressWarnings("ALL")
    public void send(Player p, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        p.sendPluginMessage(MGUI.getInstance(), CHANNEL_NAME, out.toByteArray());
    }

    @Override
    public void terminate() {
        LoadManagerInterface.super.terminate();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull[] message) {
        // NO REQUIRED
    }

}
