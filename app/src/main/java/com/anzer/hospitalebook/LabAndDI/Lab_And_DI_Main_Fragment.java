package com.anzer.hospitalebook.LabAndDI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anzer.hospitalebook.LabAndDI.AvailableTests.DI_Test;
import com.anzer.hospitalebook.LabAndDI.AvailableTests.LAB_Test;
import com.anzer.hospitalebook.LabAndDI.OrderdTests.Ordered_DI_Test;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.adapters.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Lab_And_DI_Main_Fragment extends Fragment {
    private CustomViewPager avail_test_vp;
    private CustomViewPager test_to_order_vp;

    private TabLayout avail_test_tab_layout;
    int avail_test_currentTab;

    private TabLayout test_toOrder_tab_layout;
    int test_toOrder_currentTab;

    private Lab_And_DI_Main_Fragment.ViewPagerAdapter avail_tests_pagerAdapter;
    private Lab_And_DI_Main_Fragment.OrderedTestViewPagerAdapter ordered_tests_pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lab_n_di, container, false);
        Log.e("OnCreateView", "On Create Fragment");
        // Inflate the layout for this available test of DI
        avail_test_vp = view.findViewById(R.id.available_tests_vp);
        setupViewPagerForAvailableTests(avail_test_vp);
        avail_test_vp.setPagingEnabled(false);
        avail_test_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                avail_test_currentTab = position;
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        avail_test_tab_layout = view.findViewById(R.id.available_test_tabs);
        avail_test_tab_layout.setupWithViewPager(avail_test_vp);

        int betweenSpace = 2;

        ViewGroup slidingTabStrip = (ViewGroup) avail_test_tab_layout.getChildAt(0);
        for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }

        // Inflate the layout for ordered test of DI
        test_to_order_vp = view.findViewById(R.id.tests_toOrder_vp);
        setupViewPagerForOrderedTests(test_to_order_vp);
        test_to_order_vp.setPagingEnabled(false);
        test_to_order_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                test_toOrder_currentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        test_toOrder_tab_layout = view.findViewById(R.id.test_tobeOrder_tabs);
        test_toOrder_tab_layout.setupWithViewPager(test_to_order_vp);
        ViewGroup slidingTabStrip2 = (ViewGroup) test_toOrder_tab_layout.getChildAt(0);
        for (int i = 0; i < slidingTabStrip2.getChildCount() - 1; i++) {
            View v = slidingTabStrip2.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = betweenSpace;
        }
        return view;
    }

    // setup view page for available test
    private void setupViewPagerForAvailableTests(ViewPager viewPager) {
        avail_tests_pagerAdapter = new Lab_And_DI_Main_Fragment.ViewPagerAdapter(getActivity().getSupportFragmentManager());
        avail_tests_pagerAdapter.addFragment(new DI_Test(), "DI AND PROCEDURES");
        avail_tests_pagerAdapter.addFragment(new LAB_Test(), "LAB Tests");
        viewPager.setAdapter(avail_tests_pagerAdapter);
        Log.e("Visible", String.valueOf(viewPager.getVisibility()));
        viewPager.setOffscreenPageLimit(2);
    }

    // build view pager adapter for available tests
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

    // setup view page for ordered tests
    private void setupViewPagerForOrderedTests(ViewPager viewPager) {
        ordered_tests_pagerAdapter = new Lab_And_DI_Main_Fragment.OrderedTestViewPagerAdapter(getActivity().getSupportFragmentManager());
        ordered_tests_pagerAdapter.addFragment(new Ordered_DI_Test(), "DI AND PROCEDURES");
        ordered_tests_pagerAdapter.addFragment(new Ordered_DI_Test(), "LAB Tests");
        viewPager.setAdapter(ordered_tests_pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    // build view pager adapter for available tests
    class OrderedTestViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public OrderedTestViewPagerAdapter(FragmentManager manager) {
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
    public void onResume() {
        setupViewPagerForAvailableTests(avail_test_vp);
        avail_test_vp.setPagingEnabled(false);
        super.onResume();
    }
}