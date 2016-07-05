package com.app.sample.chatting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.sample.chatting.adapter.FriendsListAdapter;
import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.data.Tools;
import com.app.sample.chatting.model.Friend;
import com.app.sample.chatting.widget.DividerItemDecoration;

public class ActivitySelectFriend extends BaseActivity {

    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private FriendsListAdapter mAdapter;
    private SearchView search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat);
        initToolbar();
        initComponent();
        // specify an adapter (see also next example)
        mAdapter = new FriendsListAdapter(this, Constant.getFriendsData(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int position) {
                ActivityChatDetails.navigate(ActivitySelectFriend.this, v.findViewById(R.id.lyt_parent), obj, null);
            }
        });

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
	}

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setSubtitle(Constant.getFriendsData(this).size()+" friends");
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_chat, menu);
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return true;
            }
        });
        search.onActionViewCollapsed();
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search: {
                // this do magic
                supportInvalidateOptionsMenu();
                return true;
            }
        }
        return false;
    }
}
