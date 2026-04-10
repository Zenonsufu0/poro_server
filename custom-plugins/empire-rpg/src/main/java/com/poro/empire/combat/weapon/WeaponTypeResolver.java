package com.poro.empire.combat.weapon;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class WeaponTypeResolver {
    private WeaponTypeResolver() {
    }

    public static WeaponType resolve(Player player) {
        if (player == null) {
            return WeaponType.NONE;
        }

        return resolve(player.getInventory().getItemInMainHand());
    }

    public static WeaponType resolve(ItemStack itemStack) {
        if (itemStack == null) {
            return WeaponType.NONE;
        }

        return resolve(itemStack.getType());
    }

    public static WeaponType resolve(Material material) {
        if (material == null || material.isAir()) {
            return WeaponType.NONE;
        }

        return switch (material) {
            case NETHERITE_SWORD, DIAMOND_SWORD -> WeaponType.GREATSWORD;
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD -> WeaponType.SWORD_AND_SHIELD;
            case TRIDENT -> WeaponType.SPEAR;
            case SHEARS -> WeaponType.DAGGER;
            case STICK, BLAZE_ROD -> WeaponType.STAFF;
            default -> WeaponType.NONE;
        };
    }
}
