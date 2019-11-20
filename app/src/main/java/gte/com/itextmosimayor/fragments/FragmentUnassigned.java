package gte.com.itextmosimayor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.activities.dialogs.Messages;
import gte.com.itextmosimayor.adapters.FragmentUnassignedAdapter;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.viewmodels.MessagesViewModel;

import static gte.com.itextmosimayor.fragments.MessagesFragment.tabs;

public class FragmentUnassigned extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView view_unassigned;
    private FragmentUnassignedAdapter fragmentUnassignedAdapter;
    private MessagesViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        view_unassigned = view.findViewById(R.id.recyclerView);

        //View Model
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MessagesViewModel.class);
        viewModel.getUnassignedMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> unassignedMessagesData) {
                fragmentUnassignedAdapter.setList(unassignedMessagesData);
                refreshBadge(unassignedMessagesData.size());
            }
        });
        initializeRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
        return view;
    }

    @Override
    public void onRefresh() {
        initializeViewModel();
    }

    private void initializeViewModel() {
        swipeRefreshLayout.setRefreshing(true);
        viewModel.initUnassigned();
        viewModel.getUnassignedMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> unassignedMessagesData) {
                fragmentUnassignedAdapter.setList(unassignedMessagesData);
                refreshBadge(unassignedMessagesData.size());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initializeRecyclerView() {
        fragmentUnassignedAdapter = new FragmentUnassignedAdapter(viewModel.getUnassignedMessages().getValue());
        fragmentUnassignedAdapter.setOnItemClickListener(new FragmentUnassignedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String MobileNumber) {
                Intent intent = new Intent(getActivity(), Messages.class);
                Bundle bundle = new Bundle();
                bundle.putString("MobileNumber", MobileNumber);
                bundle.putString("Status", "Open");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        view_unassigned.setAdapter(fragmentUnassignedAdapter);
        view_unassigned.setLayoutManager(new LinearLayoutManager(getContext()));
        view_unassigned.addItemDecoration(new DividerItemDecoration(view_unassigned.getContext(), DividerItemDecoration.VERTICAL));
        view_unassigned.setHasFixedSize(true);
        view_unassigned.setItemAnimator(new DefaultItemAnimator());
        fragmentUnassignedAdapter.notifyDataSetChanged();
    }

    private void refreshBadge(int size) {
        if (Preference.getInstance(getContext()).getPrefBoolean("tabs")) {
            if (size < 2)
                tabs.setBadgeText(0, null);
            else
                tabs.setBadgeText(0, size + "");
        }
    }
}