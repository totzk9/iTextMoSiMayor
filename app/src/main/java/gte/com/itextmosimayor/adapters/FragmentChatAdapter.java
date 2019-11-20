package gte.com.itextmosimayor.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.database.Queries;
import gte.com.itextmosimayor.models.LastMessagesData;
import gte.com.itextmosimayor.modules.ColumnIndexCache;
import gte.com.itextmosimayor.modules.GetTimeAgo;

public class FragmentChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<LastMessagesData> messages;
    private Context context;

    public FragmentChatAdapter(Context context, List<LastMessagesData> messages) {
        this.messages = messages;
        this.context = context;
    }

    public void setList(List<LastMessagesData> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.item_chat, viewGroup, false);
        return new ChatHolder(row, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Cursor cursor = Queries.getInstance(context).getDepartments(messages.get(position).getDepartmentID());
        ColumnIndexCache cache = new ColumnIndexCache();
        String departmentName = "Mayor";

        try {
            if (cursor != null && cursor.moveToFirst()) {
                departmentName = cursor.getString(cache.getColumnIndex(cursor, DBInfo.DEPARTMENTNAME));
                cache.clear();
            }
        } catch (Exception ignore) {
        }

        Date date1 = messages.get(position).getTime();
        String imgURL = messages.get(position).getImg();
        String name = messages.get(position).getName();
        String text;
        if(messages.get(position).getType().equals("TEXT")){
            if (!messages.get(position).getSenderId().equals(messages.get(position).getFid()))
                text = "You: " + messages.get(position).getText();
            else
                text = messages.get(position).getText();
        } else {
            if (!messages.get(position).getSenderId().equals(messages.get(position).getFid()))
                text = "You sent a photo";
            else
                text = messages.get(position).getSenderName() + " sent a photo";
        }

        ((ChatHolder) holder).txtMessage.setText(text);
        ((ChatHolder) holder).txtDate.setText(GetTimeAgo.getTimeAgo(date1.getTime()));
        ((ChatHolder) holder).txtMessage.setText(text);
        ((ChatHolder) holder).txtDepartment.setText(departmentName);
        ((ChatHolder) holder).txtName.setText(name);
        Picasso.get()
                .load(imgURL)
                .fit()
                .centerInside()
                .placeholder(R.drawable.ic_person)
                .into(((ChatHolder) holder).imgPP);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String fid, String name, String imgUrl);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        CircleImageView imgPP;
        TextView txtDepartment;
        TextView txtName;
        TextView txtDate;
        TextView txtMessage;

        ChatHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgPP = itemView.findViewById(R.id.imgPP);
            txtDepartment = itemView.findViewById(R.id.txtDepartment);
            txtName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtMessage = itemView.findViewById(R.id.txtMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemClick(
                                    messages.get(position).getFid(),
                                    messages.get(position).getName(),
                                    messages.get(position).getImg());
                    }
                }
            });
        }
    }
}