package com.recharge.demomap;

import android.location.Location;

/**
 * @author msahakyan
 */

public interface NavigationManager {


    void showHomeFragment(Location location);

    void showAccountFragment();

void showNavigationFragment();
}