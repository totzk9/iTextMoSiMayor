package gte.com.itextmosimayor.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;
import gte.com.itextmosimayor.modules.ColumnIndexCache;

public class ResponseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cursor cursor;
    LayoutInflater inflater;
    private ColumnIndexCache cache = new ColumnIndexCache();
    private String mobNumber;
    private String sender;

    public ResponseAdapter(Cursor cursor, String mobNumber) {
        this.cursor = cursor;
        this.mobNumber = mobNumber;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        inflater = LayoutInflater.from(viewGroup.getContext());
        cursor.moveToNext();
        View row;
        sender = cursor.getString(cache.getColumnIndex(cursor, DBInfo.SENTBY));
        cache.clear();

        if (sender.equals(mobNumber))
            row = inflater.inflate(R.layout.item_client_sender, viewGroup, false);
        else
            row = inflater.inflate(R.layout.item_mayor_sender, viewGroup, false);

        return new Item(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position))
            return;

        String content = cursor.getString(cache.getColumnIndex(cursor, DBInfo.MESSAGECONTENT));
        String date = cursor.getString(cache.getColumnIndex(cursor, DBInfo.DATESENT));
        String sentBy = cursor.getString(cache.getColumnIndex(cursor, DBInfo.SENTBY));
        cache.clear();

        ((Item) holder).textView_message_text.setText(content);
        if (((Item) holder).txtDateClient != null) {
            ((Item) holder).txtDateClient.setText(date);
        } else {
            ((Item) holder).textView_message_time.setText(date);
        }

        try {
            if (sender.equals(mobNumber))
                ((Item) holder).imgPP.setVisibility(View.GONE);
            else
                ((Item) holder).txtMayorLbl.setText(sentBy);
        } catch (Exception ignored) {
        }
    }

    public int getItemCount() {
        return cursor.getCount();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView textView_message_text;
        TextView txtDateClient;
        TextView txtMayorLbl;
        TextView textView_message_time;
        CircleImageView imgPP;

        public Item(View itemView) {
            super(itemView);
            textView_message_text = itemView.findViewById(R.id.textView_message_text);
            txtDateClient = itemView.findViewById(R.id.txtDateClient);
            textView_message_time = itemView.findViewById(R.id.textView_message_time);
            txtMayorLbl = itemView.findViewById(R.id.txtMayorLbl);
            imgPP = itemView.findViewById(R.id.imgPP);
        }
    }
}