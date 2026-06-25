package com.minimace.mace;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener {

    private final TrueMiniMace plugin;
    private final MiniMaceItem itemManager;

    public AnvilListener(TrueMiniMace plugin) {
        this.plugin = plugin;
        this.itemManager = plugin.getItemManager();
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack first = event.getInventory().getFirstItem();
        ItemStack second = event.getInventory().getSecondItem();
        ItemStack result = event.getResult();

        // If either slot is empty, nothing to check
        if (first == null || second == null) return;

        boolean firstIsMini = itemManager.isMiniMace(first);
        boolean secondIsMini = itemManager.isMiniMace(second);
        boolean firstIsMace = first.getType() == Material.MACE;
        boolean secondIsMace = second.getType() == Material.MACE;

        // BLOCK 1: MiniMace + MiniMace (no combining two)
        if (firstIsMini && secondIsMini) {
            event.setResult(null);
            return;
        }

        // BLOCK 2: MiniMace + normal Mace (can't combine with regular mace)
        if (firstIsMini && secondIsMace) {
            event.setResult(null);
            return;
        }
        if (firstIsMace && secondIsMini) {
            event.setResult(null);
            return;
        }

        // BLOCK 3: Enchantment caps on MiniMace result
        if (result == null || !itemManager.isMiniMace(result)) return;

        if (exceedsCap(result, "wind_burst", 1)) {
            event.setResult(null);
            return;
        }
        if (exceedsCap(result, "density", 3)) {
            event.setResult(null);
            return;
        }
        if (exceedsCap(result, "breach", 3)) {
            event.setResult(null);
            return;
        }
    }

    private boolean exceedsCap(ItemStack item, String enchantmentKey, int maxLevel) {
        Enchantment enchantment = Registry.ENCHANTMENT.get(org.bukkit.NamespacedKey.minecraft(enchantmentKey));
        if (enchantment == null) return false;
        return item.getEnchantmentLevel(enchantment) > maxLevel;
    }
}
