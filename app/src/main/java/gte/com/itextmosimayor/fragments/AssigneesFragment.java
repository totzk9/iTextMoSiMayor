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

public class AssigneesFragment extends Fragment {
    View view;

    public static AssigneesFragment newInstance() {
        return new AssigneesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tabbed_layout, container, false);
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = view.findViewById(R.id.viewPager);
        // Setting ViewPager for each Tabs
        setupViewPager(mViewPager);

        BadgedTabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);
        TabLayout tabs1 = view.findViewById(R.id.tabs1);
        tabs1.setVisibility(View.VISIBLE);
        tabs1.setupWithViewPager(mViewPager);
        return view;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        MainMessagesPageAdapter adapter = new MainMessagesPageAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new FragmentChat(), "Recent Chats");
        switch (Preference.getInstance(getContext()).getPrefString("UserType")) {
            case "Mayor": {
                adapter.addFragment(new FragmentDepartments(), "Assignees");
                break;
            }
            case "Department": {
//                adapter.addFragment(new FragmentDepartments(), "Assignees");
                break;
            }
        }
        viewPager.setAdapter(adapter);
    }
}