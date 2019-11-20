package gte.com.itextmosimayor.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.database.DatabaseInfo;
import gte.com.itextmosimayor.modules.ColumnIndexCache;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private String stats;
    private Cursor cursor;

    public MessagesAdapter(Cursor cursor, String stats) {
        this.cursor = cursor;
        this.stats = stats;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row;

        if (stats.equals("Open")) {
            row = inflater.inflate(R.layout.item_unassigned_message, viewGroup, false);
            return new ItemViewHolder(row, onItemClickListener);
        } else {
            row = inflater.inflate(R.layout.item_message_data, viewGroup, false);
            return new ItemViewHolder2(row);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ColumnIndexCache cache = new ColumnIndexCache();
        if (!cursor.moveToPosition(position))
            return;

        String messageID = cursor.getString(cache.getColumnIndex(cursor, DatabaseInfo.DBInfo.MESSAGEID));
        String dateSent = cursor.getString(cache.getColumnIndex(cursor, DatabaseInfo.DBInfo.DATESENT));
        String content = cursor.getString(cache.getColumnIndex(cursor, DatabaseInfo.DBInfo.CONTENT));
        cache.clear();

        if (stats.equals("Open")) {
            ((ItemViewHolder) holder).txtMessage.setText(content);
            ((ItemViewHolder) holder).textView_message_time.setText(dateSent);
            ((ItemViewHolder) holder).txtMessageID.setText(messageID);
        } else {
            ((ItemViewHolder2) holder).txtMessage.setText(content);
            ((ItemViewHolder2) holder).textView_message_time.setText(dateSent);

        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface OnItemClickListener {
        void onClickimgForward(String messageID);

        void onClickimgCheck(String messageID);

        void onClickimgDelete(String messageID);

        void onClickimgConfidential(String messageID);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView textView_message_time;
        TextView txtMessageID;
        ImageButton imgForwardMessage;
        ImageButton imgCheckMessage;
        ImageButton imgConfidential;
        ImageButton imgDelete;
        ConstraintLayout itemLayout;

        ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            textView_message_time = itemView.findViewById(R.id.txtDate);
            txtMessageID = itemView.findViewById(R.id.txtMessageID);
            txtMessageID.setVisibility(View.GONE);
            imgForwardMessage = itemView.findViewById(R.id.imgForwardMessage);
            imgCheckMessage = itemView.findViewById(R.id.imgCheckMessage);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgConfidential = itemView.findViewById(R.id.imgMoveConfidential);

            imgCheckMessage.setImageResource(R.drawable.ic_check);
            imgForwardMessage.setImageResource(R.drawable.ic_launch);
            imgDelete.setImageResource(R.drawable.ic_delete);
            imgConfidential.setImageResource(R.drawable.ic_move_confidential);
            imgConfidential.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onClickimgConfidential(txtMessageID.getText().toString());
                    }
                }
            });
            imgForwardMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onClickimgForward(txtMessageID.getText().toString());
                    }
                }
            });

            imgCheckMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onClickimgCheck(txtMessageID.getText().toString());
                    }
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onClickimgDelete(txtMessageID.getText().toString());
                    }
                }
            });
        }
    }

    public class ItemViewHolder2 extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView textView_message_time;
        ConstraintLayout itemLayout;

        ItemViewHolder2(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            textView_message_time = itemView.findViewById(R.id.txtDate);
        }
    }
}