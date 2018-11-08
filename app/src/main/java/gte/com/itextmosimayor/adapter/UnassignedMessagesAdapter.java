package gte.com.itextmosimayor.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gte.com.itextmosimayor.Database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.R;

public class UnassignedMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Cursor cursor;
    private OnItemClickListener onItemClickListener;

    public UnassignedMessagesAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_text_message, viewGroup, false);
        return new ItemViewHolder(row, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(!cursor.moveToPosition(position)){
            return;
        }

        String messageID = cursor.getString(cursor.getColumnIndex(DBInfo.MESSAGEID));
        String dataSent = cursor.getString(cursor.getColumnIndex(DBInfo.DATESENT));
        String content = cursor.getString(cursor.getColumnIndex(DBInfo.CONTENT));

        ((ItemViewHolder)holder).txtMessage.setText(content);
        ((ItemViewHolder)holder).textView_message_time.setText(dataSent);
        ((ItemViewHolder)holder).txtMessageID.setText(messageID);
    }

    public int getItemCount(){
        return cursor.getCount();
    }

    public void swapCursor(Cursor newcursor){
        if(cursor != null){
            cursor.close();
        }
        cursor = newcursor;

        if(newcursor != null){
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onClickimgForward(String messageID);
        void onClickimgCheck(String messageID);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView txtMessage;
        public TextView textView_message_time;
        public TextView txtMessageID;
        public ImageView imgForwardMessage;
        public ImageView imgCheckMessage;

        public ItemViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            textView_message_time = itemView.findViewById(R.id.textView_message_time);
            txtMessageID = itemView.findViewById(R.id.txtMessageID);
            txtMessageID.setVisibility(View.GONE);

            imgForwardMessage = itemView.findViewById(R.id.imgForwardMessage);
            imgCheckMessage = itemView.findViewById(R.id.imgCheckMessage);
            imgCheckMessage.setImageResource(R.drawable.ic_check);
            imgForwardMessage.setImageResource(R.drawable.ic_forward_message);
            imgForwardMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClickimgForward(txtMessageID.getText().toString());
                        }
                    }
                }
            });


            imgCheckMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClickimgCheck(txtMessageID.getText().toString());
                        }
                    }
                }
            });
        }
    }
}