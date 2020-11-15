package com.anzer.hospitalebook.models.adapters;

import android.view.MenuItem;

import com.google.android.material.internal.NavigationMenu;

public class SimpleMenuListenerAdapter implements io.github.yavski.fabspeeddial.FabSpeedDial.MenuListener {

    @Override
    public boolean onPrepareMenu(NavigationMenu navigationMenu) {return true;}

    @Override
    public boolean onMenuItemSelected(MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMenuClosed() {}
}
