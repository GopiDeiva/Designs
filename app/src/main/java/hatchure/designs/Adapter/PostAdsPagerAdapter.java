package hatchure.designs.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import hatchure.designs.InStoreAds;
import hatchure.designs.ProductAds;

public class PostAdsPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public PostAdsPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            default:
                return new InStoreAds();
            case 1:
                return new ProductAds();
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
