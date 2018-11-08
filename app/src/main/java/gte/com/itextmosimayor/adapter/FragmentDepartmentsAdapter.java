package gte.com.itextmosimayor.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;

public class FragmentDepartmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Cursor cursor;
    private OnItemClickListener onItemClickListener;

    public FragmentDepartmentsAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_department, viewGroup, false);
        return new Item(row, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(!cursor.moveToPosition(position)){
            return;
        }

        String departmentName = cursor.getString(cursor.getColumnIndex(DBInfo.DEPARTMENTNAME));
        String departmentID = cursor.getString(cursor.getColumnIndex(DBInfo.DEPARTMENTID));
        String address = cursor.getString(cursor.getColumnIndex(DBInfo.STREETNAME)) + ", " +
                cursor.getString(cursor.getColumnIndex(DBInfo.BARANGAY)) + ", " +
                cursor.getString(cursor.getColumnIndex(DBInfo.MUNICIPALITY)) + ", " +
                cursor.getString(cursor.getColumnIndex(DBInfo.PROVINCE)) + ", " +
                cursor.getString(cursor.getColumnIndex(DBInfo.ZIPCODE));

        ((Item)holder).txtDepartmentName.setText(departmentName);
        ((Item)holder).txtAddress.setText(address);
        ((Item)holder).txtDepartmentID.setText(departmentID);

    }

    public int getItemCount(){
        return cursor.getCount();
    }

    public interface OnItemClickListener {
        void onItemClick(String DepartmentID);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public class Item extends RecyclerView.ViewHolder {
        public TextView txtDepartmentName;
        public TextView txtAddress;
        public TextView txtDepartmentID;

        public Item(View itemView, final OnItemClickListener listener){
            super(itemView);
            txtDepartmentName = itemView.findViewById(R.id.txtDepartmentName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDepartmentID = itemView.findViewById(R.id.txtDepartmentID);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(txtDepartmentID.getText().toString());
                        }
                    }
                }
            });
        }
    }
}