package gte.com.itextmosimayor.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gte.com.itextmosimayor.Database.DatabaseInfo;
import gte.com.itextmosimayor.R;

public class FragmentAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Cursor cursor;

    public FragmentAllAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_conversation_text_message, viewGroup, false);
        return new Item(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(!cursor.move(position)){
            return;
        }

        String dataSent = cursor.getString(cursor.getColumnIndex(DatabaseInfo.DBInfo.DATESENT));
        String mobileNumber = cursor.getString(cursor.getColumnIndex(DatabaseInfo.DBInfo.CLIENTMOBILENUMBER));
        String content = cursor.getString(cursor.getColumnIndex(DatabaseInfo.DBInfo.CONTENT));

        ((Item)holder).txtMessage.setText(content);
        ((Item)holder).textView_message_time.setText(dataSent);
        ((Item)holder).txtNumber.setText(mobileNumber);
    }

    public int getItemCount(){
        return cursor.getCount();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView textView_message_time;
        TextView txtNumber;
//        ConstraintLayout convo;
        public Item(View itemView){
            super(itemView);
//            convo = itemView.findViewById(R.id.convo);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            textView_message_time = itemView.findViewById(R.id.textView_message_time);
            txtNumber = itemView.findViewById(R.id.txtNumber);
        }
    }
}
