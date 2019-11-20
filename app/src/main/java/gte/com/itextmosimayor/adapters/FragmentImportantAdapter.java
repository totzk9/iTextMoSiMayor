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

public class FragmentImportantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    private OnItemClickListener onItemClickListener;
    private List<MessagesData> messagesDataList;

    public void setList(List<MessagesData> messagesData) {
        this.messagesDataList = messagesData;
        notifyDataSetChanged();
    }

    public FragmentImportantAdapter(List<MessagesData> messagesDataList) {
        this.messagesDataList = messagesDataList;
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

    public class Item extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtDepartment;
        TextView txtNumber;

        Item(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtDepartment = itemView.findViewById(R.id.txtDate);
            txtNumber = itemView.findViewById(R.id.txtNumber);
        }
    }
}
