package gte.com.itextmosimayor.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gte.com.itextmosimayor.Database.DatabaseHandler;
import gte.com.itextmosimayor.Database.DatabaseInfo;
import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.adapter.FragmentAllAdapter;

public class FragmentAll extends Fragment {
    RecyclerView view_all;
    SQLiteDatabase database;
    public static FragmentAll newInstance() {
        return new FragmentAll();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        DatabaseHandler db = new DatabaseHandler(getContext());
        database = db.getWritableDatabase();

        view_all = view.findViewById(R.id.view_all);
        view_all.setLayoutManager(new LinearLayoutManager(getContext()));
        view_all.setAdapter(new FragmentAllAdapter(getContext(), getAllItems()));
        view_all.addItemDecoration(new DividerItemDecoration(view_all.getContext(), DividerItemDecoration.VERTICAL));

        return view;

    }
    private Cursor getAllItems() {
        return database.query(DatabaseInfo.DBInfo.Message, null, null, null, null, null, DBInfo.MESSAGEID + " DESC");
    }
}
