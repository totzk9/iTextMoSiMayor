package gte.com.itextmosimayor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gte.com.itextmosimayor.R;
import gte.com.itextmosimayor.activities.dialogs.ChatActivity;
import gte.com.itextmosimayor.adapters.FragmentChatAdapter;
import gte.com.itextmosimayor.models.LastMessagesData;
import gte.com.itextmosimayor.models.OtherUsers;
import gte.com.itextmosimayor.modules.SortArrayList;


public class FragmentChat extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    private String uid;
    private FragmentChatAdapter fragmentChatAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ListenerRegistration messageListener;
    private DocumentReference docRef;
    List<String> id = new ArrayList<>();
    private ArrayList<LastMessagesData> messages = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        uid = FirebaseAuth.getInstance().getUid();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);

        initializeRecyclerView();
        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadChatData();
    }

    private void loadChatData() {
        swipeRefreshLayout.setRefreshing(true);

        messageListener = db.collection("users")
                .document(uid)
                .collection("engagedChatChannels")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for (final DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {

                            docRef = db.document("chatChannels/" + document.getDocument().getString("channelId") + "/lastMessages/mes");
                            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    final DatabaseReference ref = database.getReference("users")
                                            .child(document.getDocument().getId());
                                    messages.clear();

                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            OtherUsers users = dataSnapshot.getValue(OtherUsers.class);
                                            LastMessagesData messagesData = new LastMessagesData(documentSnapshot.getString("text"),
                                                    documentSnapshot.getString("senderId"),
                                                    documentSnapshot.getString("senderName"),
                                                    documentSnapshot.getString("type"),
                                                    documentSnapshot.getDate("time"),
                                                    users.getName(),
                                                    users.getMobileNumber(),
                                                    users.getDepartmentID(),
                                                    users.getImg(),
                                                    users.getUserID(),
                                                    users.getFid());
                                            messages.add(messagesData);

                                            fragmentChatAdapter.setList(SortArrayList.sortLastMessagesToDate(messages));
                                            swipeRefreshLayout.setRefreshing(false);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("OtherUsersError", "The read failed: " + databaseError.getCode());
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
    }

    private void initializeRecyclerView() {
        fragmentChatAdapter = new FragmentChatAdapter(getContext(), messages);
        fragmentChatAdapter.setOnItemClickListener(new FragmentChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String fid, String name, String imgUrl) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fid", fid);
                bundle.putString("name", name);
                bundle.putString("img", imgUrl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(fragmentChatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadChatData();
    }

    @Override
    public void onStop() {
        super.onStop();
        messageListener.remove();
    }
}