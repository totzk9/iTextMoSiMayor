package gte.com.itextmosimayor.adapters;

import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.modules.ColumnIndexCache;

public class ForwardMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cursor cursor;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private OnItemCheckListener onItemCheckListener;

    public ForwardMessageAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.item_forward_department, viewGroup, false);
        return new Item(row, onItemCheckListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ColumnIndexCache cache = new ColumnIndexCache();
        if (!cursor.moveToPosition(position))
            return;
        ((Item) holder).bind(position);
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

    public interface OnItemCheckListener {
        void onItemCheck(String departmentID);

        void onItemUncheck(String departmentID);

    }

    public void setOnItemCheckListener(OnItemCheckListener listener) {
        onItemCheckListener = listener;
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView txtDepartmentName;
        TextView txtAddress;
        TextView txtDepartmentID;
        CheckBox checkBox;

        public Item(View itemView, final OnItemCheckListener onItemCheckListener) {
            super(itemView);
            txtDepartmentName = itemView.findViewById(R.id.txtDepartmentName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDepartmentID = itemView.findViewById(R.id.txtDepartmentID);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (!itemStateArray.get(position, false)) {
                        checkBox.setChecked(true);
                        itemStateArray.put(position, true);
                        onItemCheckListener.onItemCheck(txtDepartmentID.getText().toString());
                    } else {
                        checkBox.setChecked(false);
                        itemStateArray.put(position, false);
                        onItemCheckListener.onItemUncheck(txtDepartmentID.getText().toString());
                    }
                }
            });
        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
        }
    }
}