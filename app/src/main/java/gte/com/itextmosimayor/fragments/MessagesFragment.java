package gte.com.itextmosimayor.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapter.MainMessagesPageAdapter;
import gte.com.itextmosimayor.modules.Preference;

public class MessagesFragment extends Fragment {
    View view;
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
        view = inflater.inflate(R.layout.activity_messages_fragment, container, false);
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = view.findViewById(R.id.viewPager);
        // Setting ViewPager for each Tabs

        if (Preference.getInstance(getContext()).getPrefString("UserType").equals("Mayor")){
            setupMayorViewPager(mViewPager);
        } else if(Preference.getInstance(getContext()).getPrefString("UserType").equals("Department")) {
            setupDepartmentViewPager(mViewPager);
        }

        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);

        return view;
    }

    // Add Fragments to Tabs
    private void setupMayorViewPager(ViewPager viewPager) {
        MainMessagesPageAdapter adapter = new MainMessagesPageAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentUnassigned(), "Unassigned");
        adapter.addFragment(new FragmentOpen(), "Open");
        adapter.addFragment(new FragmentImportant(), "Important");
        viewPager.setAdapter(adapter);
    }

    private void setupDepartmentViewPager(ViewPager viewPager) {
        MainMessagesPageAdapter adapter = new MainMessagesPageAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentDepartmentOpen(), "Open");
        adapter.addFragment(new FragmentDepartmentImportant(), "Important");
        viewPager.setAdapter(adapter);
    }

}