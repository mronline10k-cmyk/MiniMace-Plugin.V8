package com.minimace.mace;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MiniMaceItem {

    private final TrueMiniMace plugin;
    private final NamespacedKey maceKey;

    public MiniMaceItem(TrueMiniMace plugin) {
        this.plugin = plugin;
        this.maceKey = new NamespacedKey(plugin, "minimace");
    }

    public ItemStack create() {
        // USE THE REAL MACE - vanilla mechanics, no sweep, can enchant with books
        ItemStack item = new ItemStack(Material.MACE);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return item;

        // Custom name
        String name = plugin.getConfig().getString("item.name", "&4&lMini Mace");
        meta.displayName(Component.text(name.replace('&', '§')).decoration(TextDecoration.ITALIC, false));

        // Lore
        List<String> loreStrings = plugin.getConfig().getStringList("item.lore");
        if (!loreStrings.isEmpty()) {
            List<Component> lore = loreStrings.stream()
                .map(s -> (Component) Component.text(s.replace('&', '§')))
                .toList();
            meta.lore(lore);
        }

        // Mark as MiniMace
        meta.getPersistentDataContainer().set(maceKey, PersistentDataType.BYTE, (byte) 1);

        // BONUS DAMAGE from config (half damage is default, or set your own)
        double bonusDamage = plugin.getConfig().getDouble("item.bonus-damage", -2.0);
        if (bonusDamage != 0) {
            meta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(
                    new NamespacedKey(plugin, "minimace_damage"),
                    bonusDamage,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.MAINHAND
                )
            );
        }

        // FASTER ATTACK SPEED: Half the charge time of original mace
        // Original mace: 1.6 attack speed (slow)
        // MiniMace: 3.2 attack speed (medium - twice as fast)
        meta.addAttributeModifier(
            Attribute.GENERIC_ATTACK_SPEED,
            new AttributeModifier(
                new NamespacedKey(plugin, "minimace_speed"),
                1.6, // +1.6 makes it 3.2 (half the cooldown of mace)
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.MAINHAND
            )
        );

        // Hide attributes so lore is clean
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        // Custom model data triggers the resource pack shrink
        meta.setCustomModelData(1000001);

        item.setItemMeta(meta);
        return item;
    }

    public boolean isMiniMace(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(maceKey, PersistentDataType.BYTE);
    }

    public NamespacedKey getMaceKey() {
        return maceKey;
    }
}
