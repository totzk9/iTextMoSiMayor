package gte.com.itextmosimayor.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.modules.ColumnIndexCache;

public class FragmentDepartmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cursor cursor;
    private OnItemClickListener onItemClickListener;
    private ColumnIndexCache cache;

    public FragmentDepartmentsAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.item_department, viewGroup, false);
        return new Item(row, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        cache = new ColumnIndexCache();
        if (!cursor.moveToPosition(position))
            return;

        String departmentName = cursor.getString(cache.getColumnIndex(cursor, DBInfo.DEPARTMENTNAME));
        String departmentID = cursor.getString(cache.getColumnIndex(cursor, DBInfo.DEPARTMENTID));
        String address = cursor.getString(cache.getColumnIndex(cursor, DBInfo.STREETNAME)) + ", " +
                cursor.getString(cache.getColumnIndex(cursor, DBInfo.BARANGAY)) + ", " +
                cursor.getString(cache.getColumnIndex(cursor, DBInfo.MUNICIPALITY)) + ", " +
                cursor.getString(cache.getColumnIndex(cursor, DBInfo.PROVINCE)) + ", " +
                cursor.getString(cache.getColumnIndex(cursor, DBInfo.ZIPCODE));
        cache.clear();
        ((Item) holder).txtDepartmentName.setText(departmentName);
        ((Item) holder).txtAddress.setText(address);
        ((Item) holder).txtDepartmentID.setText(departmentID);

    }

    public int getItemCount() {
        return cursor.getCount();
    }

    public interface OnItemClickListener {
        void onItemClick(String DepartmentID);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView txtDepartmentName;
        TextView txtAddress;
        TextView txtDepartmentID;

        Item(View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtDepartmentName = itemView.findViewById(R.id.txtDepartmentName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDepartmentID = itemView.findViewById(R.id.txtDepartmentID);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(txtDepartmentID.getText().toString());
                        }
                    }
                }
            });
        }
    }
}