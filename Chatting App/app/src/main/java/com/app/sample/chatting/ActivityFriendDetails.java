package com.app.sample.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.sample.chatting.activity.chat.ChatActivity;
import com.app.sample.chatting.model.Friend;
import com.app.sample.chatting.util.FileSave;
import com.app.sample.chatting.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ActivityFriendDetails extends BaseActivity {

    public static final String EXTRA_OBJCT = "com.app.sample.chatting";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Friend obj) {
        Intent intent = new Intent(activity, ActivityFriendDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Friend friend;
    private View parent_view;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        friend = (Friend) getIntent().getSerializableExtra(EXTRA_OBJCT);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(!TextUtils.isEmpty(friend.getName())?friend.getName():friend.getUserId().split("@")[0]);

//        ((ImageView) findViewById(R.id.image)).setImageResource(friend.getUserId());
        Picasso.with(this).load(new File(FileSave.Second_PATH + friend.getUserId() + ".jpg")).into((ImageView) findViewById(R.id.image));
        ((Button) findViewById(R.id.bt_view_photos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "View Photos Clicked ", Snackbar.LENGTH_SHORT).show();
            }
        });
        ((Button) findViewById(R.id.bt_view_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "View Gallery Clicked ", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_send_message) {
            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
            i.putExtra(ChatActivity.KEY_FRIEND, friend);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend_details, menu);
        return true;
    }

}
