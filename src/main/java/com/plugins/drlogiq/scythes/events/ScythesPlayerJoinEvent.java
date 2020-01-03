package com.plugins.drlogiq.scythes.events;

import com.plugins.drlogiq.scythes.LogixScythes;
import com.plugins.drlogiq.scythes.Versions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ScythesPlayerJoinEvent implements Listener
{
    public static final ScythesPlayerJoinEvent Instance = new ScythesPlayerJoinEvent();

    private ScythesPlayerJoinEvent()
    {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        // Update check
        if (player.isOp())
        {
            LogixScythes.getInstance().getServer().getScheduler().runTaskLater(LogixScythes.getInstance(), () ->
            {
                try
                {
                    URL url = new URL("https://raw.githubusercontent.com/DoctorLogiq/Logix-Scythes-Plugin/master/latest.txt");
                    URLConnection uc;
                    uc = url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                    String line = reader.readLine();
                    reader.close();

                    Versions latestVersion = Versions.fromString(line);
                    if (latestVersion == Versions.UNKNOWN)
                    {
                        LogixScythes.sendFailureMessage(player, "Update check failed - unable to determine latest version. Please report this to the " +
                                "plugin developer (Dr. Logiq).");
                        return;
                    }

                    if (Versions.isLowerThan(LogixScythes.VERSION, latestVersion))
                    {
                        LogixScythes.sendMessage(player,
                                                 ChatColor.LIGHT_PURPLE + "An update is available. Your version: " + ChatColor.AQUA + LogixScythes.VERSION.getVersionString() + ChatColor.LIGHT_PURPLE + ", " + ChatColor.AQUA + "latest version: " + latestVersion.getVersionString());
                    }
                    else
                    {
                        LogixScythes.logDebug("You are using the latest version of Logix-Scythes");
                    }
                }
                catch (IOException e)
                {
                    LogixScythes.sendFailureMessage(player, "Update check failed.");
                    e.printStackTrace();
                }
            }, 6);
        }
    }
}