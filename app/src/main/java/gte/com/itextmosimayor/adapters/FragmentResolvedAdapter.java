package gte.com.itextmosimayor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.models.MessagesData;

public class FragmentResolvedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessagesData> messagesDataList;

    public FragmentResolvedAdapter(List<MessagesData> messagesDataList) {
        this.messagesDataList = messagesDataList;
    }

    public void setList(List<MessagesData> messagesData) {
        this.messagesDataList = messagesData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.item_open_text_message, viewGroup, false);
        return new Item(row /* , onItemClickListener */);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String dept = messagesDataList.get(position).getDepartmentName();
        String mobileNumber = messagesDataList.get(position).getClientMobileNumber();
        String content = messagesDataList.get(position).getContent();

        ((Item) holder).txtMessage.setText(content);
        ((Item) holder).txtDepartment.setText(dept);
        ((Item) holder).txtNumber.setText(mobileNumber);
    }

    public int getItemCount() {
        return messagesDataList.size();
    }

//    public interface OnItemClickListener {
//        void onItemClick(String MessageID);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        onItemClickListener = listener;
//    }

    public class Item extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtDepartment;
        TextView txtNumber;

        Item(View itemView  /* , final OnItemClickListener listener */) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtDepartment = itemView.findViewById(R.id.txtDate);
            txtNumber = itemView.findViewById(R.id.txtNumber);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(listener != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            listener.onItemClick(txtMessageID.getText().toString(), txtNumber.getText().toString());
//                        }
//                    }
//                }
//            });
        }
    }
}
