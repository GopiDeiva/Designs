package hatchure.designs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hatchure.designs.Interfaces.ICustomClickEvent;

public class ProductAds extends Fragment implements ICustomClickEvent {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_postads_fragment, container, false);
        return view;
    }

    @Override
    public void OnCustomClick(int position) {

    }
}