package com.plugins.drlogiq.scythes;

import com.plugins.drlogiq.scythes.events.ScythesPlayerJoinEvent;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class LogixScythes extends JavaPlugin
{
    public static final Versions VERSION = Versions.V0_1;
    public static final Random RNG = new Random();

    private static LogixScythes instance;

    private Server server;
    private Logger logger;
    private FileConfiguration config;
    private boolean configLoaded = false;

    // Config values
    private boolean prefixMessages;

    @Override
    public void onEnable()
    {
        instance = this;
        super.onEnable();

        server = getServer();
        assert server != null;

        logger = server.getLogger();
        assert logger != null;

        // Load config
        loadTheConfig(null);

        // Register event handlers
        logDebug("Registering events");
        server.getPluginManager().registerEvents(ScythesPlayerJoinEvent.Instance, this);

        // Register command handlers
        logDebug("Registering commands");
        //TODO: getCommand("refresh").setExecutor(new ScythesCommandRefresh());
    }

    //region Config

    private void loadConfigValues()
    {
        prefixMessages = loadBoolean("PrefixMessages");
    }

    public void loadTheConfig(Player sender)
    {
        logDebug(configLoaded ? "Loading config" : "Reloading config");
        if (configLoaded && sender != null)
        {
            sendMessage(sender, ChatColor.YELLOW + "Reloading config...");
        }
        getConfig().options().copyDefaults(true); // TODO(logiq): Check this doesn't overwrite customised values
        saveConfig();
        config = getConfig();
        loadConfigValues();
        // TODO(logiq): Verify success
        if (configLoaded && sender != null)
        {
            sendSuccessMessage(sender, "Reload complete");
        }
        configLoaded = true;
    }

    private String loadStringOrDefault(String key, String defaultString)
    {
        String string = config.getString(key);
        if (string == null || string.length() < 1)
        {
            logFailure("Failed to load string '" + key + "' from config. Setting it to default");
            config.set(key, defaultString);
            saveConfig();
            return defaultString;
        }
        logSuccess("Loaded string value '" + key + "' from config");
        return string;
    }

    private List<String> loadStringListOrDefault(String key, List<String> defaultList)
    {
        List<String> strings = config.getStringList(key);
        if (strings == null || strings.size() < 1)
        {
            logFailure("Failed to load string list '" + key + "' from config. Setting it to default");
            config.set(key, defaultList);
            saveConfig();
            return defaultList;
        }
        logSuccess("Loaded string list value '" + key + "' from config");
        return strings;
    }

    private boolean loadBoolean(String key)
    {
        return config.getBoolean(key);
    }
    //endregion

    //region Logging
    public static void log(String message)
    {
        getInstance().logger.info("[Logix-Scythes] " + message);
    }

    public static void logDebug(String message)
    {
        getInstance().logger.info("[Logix-Scythes] (DEBUG) " + message);
    }

    public static void logSuccess(String message)
    {
        getInstance().logger.info("[Logix-Scythes] (SUCCESS) " + message);
    }

    public static void logFailure(String message)
    {
        getInstance().logger.info("[Logix-Scythes] (FAILURE) " + message);
    }

    public static void logWarning(String message)
    {
        getInstance().logger.info("[Logix-Scythes] (WARNING) " + message);
    }

    public static void logError(String message)
    {
        getInstance().logger.info("[Logix-Scythes] (ERROR) " + message);
    }
    //endregion

    //region Player Messaging
    public static void sendMessage(Player player, String message)
    {
        if (!instance.prefixMessages)
        {
            player.sendMessage(ChatColor.GRAY + message);
        }
        else
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[Logix-Scythes] " + ChatColor.GRAY + message);
        }
    }

    public static void sendDebugMessage(Player player, String message)
    {
        if (!instance.prefixMessages)
        {
            player.sendMessage("(DEBUG) " + ChatColor.GRAY + message);
        }
        else
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[Logix-Scythes] (DEBUG) " + ChatColor.GRAY + message);
        }
    }

    public static void sendSuccessMessage(Player player, String message)
    {
        if (!instance.prefixMessages)
        {
            player.sendMessage(ChatColor.GREEN + message);
        }
        else
        {
            player.sendMessage(ChatColor.DARK_GREEN + "[Logix-Scythes] " + ChatColor.GREEN + message);
        }
    }

    public static void sendFailureMessage(Player player, String message)
    {
        if (!instance.prefixMessages)
        {
            player.sendMessage(ChatColor.RED + message);
        }
        else
        {
            player.sendMessage(ChatColor.DARK_RED + "[Logix-Scythes] " + ChatColor.RED + message);
        }
    }

    public static void sendOperatorOnlyErrorMessage(Player player)
    {
        sendFailureMessage(player, "This command can only be used by server OPs.");
    }
    //endregion

    //region Getters
    public static LogixScythes getInstance()
    {
        return instance;
    }
    //endregion
}