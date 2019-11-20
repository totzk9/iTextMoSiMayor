package gte.com.itextmosimayor.fragments;

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
import gte.com.itextmosimayor.adapters.FragmentImportantAdapter;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.viewmodels.MessagesViewModel;

import static gte.com.itextmosimayor.fragments.MessagesFragment.tabs;

public class FragmentImportant extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView view_important;
    private FragmentImportantAdapter fragmentImportantAdapter;
    private MessagesViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        view_important = view.findViewById(R.id.recyclerView);

        //View Model
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MessagesViewModel.class);
        viewModel.getImportantMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> messagesData) {
                fragmentImportantAdapter.setList(messagesData);
                refreshBadge(messagesData.size());
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
        viewModel.initImportant();
        viewModel.getImportantMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> messagesData) {
                fragmentImportantAdapter.setList(messagesData);
                refreshBadge(messagesData.size());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void initializeRecyclerView() {
        fragmentImportantAdapter = new FragmentImportantAdapter(viewModel.getImportantMessages().getValue());
        view_important.setAdapter(fragmentImportantAdapter);
        view_important.setLayoutManager(new LinearLayoutManager(getContext()));
        view_important.addItemDecoration(new DividerItemDecoration(view_important.getContext(), DividerItemDecoration.VERTICAL));
        view_important.setHasFixedSize(true);
        view_important.setItemAnimator(new DefaultItemAnimator());
        fragmentImportantAdapter.notifyDataSetChanged();
    }


    private void refreshBadge(int size) {
        if (Preference.getInstance(getContext()).getPrefBoolean("tabs")) {
            if (size < 2)
                tabs.setBadgeText(2, null);
            else
                tabs.setBadgeText(2, size + "");
        }
    }
}
