package com.recharge.demomap;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.recharge.demomap.ui.home.MainActivity;


public class FragmentNavigationManager implements NavigationManager {

    private static FragmentNavigationManager sInstance;

    private FragmentManager mFragmentManager;
    private MainActivity mActivity;

    public static FragmentNavigationManager obtain(MainActivity activity) {
        if (sInstance == null) {
            sInstance = new FragmentNavigationManager();
        }
        sInstance.configure(activity);
        return sInstance;
    }

    private void configure(MainActivity activity) {
        mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
    }




    @Override
    public void showHomeFragment(Location location) {
        showFragment(MapShowFragment.newInstance(location.getLatitude(),location.getLongitude()),true);
    }

    @Override
    public void showAccountFragment() {
      //  showFragment(new MyAccountFragment(),true);
    }

    @Override
    public void showNavigationFragment() {
       // showNavFragment(new MenuListFragment(),true);
    }




    /*@Override
    public void showMoreFragment() {
        showFragment(new MoreFragment(),true);
    }
*/





    private void showFragment(Fragment fragment, boolean allowStateLoss) {
        FragmentManager fm = mFragmentManager;

        FragmentTransaction ft = fm.beginTransaction()
            .replace(R.id.mapframe, fragment);

        if (!(fragment instanceof MapShowFragment)) {
            ft.addToBackStack(null);
        }
        ft.commit();
        fm.executePendingTransactions();
    }

    private void showNavFragment(Fragment fragment, boolean allowStateLoss) {
        /*FragmentManager fm = mFragmentManager;

        FragmentTransaction ft = fm.beginTransaction()
                .replace(R.id.id_container_menu, fragment);

        if (!(fragment instanceof HomeFragment)) {
            ft.addToBackStack(null);
        }
        ft.commit();
        fm.executePendingTransactions();*/
    }
}
