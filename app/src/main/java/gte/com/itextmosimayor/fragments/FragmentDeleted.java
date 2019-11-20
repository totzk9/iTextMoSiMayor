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
import gte.com.itextmosimayor.adapters.FragmentDeletedAdapter;
import gte.com.itextmosimayor.activities.dialogs.Messages;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.viewmodels.DeletedMessagesViewModel;

public class FragmentDeleted extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView view_trash;
    private FragmentDeletedAdapter fragmentDeletedAdapter;
    private DeletedMessagesViewModel viewModel;

    public static FragmentDeleted newInstance() {
        return new FragmentDeleted();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        view_trash = view.findViewById(R.id.recyclerView);

        //View Model
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(DeletedMessagesViewModel.class);
        viewModel.getMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> msgData) {
                fragmentDeletedAdapter.setList(msgData);
            }
        });
        initializeRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        initializeViewModel();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initializeViewModel() {
        viewModel.init();
        viewModel.getMessages().observe(this, new Observer<List<MessagesData>>() {
            @Override
            public void onChanged(List<MessagesData> msgData) {
                fragmentDeletedAdapter.setList(msgData);
            }
        });
    }

    private void initializeRecyclerView() {
        fragmentDeletedAdapter = new FragmentDeletedAdapter(viewModel.getMessages().getValue());
        fragmentDeletedAdapter.setOnItemClickListener(new FragmentDeletedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String mobileNumber) {
                Intent intent = new Intent(getActivity(), Messages.class);
                Bundle bundle = new Bundle();
                bundle.putString("MobileNumber", mobileNumber);
                bundle.putString("Status", "Deleted");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        view_trash.setAdapter(fragmentDeletedAdapter);
        view_trash.setLayoutManager(new LinearLayoutManager(getContext()));
        view_trash.addItemDecoration(new DividerItemDecoration(view_trash.getContext(), DividerItemDecoration.VERTICAL));
        view_trash.setHasFixedSize(true);
        view_trash.setItemAnimator(new DefaultItemAnimator());
        fragmentDeletedAdapter.notifyDataSetChanged();
    }
}
