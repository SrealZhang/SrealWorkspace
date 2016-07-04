package com.app.sample.chatting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.app.sample.chatting.adapter.ChatDetailsListAdapter;
import com.app.sample.chatting.data.Tools;
import com.app.sample.chatting.model.ChatsDetails;
import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class ActivityChatDetails extends AppCompatActivity {
    public static String KEY_FRIEND     = "com.app.sample.chatting.FRIEND";
    public static String KEY_SNIPPET   = "com.app.sample.chatting.SNIPPET";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Friend obj, String snippet) {
        Intent intent = new Intent(activity, ActivityChatDetails.class);
        intent.putExtra(KEY_FRIEND, obj);
        intent.putExtra(KEY_SNIPPET, snippet);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, KEY_FRIEND);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Button btn_send;
    private EditText et_content;
    public static ChatDetailsListAdapter adapter;

    private ListView listview;
    private ActionBar actionBar;
    private Friend friend;
    private List<ChatsDetails> items = new ArrayList<>();
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(parent_view, KEY_FRIEND);

        // initialize conversation data
        Intent intent = getIntent();
        friend = (Friend) intent.getExtras().getSerializable(KEY_FRIEND);
        String snippet = intent.getStringExtra(KEY_SNIPPET);
        initToolbar();

        iniComponen();
        if(snippet != null){
            items.add(new ChatsDetails(999, "09:55", friend, snippet, false));
        }
        items.addAll(Constant.getChatDetailsData(this, friend));

        adapter = new ChatDetailsListAdapter(this, items);
        listview.setAdapter(adapter);
        listview.setSelectionFromTop(adapter.getCount(), 0);
        listview.requestFocus();
        registerForContextMenu(listview);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(friend.getName());
    }

    public void bindView() {
        try {
            adapter.notifyDataSetChanged();
            listview.setSelectionFromTop(adapter.getCount(), 0);
        } catch (Exception e) {

        }
    }

    public void iniComponen() {
        listview = (ListView) findViewById(R.id.listview);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.text_content);
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                items.add(items.size(), new ChatsDetails( items.size(), Constant.formatTime(System.currentTimeMillis()),friend, et_content.getText().toString(), true));
                items.add(items.size(), new ChatsDetails( items.size(), Constant.formatTime(System.currentTimeMillis()), friend, et_content.getText().toString(), false));
                et_content.setText("");
                bindView();
                hideKeyboard();
            }
        });
        et_content.addTextChangedListener(contentWatcher);
        if (et_content.length() == 0) {
            btn_send.setEnabled(false);
        }
        hideKeyboard();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setEnabled(false);
            } else {
                btn_send.setEnabled(true);
            }
            //draft.setContent(etd.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_details, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Handle click on action bar
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_sample:
                Snackbar.make(parent_view, item.getTitle() + " Clicked ", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
