package com.poro.empire.classes;

public enum PlayerClass {
    UNSELECTED,
    WARRIOR,
    ASSASSIN,
    MAGE;

    public boolean isSelected() {
        return this != UNSELECTED;
    }
}
