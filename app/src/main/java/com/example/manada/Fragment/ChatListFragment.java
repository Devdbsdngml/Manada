package com.example.manada.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Adapter.ChatListAdapter;
import com.example.manada.R;


public class ChatListFragment extends Fragment {

    private static final String TAG = "ChatListFragment";

    private View view;
    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chatlist, container, false);
        initView();


        chatListAdapter = new ChatListAdapter();
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.chatlist_frag_recyclerview);
    }

}
