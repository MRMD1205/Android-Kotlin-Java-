package com.courtesycarsredhill.model;


public class DrawerItem {
    public int icon;
    public int iconSelected;
    public String name;

    // Constructor.
    public DrawerItem(int icon, int iconSelected, String name) {
        this.icon = icon;
        this.name = name;
        this.iconSelected = iconSelected;
    }
}
