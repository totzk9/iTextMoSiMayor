package gte.com.itextmosimayor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapters.FragmentResolvedAdapter;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.viewmodels.ResolvedMessagesViewModel;

public class FragmentResolved extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView view_resolved;
    private FragmentResolvedAdapter fragmentResolvedAdapter;
    private ResolvedMessagesViewModel viewModel;

    public static FragmentResolved newInstance() {
        return new FragmentResolved();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        view_resolved = view.findViewById(R.id.recyclerView);

        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ResolvedMessagesViewModel.class);
        switch (Preference.getInstance(getContext()).getPrefString("UserType")) {
            case "Mayor": {
                viewModel.getResolvedMessages().observe(this, new Observer<List<MessagesData>>() {
                    @Override
                    public void onChanged(List<MessagesData> messagesData) {
                        fragmentResolvedAdapter.setList(messagesData);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                fragmentResolvedAdapter = new FragmentResolvedAdapter(viewModel.getResolvedMessages().getValue());
                break;
            }
            case "Department": {
                viewModel.getDepartmentResolvedMessage().observe(this, new Observer<List<MessagesData>>() {
                    @Override
                    public void onChanged(List<MessagesData> messagesData) {
                        fragmentResolvedAdapter.setList(messagesData);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                fragmentResolvedAdapter = new FragmentResolvedAdapter(viewModel.getDepartmentResolvedMessage().getValue());
                break;
            }
        }
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

        switch (Preference.getInstance(getContext()).getPrefString("UserType")) {
            case "Mayor": {
                viewModel.init();
                viewModel.getResolvedMessages().observe(this, new Observer<List<MessagesData>>() {
                    @Override
                    public void onChanged(List<MessagesData> messagesData) {
                        fragmentResolvedAdapter.setList(messagesData);
                    }
                });
                break;
            }
            case "Department": {
                viewModel.initByDepartment();
                viewModel.getDepartmentResolvedMessage().observe(this, new Observer<List<MessagesData>>() {
                    @Override
                    public void onChanged(List<MessagesData> messagesData) {
                        fragmentResolvedAdapter.setList(messagesData);
                    }
                });
                break;
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public void initializeRecyclerView() {
        view_resolved.setAdapter(fragmentResolvedAdapter);
        view_resolved.setLayoutManager(new LinearLayoutManager(getContext()));
        view_resolved.addItemDecoration(new DividerItemDecoration(view_resolved.getContext(), DividerItemDecoration.VERTICAL));
        view_resolved.setHasFixedSize(true);
        view_resolved.setItemAnimator(new DefaultItemAnimator());
        fragmentResolvedAdapter.notifyDataSetChanged();
    }
}