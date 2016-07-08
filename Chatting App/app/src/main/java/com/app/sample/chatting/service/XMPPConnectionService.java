package com.app.sample.chatting.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.sample.chatting.MyApplication;
import com.app.sample.chatting.bean.NeoUser;
import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.event.Event_FriendUpdate;
import com.app.sample.chatting.event.LoggedInEvent;
import com.app.sample.chatting.event.MyChatMessageListener;
import com.app.sample.chatting.event.TaxiConnectionListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by neo2 on 2016/7/4.
 */
public class XMPPConnectionService extends Service {
    protected static final String TAG = "nilaiSmackConnectionService";
    private XMPPTCPConnectionConfiguration mConnectionConfiguration;
    public static AbstractXMPPConnection mConnection;
    private static boolean startConnected = false;//连接是否成功
    public static boolean isConnected = false;//登陆是否成功
    public static ServiceBinder mBinder;

    public class ServiceBinder extends Binder {
        //获取 Service 实例
        public XMPPConnectionService getService() {
            return XMPPConnectionService.this;
        }
    }

    public static boolean isConnected() {
        if (!mConnection.isConnected()) isConnected = false;
        return isConnected;
    }

    public static AbstractXMPPConnection getmConnection() {
        return mConnection;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new ServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return START_STICKY;
    }

    // Handles incoming events
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            Log.e(TAG, "Stopped service");
            return;
        }
        int event = intent.getIntExtra("event", 10);
        switch (event) {
            // login
            case 0:
                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");
                if (username != null && password != null) {
                    startconnet(username, password, 0);
                }
                break;
            case 1:
                String usernameregist = intent.getStringExtra("username");
                String passwordregist = intent.getStringExtra("password");
                if (usernameregist != null && passwordregist != null) {
                    startconnet(usernameregist, passwordregist, 1);
                }
                break;
            case 3:
                String updataPassword = intent.getStringExtra("password");
                if (updataPassword != null) {
//                    changePassword(updataPassword);
                }
                break;
            default:
                disconnect();
                break;
        }
    }

    private void startconnet(final String username, final String password, final int i) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Log.d(TAG, "startconnet");
                    mConnectionConfiguration = XMPPTCPConnectionConfiguration.builder()
                            .setServiceName(Constant.XMPP_SEIVICE_NAME)
                            .setHost(Constant.XMPP_HOST)
                            .setPort(Constant.XMPP_PORT)
                            .setCompressionEnabled(false) //是否开启压缩
                            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) //是否开启安全模式
                            .setDebuggerEnabled(false)//开启调试模式
                            .setSendPresence(false)//设置是否发送Presece信息
                            .build();

                    Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);// 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友

                    mConnection = new XMPPTCPConnection(mConnectionConfiguration);
                    ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
                    reconnectionManager.setFixedDelay(10);//重联间隔
                    reconnectionManager.enableAutomaticReconnection();//开启重联机制

                    //添加重连连接监听
                    TaxiConnectionListener connectionListener = new TaxiConnectionListener();
                    mConnection.addConnectionListener(connectionListener);

                    mConnection.connect();
                    startConnected = true;

                    switch (i) {
                        case 0:
                            connectionLogin(username, password);
                            //获取离线消息
                            getOfflineMessage();
                            Presence presence = new Presence(Presence.Type.available);
                            mConnection.sendPacket(presence);
                            break;
                        case 1:
                            startRegist(username, password);
                            break;
                        default:
                            break;
                    }
                    Log.e(TAG, "服务器连接成功-------------mmmmmmmmmmmm");
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new LoggedInEvent(false));
                    Log.e(TAG, "服务器连接失败-------------原因" + e);
                    isConnected = false;
                    startConnected = false;
                }
            }
        }.start();
    }

    /**
     * 登陆
     */
    private boolean loggedIn = true;

    private void connectionLogin(final String username, final String password) {
        if (!startConnected) {
            isConnected = false;
            return;
        }
        try {
            mConnection.login(username, password);
        } catch (Exception e) {
            loggedIn = false;
            Log.e(TAG, "登陆失败" + e);
            isConnected = false;
        }
        // If the login fails, we disconnect from the server
        if (!loggedIn) {
            disconnect();
            loggedIn = true;
            EventBus.getDefault().post(new LoggedInEvent(false));
        } else {
            // Callback to LoginScreen to change the UI to the ChatScreen listview
            Log.e(TAG, "登陆成功");
            Constant.USERID = username;
            MyApplication.clearDate();
            isConnected = true;
            MyApplication.insertUserTxt(username, password);
            createChatListener();
            EventBus.getDefault().post(new LoggedInEvent(true));
        }
    }

    /**
     * 注册用户信息
     *
     * @param username     账号，是用来登陆用的，不是用户昵称
     * @param password     账号密码
     * @param //attributes 账号其他属性，参考AccountManager.getAccountAttributes()的属性介绍
     * @return
     */
    public void startRegist(String username, String password) {
        if (!startConnected) {
            Log.e(TAG, "未连接网络");
            EventBus.getDefault().post(new LoggedInEvent(false, "未连接"));
            return;
        }
        try {
            AccountManager.getInstance(mConnection).createAccount(username, password);
            EventBus.getDefault().post(new LoggedInEvent(true, "注册成功，\n用户名：" + username + "\n密码:" + password));
            Log.d(TAG, "注册成功");
        } catch (SmackException.NotConnectedException e) {
            Log.e(TAG, "注册失败,原因0" + e);
            EventBus.getDefault().post(new LoggedInEvent(false, "注册失败，服务器已断开"));
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            EventBus.getDefault().post(new LoggedInEvent(false, "注册失败，已存在用户或其他原因"));
            Log.e(TAG, "注册失败,原因1" + e);
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            EventBus.getDefault().post(new LoggedInEvent(false, "注册失败，未知原因"));
            Log.e(TAG, "注册失败,原因2" + e);
            e.printStackTrace();
        }
    }

    private MyChatMessageListener mChatMessageListener;

    private void createChatListener() {
        if (mConnection != null) {
//            IMContactServiceHelper.getmInstance().joinChatRoom("admin1459325150454", Constant.USERNAME, null);

            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
//            chatManager.setNormalIncluded(true); // Eliminates a few debug messages
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    if (!createdLocally) {
                        mChatMessageListener = new MyChatMessageListener();
                        chat.addMessageListener(mChatMessageListener);
                        Log.e(TAG, "ChatListener created");
                    } else {
                        mChatMessageListener = new MyChatMessageListener();
                        chat.addMessageListener(mChatMessageListener);
                        Log.e(TAG, "ChatListener created");
                    }
                }
            });
            MultiUserChatManager.getInstanceFor(mConnection).addInvitationListener(new InvitationListener() {
                @Override
                public void invitationReceived(XMPPConnection conn, final MultiUserChat room, String inviter,
                                               String reason, final String password, Message message) {

                    Log.i(TAG, room + ":收到来自 " + inviter + " 的聊天室邀请。邀请附带内容：" + reason + "--message" + message);
//                  room.addMessageListener(new MyRoomMessageListener());
//                    try {
//                        room.sendMessage("我是杨滨，请多多关照");
//                    } catch (SmackException.NotConnectedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }
        Roster roster = Roster.getInstanceFor(mConnection);
        roster.addRosterListener(
                new RosterListener() {

                    @Override
                    public void entriesAdded(Collection<String> arg0) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "--------EE:" + "entriesAdded");
                        EventBus.getDefault().post(new Event_FriendUpdate(true));
                    }

                    @Override
                    public void entriesDeleted(Collection<String> arg0) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "--------EE:" + "entriesDeleted");
                        EventBus.getDefault().post(new Event_FriendUpdate(true));
                    }

                    @Override
                    public void entriesUpdated(Collection<String> arg0) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "--------EE:" + "entriesUpdated");
                        EventBus.getDefault().post(new Event_FriendUpdate(true));
                    }

                    @Override
                    public void presenceChanged(Presence arg0) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "--------EE:" + "presenceChanged这里可以监听用户离线或者上线");
                    }

                });
    }

    //获取离线消息
    public void getOfflineMessage() {

        OfflineMessageManager offlineManager = new OfflineMessageManager(getmConnection());
        try {
            List<Message> it = offlineManager.getMessages();

            Log.d(TAG, offlineManager.supportsFlexibleRetrieval() + "");
            Log.d(TAG, "离线消息数量: " + offlineManager.getMessageCount());


            Map<String, ArrayList<Message>> offlineMsgs = new HashMap<String, ArrayList<Message>>();

            for (int i = 0; i < it.size(); i++) {
                Message message = it.get(i);
                Log.d(TAG, "收到离线消息, Received from 【" + message.getFrom()
                        + "】 message: " + message.getBody());
                String fromUser = message.getFrom().split("/")[0];

                if (offlineMsgs.containsKey(fromUser)) {
                    offlineMsgs.get(fromUser).add(message);
                } else {
                    ArrayList<Message> temp = new ArrayList<Message>();
                    temp.add(message);
                    offlineMsgs.put(fromUser, temp);
                }
            }

            //在这里进行处理离线消息集合......
            Set<String> keys = offlineMsgs.keySet();
            Iterator<String> offIt = keys.iterator();
            while (offIt.hasNext()) {
                String key = offIt.next();
                ArrayList<Message> ms = offlineMsgs.get(key);
//                TelFrame tel = new TelFrame(key);
//                ChatFrameThread cft = new ChatFrameThread(key, null);
//                cft.setTel(tel);
//                cft.start();
//                for (int i = 0; i < ms.size(); i++) {
//                    tel.messageReceiveHandler(ms.get(i));
//                }
            }
            offlineManager.deleteMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //断开链接
    public void disconnect() {
        if (mConnection != null && mConnection.isConnected()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    mConnection.disconnect();
                    Log.e(TAG, "Connection disconnected");
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}
