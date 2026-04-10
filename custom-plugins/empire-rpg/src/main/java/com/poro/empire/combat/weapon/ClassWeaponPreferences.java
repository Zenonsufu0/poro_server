package com.poro.empire.combat.weapon;

import com.poro.empire.classes.PlayerClass;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class ClassWeaponPreferences {
    private static final Map<PlayerClass, Set<WeaponType>> PREFERRED_WEAPONS = createDefaults();

    private ClassWeaponPreferences() {
    }

    public static Set<WeaponType> getPreferredWeapons(PlayerClass playerClass) {
        if (playerClass == null) {
            return Set.of(WeaponType.NONE);
        }

        return PREFERRED_WEAPONS.getOrDefault(playerClass, Set.of(WeaponType.NONE));
    }

    public static boolean isPreferred(PlayerClass playerClass, WeaponType weaponType) {
        WeaponType safeWeaponType = weaponType == null ? WeaponType.NONE : weaponType;
        return getPreferredWeapons(playerClass).contains(safeWeaponType);
    }

    private static Map<PlayerClass, Set<WeaponType>> createDefaults() {
        EnumMap<PlayerClass, Set<WeaponType>> map = new EnumMap<>(PlayerClass.class);
        map.put(PlayerClass.UNSELECTED, EnumSet.of(WeaponType.NONE));
        map.put(PlayerClass.WARRIOR, EnumSet.of(WeaponType.GREATSWORD, WeaponType.SWORD_AND_SHIELD, WeaponType.SPEAR));
        map.put(PlayerClass.ASSASSIN, EnumSet.of(WeaponType.DAGGER, WeaponType.SWORD_AND_SHIELD));
        map.put(PlayerClass.MAGE, EnumSet.of(WeaponType.STAFF));
        return Collections.unmodifiableMap(map);
    }
}
