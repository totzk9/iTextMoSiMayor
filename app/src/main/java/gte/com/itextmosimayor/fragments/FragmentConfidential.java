package gte.com.itextmosimayor.fragments;

import android.content.Intent;
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
import gte.com.itextmosimayor.activities.dialogs.Messages;
import gte.com.itextmosimayor.adapters.FragmentConfidentialAdapter;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.viewmodels.ConfidentialMessagesViewModel;

public class FragmentConfidential extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FragmentConfidentialAdapter fragmentConfidentialAdapter;
    private ConfidentialMessagesViewModel viewModel;

    public static FragmentConfidential newInstance() {
        return new FragmentConfidential();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        recyclerView = view.findViewById(R.id.recyclerView);

        //View Model
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ConfidentialMessagesViewModel.class);
        viewModel.getMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> msgData) {
                fragmentConfidentialAdapter.setList(msgData);
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
        viewModel.init();
        viewModel.getMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> msgData) {
                fragmentConfidentialAdapter.setList(msgData);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initializeRecyclerView() {
        fragmentConfidentialAdapter = new FragmentConfidentialAdapter(viewModel.getMessages().getValue());
        fragmentConfidentialAdapter.setOnItemClickListener(new FragmentConfidentialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String MobileNumber) {
                Intent intent = new Intent(getActivity(), Messages.class);
                Bundle bundle = new Bundle();
                bundle.putString("MobileNumber", MobileNumber);
                bundle.putString("Status", "Confidential");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        recyclerView.setAdapter(fragmentConfidentialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentConfidentialAdapter.notifyDataSetChanged();
    }
}