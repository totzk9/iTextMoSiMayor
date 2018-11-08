package gte.com.itextmosimayor.adapter;

//
//public class ConvoRecyclerViewAdapter extends RecyclerView.Adapter<ConvoRecyclerViewAdapter.MyViewHolder>{
//
//    private LayoutInflater inflater;
//    private Context context;
//    ArrayList<String> smsMessagesList;
//
//    public ConvoRecyclerViewAdapter(Context context, ArrayList<String> MessagesList) {
//        inflater = LayoutInflater.from(context);
//        this.context = context;
//        this.smsMessagesList = MessagesList;
//    }
//
//    @Override
//    public ConvoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View sender_department = inflater.inflate(R.layout.item_department_sender, parent, false);
//        View sender_client = inflater.inflate(R.layout.item_client_sender, parent, false);
//
//        if(){
//
//        }
//        ConvoRecyclerViewHolder departmentHolder = new ConvoRecyclerViewHolder(sender_department);
//        ConvoRecyclerViewHolder clientHolder = new ConvoRecyclerViewHolder(sender_client);
//
//        return departmentHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(ConvoRecyclerViewHolder holder, int position) {
//        holder.serial_number.setText(smsMessagesList[position]);
//    }
//
//    @Override
//    public int getItemCount() {
//        return smsMessagesList.size();
//    }
//
//    class ConvoRecyclerViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView textView_message_time;
//        TextView txtMessage;
//        RelativeLayout relative_view;
//        public ConvoRecyclerViewHolder(View itemView) {
//            super(itemView);
//            relative_view = itemView.findViewById(R.id.message_root);
//            txtMessage = itemView.findViewById(R.id.txtMessage);
//            textView_message_time = itemView.findViewById(R.id.textView_message_time);
//        }
//    }
//}