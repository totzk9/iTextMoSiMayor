package gte.com.itextmosimayor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapters.MainMessagesPageAdapter;
import gte.com.itextmosimayor.modules.Preference;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MessagesFragment extends Fragment {
    View view;
    public static BadgedTabLayout tabs;

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tabbed_layout, container, false);
        ViewPager mViewPager = view.findViewById(R.id.viewPager);

        //setUp adapter
        if (Preference.getInstance(getContext()).getPrefString("UserType").equals("Mayor"))
            mViewPager.setAdapter(mayorViewPageAdapter());
        else if (Preference.getInstance(getContext()).getPrefString("UserType").equals("Department"))
            mViewPager.setAdapter(departmentViewPageAdapter());

        //setUp tablayout
        if(Preference.getInstance(getContext()).getPrefBoolean("tabs")){
            tabs = view.findViewById(R.id.tabs);
            tabs.setVisibility(View.VISIBLE);
            tabs.setupWithViewPager(mViewPager);
        } else {
            TabLayout tabs1 = view.findViewById(R.id.tabs1);
            tabs1.setVisibility(View.VISIBLE);
            tabs1.setupWithViewPager(mViewPager);
        }


//        try {
        //first parameter is the tab index, at which badge should appear
//            tabs.setBadgeText(0,"1");
//            tabs.setBadgeText(1,"1");
//            tabs.setBadgeText(2,"1");
        //if you want to hide a badge pass null as a second parameter
//        tabs.setBadgeText(0,null);
//        } catch (Exception ignore) {
//
//        }


        return view;
    }

    // Add Fragments to Tabs
    private MainMessagesPageAdapter mayorViewPageAdapter() {
        MainMessagesPageAdapter adapter = new MainMessagesPageAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new FragmentUnassigned(), "Unassigned");
        adapter.addFragment(new FragmentOpen(), "Open");
        adapter.addFragment(new FragmentImportant(), "Important");
        return adapter;
    }

    private MainMessagesPageAdapter departmentViewPageAdapter() {
        MainMessagesPageAdapter adapter = new MainMessagesPageAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new FragmentDepartmentOpen(), "Open");
        adapter.addFragment(new FragmentDepartmentImportant(), "Important");
        return adapter;
    }
}