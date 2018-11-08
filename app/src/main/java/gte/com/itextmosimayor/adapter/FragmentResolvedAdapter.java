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

public class FragmentResolvedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Cursor cursor;
    private OnItemClickListener onItemClickListener;

    public FragmentResolvedAdapter(Context context, Cursor cursor ){
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_conversation_text_message, viewGroup, false);
        return new Item(row, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(!cursor.move(position)){
            return;
        }

        String messageID = cursor.getString(cursor.getColumnIndex(DBInfo.MESSAGEID));
        String dataSent = cursor.getString(cursor.getColumnIndex(DBInfo.DATESENT));
        String mobileNumber = cursor.getString(cursor.getColumnIndex(DBInfo.CLIENTMOBILENUMBER));
        String content = cursor.getString(cursor.getColumnIndex(DBInfo.CONTENT));

        ((Item)holder).txtMessage.setText(content);
        ((Item)holder).textView_message_time.setText(dataSent);
        ((Item)holder).txtNumber.setText(mobileNumber);
        ((Item)holder).txtMessageID.setText(messageID);

    }

    public int getItemCount(){
        return cursor.getCount();
    }

    public interface OnItemClickListener {
        void onItemClick(String MessageID);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public class Item extends RecyclerView.ViewHolder {
        public TextView txtMessage;
        public TextView textView_message_time;
        public TextView txtNumber;
        public TextView txtMessageID;

        public Item(View itemView, final OnItemClickListener listener){
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            textView_message_time = itemView.findViewById(R.id.textView_message_time);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            txtMessageID = itemView.findViewById(R.id.txtMessageID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(txtMessageID.getText().toString());
                        }
                    }
                }
            });
        }
    }
}
