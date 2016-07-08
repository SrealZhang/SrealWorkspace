/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.app.sample.chatting.activity.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.sample.chatting.MyApplication;
import com.app.sample.chatting.R;
import com.app.sample.chatting.adapter.chat.ChatAdapter;
import com.app.sample.chatting.bean.Emojicon;
import com.app.sample.chatting.bean.Faceicon;
import com.app.sample.chatting.bean.MessageChat;
import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.data.Tools;
import com.app.sample.chatting.data.emoji.DisplayRules;
import com.app.sample.chatting.event.chat.ChatPersonMessageEvent;
import com.app.sample.chatting.model.Friend;
import com.app.sample.chatting.service.XMPPConnectionService;
import com.app.sample.chatting.util.ToastUtil;
import com.app.sample.chatting.util.SaveUtil;
import com.app.sample.chatting.widget.KJChatKeyboard;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import greendao.NeoChatHistory;

/**
 * 聊天主界面
 */
public class ChatActivity extends KJActivity {

    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0x1;
    private static final int TAKE_PICTURE = 101;
    public static String KEY_FRIEND = "com.app.sample.chatting.FRIEND";
    public static String KEY_SNIPPET = "com.app.sample.chatting.SNIPPET";
    public final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    public static String chatwithWho = "";

    public Uri outputFileUri;
    @BindView(R.id.iv_takedPic)
    ImageView ivTakedPic;

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Friend obj, String snippet) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(KEY_FRIEND, obj);
        intent.putExtra(KEY_SNIPPET, snippet);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, KEY_FRIEND);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private KJChatKeyboard box;
    private ListView mRealListView;
    private ActionBar actionBar;
    private Friend friend;
    List<MessageChat> datas = new ArrayList<MessageChat>();
    private ChatAdapter adapter;
    private Chat chatOfFriend;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_chat);
        // initialize conversation data
        Intent intent = getIntent();
        friend = (Friend) intent.getExtras().getSerializable(KEY_FRIEND);
        String snippet = intent.getStringExtra(KEY_SNIPPET);
        chatOfFriend = createChat(friend.getUserId());
        chatwithWho = friend.getUserId();
        initToolbar();
        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        box = (KJChatKeyboard) findViewById(R.id.chat_msg_input_box);
        mRealListView = (ListView) findViewById(R.id.chat_listview);

        mRealListView.setSelector(android.R.color.transparent);
        initMessageInputToolBox();
        initListView();
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(friend.getName().split("@")[0]);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }


    private void initMessageInputToolBox() {
        box.setOnOperationListener(new OnOperationListener() {
            @Override
            public void send(String content) {
                try {
                    if (chatOfFriend == null) {
                        MyApplication.showToast("接收方无效");
                        return;
                    }

                    chatOfFriend.sendMessage(content);


                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                    MyApplication.showToast("发送失败");
                }
                NeoChatHistory hzChatHistory = new NeoChatHistory(
                        null, Constant.getMyOpenfireId(), chatwithWho, System.currentTimeMillis(), 1, content, true);
                //Long id, String myJID, String friendJID, Long time, Integer sendState, String body

                MessageChat message = new MessageChat(MessageChat.MSG_TYPE_TEXT, MessageChat.MSG_STATE_SUCCESS,
                        "Tom", "avatar", "Jerry",
                        "avatar", content, true, true,
                        transferLongToDate(hzChatHistory.getTime()));
                datas.add(message);
                adapter.refresh(datas);
                mRealListView.setSelection(adapter.getCount() - 1);
                SaveUtil.saveChatHistoryMessage(hzChatHistory);
//                createReplayMsg(message);
            }

            @Override
            public void selectedFace(Faceicon content) {
                MessageChat message = new MessageChat(MessageChat.MSG_TYPE_FACE, MessageChat.MSG_STATE_SUCCESS,
                        "Tom", "avatar", "Jerry", "avatar", content.getPath(), true, true, new
                        Date());
                datas.add(message);
                adapter.refresh(datas);
//                createReplayMsg(message);
            }

            @Override
            public void selectedEmoji(Emojicon emoji) {
                box.getEditTextBox().append(emoji.getValue());
            }

            @Override
            public void selectedBackSpace(Emojicon back) {
                DisplayRules.backspace(box.getEditTextBox());
            }

            @Override
            public void selectedFunction(int index) {
                switch (index) {
                    case 0:
                        goToAlbum();
                        break;
                    case 1:
                        ToastUtil.toast("跳转到相机");
                        //创建输出文件
                        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        outputFileUri = Uri.fromFile(file);
                        //生成intent
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        //启动摄像头应用程序
                        startActivityForResult(intent, TAKE_PICTURE);
                        break;
                }
            }
        });


        List<String> faceCagegory = new ArrayList<>();
//        File faceList = FileUtils.getSaveFolder("chat");
        File faceList = new File("");
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    faceCagegory.add(folder.getAbsolutePath());
                }
            }
        }

        box.setFaceData(faceCagegory);
        mRealListView.setOnTouchListener(getOnTouchListener());
    }

    private void initListView() {
        List<NeoChatHistory> historyList = SaveUtil.selectUser(chatwithWho, 0, 0, 0);
        if (historyList.size() > 0)
            for (int i = 0; i < historyList.size(); i++) {
                MessageChat message = new MessageChat(MessageChat.MSG_TYPE_TEXT, MessageChat.MSG_STATE_SUCCESS, "Tom",
                        "avatar", "Jerry", "avatar", historyList.get(i).getBody(), historyList.get(i).getIsSend(),
                        true, transferLongToDate(historyList.get(i).getTime()));
                datas.add(message);
            }

//        byte[] emoji = new byte[]{
//                (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81
//        };
//        MessageChat message = new MessageChat(MessageChat.MSG_TYPE_TEXT,
//                MessageChat.MSG_STATE_SUCCESS, "\ue415", "avatar", "Jerry", "avatar",
//                new String(emoji), false, true, new Date(System.currentTimeMillis()
//                - (1000 * 60 * 60 * 24) * 8));
//        MessageChat message1 = new MessageChat(MessageChat.MSG_TYPE_TEXT,
//                MessageChat.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry", "avatar",
//                "以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",
//                true, true, new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 8));
//        MessageChat message2 = new MessageChat(MessageChat.MSG_TYPE_PHOTO,
//                MessageChat.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry", "avatar",
//                "http://static.oschina.net/uploads/space/2015/0611/103706_rpPc_1157342.png",
//                false, true, new Date(
//                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 7));
//        MessageChat message6 = new MessageChat(MessageChat.MSG_TYPE_TEXT,
//                MessageChat.MSG_STATE_FAIL, "Tom", "avatar", "Jerry", "avatar",
//                "test send fail", true, false, new Date(
//                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 6));
//        MessageChat message7 = new MessageChat(MessageChat.MSG_TYPE_TEXT,
//                MessageChat.MSG_STATE_SENDING, "Tom", "avatar", "Jerry", "avatar",
//                "<a href=\"http://kymjs.com\">自定义链接</a>也是支持的", true, true, new Date(System.currentTimeMillis()
//                - (1000 * 60 * 60 * 24) * 6));
//
//        datas.add(message);
//        datas.add(message1);
//        datas.add(message2);
//        datas.add(message6);
//        datas.add(message7);

        adapter = new ChatAdapter(this, datas, getOnChatItemClickListener());
        mRealListView.setAdapter(adapter);
        mRealListView.setSelection(adapter.getCount() - 1);
    }

    private void createReplayMsg(MessageChat message) {
        final MessageChat reMessage = new MessageChat(message.getType(), MessageChat.MSG_STATE_SUCCESS, "Tom",
                "avatar", "Jerry", "avatar", message.getType() == MessageChat.MSG_TYPE_TEXT ? "返回:"
                + message.getContent() : message.getContent(), false,
                true, new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * (new Random().nextInt(3) + 1));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            datas.add(reMessage);
                            adapter.refresh(datas);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && box.isShow()) {
            box.hideLayout();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 跳转到选择相册界面
     */
    private void goToAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            if (resultCode != Activity.RESULT_OK) {
                //TODO 此处要将拍到的图片存到聊天数据库
                /**
                if (data != null) {
                    //检查结果是否包含缩略图

                    if s(data.hasExtra("data")) {
                        Bitmap tempPic = data.getParcelableExtra("data");
                        ivTakedPic.setImageBitmap(tempPic);
                        ivTakedPic.setVisibility(View.VISIBLE);
                    } else {
                        //如果没有缩略图数据，则说明缩略图存在Uri中
                        int width = ivTakedPic.getWidth();
                        int height = ivTakedPic.getHeight();
                        BitmapFactory.Options factoryOption = new BitmapFactory.Options();
                        factoryOption.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(outputFileUri.getPath(),factoryOption);
                        int imageWidth = factoryOption.outWidth;
                        int imageHeight = factoryOption.outHeight;
                        //确定缩略图大小
                        int scaleFactor = Math.min(imageWidth/width,imageHeight/height);
                        //将图像文件解码为图像大小并填充视图
                        factoryOption.inJustDecodeBounds = false;
                        factoryOption.inSampleSize = scaleFactor;
                        factoryOption.inPurgeable = true;

                        Bitmap bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(),factoryOption);
                        ivTakedPic.setImageBitmap(bitmap);
                    }
                }
                 */
                return;
            }
        }
        if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD) {
            Uri dataUri = data.getData();
            if (dataUri != null) {
                File file = FileUtils.uri2File(aty, dataUri);
                MessageChat message = new MessageChat(MessageChat.MSG_TYPE_PHOTO, MessageChat.MSG_STATE_SUCCESS,
                        "Tom", "avatar", "Jerry",
                        "avatar", file.getAbsolutePath(), true, true, new Date());
                datas.add(message);
                adapter.refresh(datas);
            }
        }
    }

    /**
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     *
     * @return 会隐藏输入法键盘的触摸事件监听器
     */
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                box.hideLayout();
                box.hideKeyboard(aty);
                return false;
            }
        };
    }

    /**
     * @return 聊天列表内存点击事件监听器
     */
    private OnChatItemClickListener getOnChatItemClickListener() {
        return new OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position) {
                KJLoger.debug(datas.get(position).getContent() + "点击图片的");
                ViewInject.toast(aty, datas.get(position).getContent() + "点击图片的");
            }

            @Override
            public void onTextClick(int position) {
            }

            @Override
            public void onFaceClick(int position) {
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 聊天列表中对内容的点击事件监听
     */
    public interface OnChatItemClickListener {
        void onPhotoClick(int position);

        void onTextClick(int position);

        void onFaceClick(int position);
    }

    /**
     * 创建聊天
     */
    public Chat createChat(String jid) {
        if (isLoginSucceed()) {
            ChatManager chatManager = ChatManager.getInstanceFor(getmConnection());
            return chatManager.createChat(jid);
        } else {
            MyApplication.showToast("服务器连接失败，请先连接服务器");
            return null;
        }
    }

    private boolean isLoginSucceed() {
        return XMPPConnectionService.isConnected();
    }

    private AbstractXMPPConnection getmConnection() {
        return XMPPConnectionService.getmConnection();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_details, menu);
        return true;
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEventMainThread(ChatPersonMessageEvent event) {

        if (event.getMessage().getFrom().split("@")[0].equals(chatwithWho.split("@")[0])) {
            NeoChatHistory hzChatHistory = null;
            //text消息
            //Long id, String myJID, String friendJID, Long time, Integer sendState, String body
//            MessageChat msg = new MessageChat(MessageChat.MSG_TYPE_TEXT, MessageChat.MSG_STATE_SUCCESS, "Tom",
//                    "avatar", "Jerry", "avatar", "41234", false,
//                    true, transferLongToDate(hzChatHistory.getTime()));
            MessageChat message1 = new MessageChat(MessageChat.MSG_TYPE_TEXT,
                    MessageChat.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry", "avatar",
                    event.getMessage().getBody(),
                    false, true, new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 8));
            datas.add(message1);
            adapter.refresh(datas);
            mRealListView.setSelection(adapter.getCount() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatwithWho = "";
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * 毫秒转成日期
     */
    private Date transferLongToDate(Long millSec) {
        Date date = new Date(millSec);
        return date;
    }
}
