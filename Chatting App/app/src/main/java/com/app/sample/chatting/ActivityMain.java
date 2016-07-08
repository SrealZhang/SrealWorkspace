package com.app.sample.chatting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.chatting.bean.TabEntity;
import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.data.Tools;
import com.app.sample.chatting.event.Event_SureShowNum;
import com.app.sample.chatting.fragment.ChatsFragment;
import com.app.sample.chatting.fragment.FragmentAdapter;
import com.app.sample.chatting.fragment.FriendsFragment;
import com.app.sample.chatting.fragment.GroupsFragment;
import com.app.sample.chatting.fragment.NeoFragment;
import com.app.sample.chatting.service.IMContactServiceHelper;
import com.app.sample.chatting.util.FileSave;
import com.app.sample.chatting.widget.CircleTransform;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import com.squareup.picasso.Picasso;

import org.greenrobot.greendao.query.QueryBuilder;
import org.jivesoftware.smack.SmackException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import greendao.NeoContractLately;

public class ActivityMain extends BaseActivity {
    public static final String TAG = "nilaiActivityMain";
    public static String KEY_FRIEND = "com.app.sample.chatting";
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"friends", "chats", "groups", "neo"};

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage) {
        Intent intent = new Intent(activity, ActivityMain.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, KEY_FRIEND);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private DrawerLayout drawerLayout;
    public FloatingActionButton fab;
    private Toolbar searchToolbar;
    private ViewPager viewPager;

    private boolean isSearch = false;
    private FriendsFragment f_friends;
    private ChatsFragment f_chats;
    private GroupsFragment f_groups;
    private NeoFragment f_neo;
    private View parent_view;
    private TextView tv_userName, tv_userEmail;
    private ImageView iv_avatar;

    CommonTabLayout tabLayout;
    private String drawerMenuItemTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }
        parent_view = findViewById(R.id.main_content);
        setupDrawerLayout();
        initComponent();

        prepareActionBar(toolbar);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        initAction();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initAction() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        Snackbar.make(parent_view, "Add Friend Clicked", Snackbar.LENGTH_SHORT).show();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityMain.this);
                        View v = getLayoutInflater().inflate(R.layout.dialog_addfriend, null);
                        final EditText edtFriend = (EditText) v.findViewById(R.id.edt_friendId);
                        dialog.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(edtFriend.getText().toString()))
                                    edtFriend.setError("is can't null");
                                else
                                    try {
                                        IMContactServiceHelper.getmInstance().addFriend(edtFriend.getText().toString(), edtFriend.getText().toString());
                                    } catch (SmackException.NotLoggedInException e) {
                                        e.printStackTrace();
                                        Log.d(TAG + "添加好友失败！！", e + "");
                                    } catch (SmackException.NotConnectedException e) {
                                        e.printStackTrace();
                                        Log.d(TAG + "添加好友失败！！", e + "");
                                    } catch (SmackException.NoResponseException e) {
                                        e.printStackTrace();
                                        Log.d(TAG + "添加好友失败！！", e + "");
                                    }
                            }
                        });
                        dialog.show();
                        break;
                    case 1:
                        Intent i = new Intent(getApplicationContext(), ActivitySelectFriend.class);
                        startActivity(i);
                        break;
                    case 2:
                        Snackbar.make(parent_view, "Add Group Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Snackbar.make(parent_view, "Add Neo Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        tabLayout = (CommonTabLayout) findViewById(R.id.tabs);
        tabLayout.setTabData(mTabEntities);
//        tabLayout.showMsg(0, 55);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
//                    tabLayout.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });
        viewPager.setCurrentItem(1);
    }

    private void initComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewpager);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_search);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }


    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        if (f_friends == null) {
            f_friends = new FriendsFragment();
        }
        if (f_chats == null) {
            f_chats = new ChatsFragment();
        }
        if (f_groups == null) {
            f_groups = new GroupsFragment();
        }
        if (f_neo == null) {
            f_neo = new NeoFragment();
        }

        adapter.addFragment(f_friends, getString(R.string.tab_friends));
        adapter.addFragment(f_chats, getString(R.string.tab_chats));
        adapter.addFragment(f_groups, getString(R.string.tab_groups));
        adapter.addFragment(f_neo, getString(R.string.tab_neo));

        viewPager.setAdapter(adapter);
    }

    private void prepareActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if (!isSearch) {
            settingDrawer();
        }
    }

    public void setVisibilityAppBar(boolean visible) {
        CoordinatorLayout.LayoutParams layout_visible = new CoordinatorLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        CoordinatorLayout.LayoutParams layout_invisible = new CoordinatorLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        if (visible) {
            appBarLayout.setLayoutParams(layout_visible);
            fab.show();
        } else {
            appBarLayout.setLayoutParams(layout_invisible);
            fab.hide();
        }
    }

    private void settingDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setupDrawerLayout() {
        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View view1 = view.getHeaderView(0);
        tv_userName = (TextView) view1.findViewById(R.id.tv_userName);
        tv_userEmail = (TextView) view1.findViewById(R.id.tv_userEmail);
        iv_avatar = (ImageView) view1.findViewById(R.id.iv_avatar);
        tv_userName.setText(MyApplication.getUser().getUser());
        tv_userEmail.setText(MyApplication.getUser().getUser() + "@" + Constant.XMPP_HOST);
        final File f = new File(FileSave.Second_PATH + MyApplication.getUser().getUser() + "@" + Constant.XMPP_HOST + ".jpg");
        if (f.exists())
            Picasso.with(this).load(f).resize(100, 100).transform(new CircleTransform()).into(iv_avatar);
        else {
            MyApplication.getmInstance().runThread(new Thread() {
                @Override
                public void run() {
                    IMContactServiceHelper.getmInstance().getUserImage(MyApplication.getUser().getUser() + "@" + Constant.XMPP_HOST);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(ActivityMain.this).load(f).resize(100, 100).transform(new CircleTransform()).into(iv_avatar);
                        }
                    });
                }
            });
        }

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
//                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                drawerMenuItemTitle = (String) menuItem.getTitle();
                switch (drawerMenuItemTitle) {
                    case "Home":
                        //在这里执行点击Home执行相应的逻辑
                        Snackbar.make(parent_view, drawerMenuItemTitle + " Clicked ", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "Messages":
                        //在这里执行点击Messages执行相应的逻辑
                        Snackbar.make(parent_view, drawerMenuItemTitle + " Clicked ", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "Friends":
                        //在这里执行点击Friends执行相应的逻辑
                        Snackbar.make(parent_view, drawerMenuItemTitle + " Clicked ", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "聊天":
                        //在这里执行点击Discussion执行相应的逻辑
                        Snackbar.make(parent_view, drawerMenuItemTitle + " Clicked ", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "设置":
                        //在这里执行点击Discussion执行相应的逻辑
                        //  Toast.makeText(getApplication(),"faaf0",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
                        startActivity(intent);
                        break;

                    case "Sub item 1":
                        //在这里执行点击Sub item 1执行相应的逻辑
                        Snackbar.make(parent_view, drawerMenuItemTitle + " Clicked ", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "退出登录":
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                        builder.setTitle("Warn").setMessage("确定要退出").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IMContactServiceHelper.getmInstance().disconnect(ActivityMain.this);
                            }
                        }).setNegativeButton("取消", null).setCancelable(true).create().show();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isSearch) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(isSearch ? R.menu.menu_search_toolbar : R.menu.menu_main, menu);
        if (isSearch) {
            //Toast.makeText(getApplicationContext(), "Search " + isSearch, Toast.LENGTH_SHORT).show();
            final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setIconified(false);
            switch (viewPager.getCurrentItem()) {
                case 0:
                    search.setQueryHint("Search friends...");
                    break;
                case 1:
                    search.setQueryHint("Search chats...");
                    break;
                case 2:
                    search.setQueryHint("Search groups...");
                    break;
            }
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    switch (viewPager.getCurrentItem()) {
                        case 0:
                            f_friends.mAdapter.getFilter().filter(s);
                            break;
                        case 1:
                            f_chats.mAdapter.getFilter().filter(s);
                            break;
                        case 2:
                            f_groups.mAdapter.getFilter().filter(s);
                            break;
                    }
                    return true;
                }
            });
            search.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    closeSearch();
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: {
                isSearch = true;
                searchToolbar.setVisibility(View.VISIBLE);
                prepareActionBar(searchToolbar);
                supportInvalidateOptionsMenu();
                return true;
            }
            case android.R.id.home:
                closeSearch();
                return true;
            case R.id.action_notif: {
                Snackbar.make(parent_view, "Notifications Clicked", Snackbar.LENGTH_SHORT).show();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeSearch() {
        if (isSearch) {
            isSearch = false;
            if (viewPager.getCurrentItem() == 0) {
                //f_message.mAdapter.getFilter().filter("");
            } else {
                //f_contact.mAdapter.getFilter().filter("");
            }
            prepareActionBar(toolbar);
            searchToolbar.setVisibility(View.GONE);
            supportInvalidateOptionsMenu();
        }
    }


    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    public void onEventMainThread(Event_SureShowNum event) {
        if (event.isSuccessful()) {
            numMessage();
        }
    }

    //消息数量
    public synchronized void numMessage() {
        int numMessage = 0;
        //String fromJID, Integer num, long time, String body
        QueryBuilder<NeoContractLately> qb = MyApplication.getDaoSession().getNeoContractLatelyDao().queryBuilder();
        List<NeoContractLately> latelies = qb.list();
        for (int i = 0; i < latelies.size(); i++) {
            numMessage += latelies.get(i).getNum();
        }
        if (numMessage > 0) {
            if (tabLayout != null) {
                tabLayout.showMsg(1, numMessage);
                tabLayout.setMsgMargin(1, numMessage > 99 ? -20 : (numMessage > 9 ? -10 : 0), 5);
//                MsgView rtv_2_3 = tabLayout.getMsgView(1);
//                if (rtv_2_3 != null) {
//                    rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
//                }
            }
        } else {
            if (tabLayout != null)
                tabLayout.hideMsg(1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        numMessage();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
