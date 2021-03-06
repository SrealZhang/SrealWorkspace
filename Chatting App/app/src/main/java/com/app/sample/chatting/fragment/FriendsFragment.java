package com.app.sample.chatting.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.sample.chatting.ActivityFriendDetails;
import com.app.sample.chatting.ActivityMain;
import com.app.sample.chatting.R;
import com.app.sample.chatting.adapter.FriendsListAdapter;
import com.app.sample.chatting.event.Event_FriendUpdate;
import com.app.sample.chatting.model.Chat;
import com.app.sample.chatting.model.Friend;
import com.app.sample.chatting.service.IMContactServiceHelper;
import com.app.sample.chatting.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    public FriendsListAdapter mAdapter;
    private ProgressBar progressBar;
    View view;
    List<Friend> friendList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        // specify an adapter (see also next example)
        friendList = IMContactServiceHelper.getmInstance().getAllFriends();
        mAdapter = new FriendsListAdapter(getActivity(), friendList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int position) {

                ActivityFriendDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.image), obj);
            }
        });
        return view;
    }

    public void onRefreshLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    //获取整个表的数据集合
    public synchronized void getUpdate() {
        friendList = IMContactServiceHelper.getmInstance().getAllFriends();
        mAdapter.refresh(friendList);
        Log.d("NILAI", "更新了friend列表");
//        mAdapter.refresh();
    }

    public void onEventMainThread(Event_FriendUpdate event) {
        if (event.isSuccessful()) {
            getUpdate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
