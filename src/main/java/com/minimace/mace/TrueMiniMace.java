package com.minimace.mace;

import org.bukkit.plugin.java.JavaPlugin;

public class TrueMiniMace extends JavaPlugin {

    private static TrueMiniMace instance;
    private MiniMaceItem itemManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        itemManager = new MiniMaceItem(this);
        getCommand("minimace").setExecutor(new MiniMaceCommand(this));
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);

        getLogger().info("TrueMiniMace enabled! /minimace to get a half-size mace.");
    }

    @Override
    public void onDisable() {
        getLogger().info("TrueMiniMace disabled.");
    }

    public MiniMaceItem getItemManager() {
        return itemManager;
    }

    public static TrueMiniMace getInstance() {
        return instance;
    }
}
