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
import gte.com.itextmosimayor.activities.dialogs.Respond;
import gte.com.itextmosimayor.adapters.FragmentDepartmentMessagesAdapter;
import gte.com.itextmosimayor.models.MessagesData;
import gte.com.itextmosimayor.modules.Preference;
import gte.com.itextmosimayor.viewmodels.DepartmentMessagesViewModel;

import static gte.com.itextmosimayor.fragments.MessagesFragment.tabs;

public class FragmentDepartmentImportant extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView view_open;
    private FragmentDepartmentMessagesAdapter fragmentImportantAdapter;
    private DepartmentMessagesViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        view_open = view.findViewById(R.id.recyclerView);

        //View Model
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(DepartmentMessagesViewModel.class);
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
        fragmentImportantAdapter = new FragmentDepartmentMessagesAdapter(viewModel.getImportantMessages().getValue());
        fragmentImportantAdapter.setOnItemClickListener(new FragmentDepartmentMessagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String MessageID, String MobileNumber, String ConvoID) {
                Intent intent = new Intent(getActivity(), Respond.class);
                Bundle bundle = new Bundle();
                bundle.putString("MessageID", MessageID);
                bundle.putString("MobileNumber", MobileNumber);
                bundle.putString("ConvoID", ConvoID);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
            }
        });
        view_open.setAdapter(fragmentImportantAdapter);
        view_open.setLayoutManager(new LinearLayoutManager(getContext()));
        view_open.addItemDecoration(new DividerItemDecoration(view_open.getContext(), DividerItemDecoration.VERTICAL));
        view_open.setHasFixedSize(true);
        view_open.setItemAnimator(new DefaultItemAnimator());
        fragmentImportantAdapter.notifyDataSetChanged();
    }

    private void refreshBadge(int size) {
        if (Preference.getInstance(getContext()).getPrefBoolean("tabs")) {
            if (size < 2)
            tabs.setBadgeText(1, null);
        else
            tabs.setBadgeText(1, size + "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1)
            initializeViewModel();
    }
}
