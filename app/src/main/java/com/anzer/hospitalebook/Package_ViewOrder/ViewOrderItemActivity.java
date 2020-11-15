package com.anzer.hospitalebook.Package_ViewOrder;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anzer.hospitalebook.Package_ContentOfViewOrder.Consumable_Order_Tab;
import com.anzer.hospitalebook.Package_ContentOfViewOrder.DI_Order_Tab;
import com.anzer.hospitalebook.Package_ContentOfViewOrder.Lab_Order_Tab;
import com.anzer.hospitalebook.Package_ContentOfViewOrder.Pharmacy_Order_Tab;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.adapters.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import anzer.com.handwritingorderproject.Test;

/**
 * Created by David on 11/14/2018.
 */

public class ViewOrderItemActivity extends AppCompatActivity {

    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    int currentTab;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_order_activity);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPager = (CustomViewPager) findViewById(R.id.vp);
        setupViewPager(viewPager);
        viewPager.setPagingEnabled(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentTab = position;
                Toast.makeText(ViewOrderItemActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        int betweenSpace = 2;

        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for (int i=0; i<slidingTabStrip.getChildCount()-1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new All_Order_Tab(), "ALL");
        pagerAdapter.addFragment(new Pharmacy_Order_Tab(), "Pharmacy Order");
        pagerAdapter.addFragment(new Consumable_Order_Tab(), "Consumable Order");
        pagerAdapter.addFragment(new Lab_Order_Tab(), "LAB Order");
        pagerAdapter.addFragment(new DI_Order_Tab(), "DI Order");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
