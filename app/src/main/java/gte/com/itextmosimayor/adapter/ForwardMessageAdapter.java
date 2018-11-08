package gte.com.itextmosimayor.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;

public class ForwardMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Cursor cursor;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    private OnItemCheckListener onItemCheckListener;

    public ForwardMessageAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_forward_department, viewGroup, false);
        return new Item(row, onItemCheckListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(!cursor.moveToPosition(position)){
            return;
        }
        ((Item)holder).bind(position);
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

    public interface OnItemCheckListener {
        void onItemCheck(String departmentID);
        void onItemUncheck(String departmentID);

    }

    public void setOnItemCheckListener(OnItemCheckListener listener){
        onItemCheckListener = listener;
    }

    public class Item extends RecyclerView.ViewHolder{
        public TextView txtDepartmentName;
        public TextView txtAddress;
        public TextView txtDepartmentID;
        public CheckBox checkBox;

        public Item(View itemView, final OnItemCheckListener onItemCheckListener){
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
                    }
                    else  {
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
                checkBox.setChecked(false);}
            else {
                checkBox.setChecked(true);
            }
        }
    }


}