package gte.com.itextmosimayor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.models.OtherUsers;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<OtherUsers> otherUsers;

    public UsersAdapter(List<OtherUsers> otherUsers) {
        this.otherUsers = otherUsers;
    }

    public void setList(List<OtherUsers> otherUsers){
        this.otherUsers = otherUsers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.item_user_list, viewGroup, false);
        return new Item(row, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String imgURL = otherUsers.get(position).getImg();
        String name = otherUsers.get(position).getName();

        Picasso.get().load(imgURL).placeholder(R.drawable.ic_person).into(((Item) holder).imgPP);
        ((Item) holder).txtName.setText(name);
    }

    @Override
    public int getItemCount() {
        return otherUsers.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String fid, String name, String imgUrl);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class Item extends RecyclerView.ViewHolder {
        ImageView imgPP;
        TextView txtName;

        public Item(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgPP = itemView.findViewById(R.id.imgPP);
            txtName = itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemClick(
                                    otherUsers.get(position).getFid(),
                                    otherUsers.get(position).getName(),
                                    otherUsers.get(position).getImg());
                    }
                }
            });
        }
    }
}