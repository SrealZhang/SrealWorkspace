package com.app.sample.chatting.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.app.sample.chatting.MyApplication;
import com.app.sample.chatting.event.MyRoomMessageListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Yangbin on 2015/12/22.
 * 功能辅助类
 */
public class IMContactServiceHelper {
    private static final String TAG = "yangbinIMContactService";

    public static IMContactServiceHelper mInstance;

    public static IMContactServiceHelper getmInstance() {
        if (mInstance == null) {
            Log.d(TAG, "mInstance is null");
            mInstance = new IMContactServiceHelper();
        }
        return mInstance;
    }

    //登陆注册
    public void loginorRegist(Context context, String username, String password, int event) {
        Intent mServiceIntent = new Intent(context, XMPPConnectionService.class);
        mServiceIntent.putExtra("event", event);
        mServiceIntent.putExtra("username", username);
        mServiceIntent.putExtra("password", password);
        context.startService(mServiceIntent);
    }

    //退出登录
      /*
    * This disconnection method is created here to validate if the connection is not null otherwise
    *   it may crash the application.
    * */
    public void disconnect(final Context context) {
        if (getmConnection() != null && getmConnection().isConnected()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    getmConnection().disconnect();
                    Log.e(TAG, "Connection disconnected");
                    MyApplication.finishActivityNokill();
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    return null;
                }
            }.execute();
        }
    }

    /**
     * 获取账户所有属性信息
     *
     * @return
     */
    public Set getAccountAttributes() {
        if (isLoginSucceed()) {
            try {
                return AccountManager.getInstance(getmConnection()).getAccountAttributes();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    /**
     * 获取当前登录用户的所有好友信息
     *
     * @return
     */
    public Set getAllFriends() {
        if (isLoginSucceed()) {
            return Roster.getInstanceFor(getmConnection()).getEntries();
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    String textDialog = "";
    boolean isExists = false;

    public String getFriends() {
        try {
            Set set = AccountManager.getInstance(getmConnection()).getAccountAttributes();
            Iterator iterator = set.iterator();
            /**
             * 遍历方法一，迭代遍历
             */
            for (Iterator<String> iterator1 = set.iterator(); iterator1.hasNext(); ) {
                Log.d(TAG, iterator1.next() + "");
            }
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        textDialog = "";
        //获取好友列表
        Roster roster = Roster.getInstanceFor(getmConnection());
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            textDialog = textDialog + entry;
        }
        return textDialog;
    }

    public List<Hashtable<String, String>> getData(List<Hashtable<String, String>> listContent) {
        listContent.clear();
        if (isLoginSucceed()) {
            Hashtable<String, String> table2 = new Hashtable<>();
            table2.put("User", "南京恒智服务咨询");
            table2.put("Group", "开发支持");
            listContent.add(table2);
            //获取好友列表
            Roster roster = Roster.getInstanceFor(getmConnection());
            Collection<RosterEntry> entries = roster.getEntries();
            Hashtable<String, String> table1;
            Log.d(TAG, entries + "");
            for (RosterEntry entry : entries) {
                Log.d(TAG, entry + "");
                table1 = new Hashtable<>();
                table1.put("User", entry.getName() + "");
                Log.d(TAG, "getStatus" + entry.getStatus() + "getName" + entry.getName() +
                        "getType" + entry.getType() + "getUser" + entry.getUser());
                table1.put("Group", entry.getGroups() + "");
                listContent.add(table1);
            }
        } else {
            Log.d(TAG, "获取失败，您没有连接服务器...");
        }
        Log.d(TAG, listContent.size() + " listContent.size()");
        return listContent;
    }

    /**
     * 创建群聊聊天室
     *
     * @param roomName 聊天室名字
     * @param nickName 创建者在聊天室中的昵称
     * @param password 聊天室密码
     * @return
     */
    public MultiUserChat muc = null;

    public MultiUserChat createChatRoom(String roomName, String nickName, String password) {
        if (!isLoginSucceed()) {
            MyApplication.showToast("服务器连接失败，请先连接服务器");
//            throw new NullPointerException("服务器连接失败，请先连接服务器");
        }
        try {
            // 创建一个MultiUserChat
            muc = MultiUserChatManager.getInstanceFor(getmConnection()).getMultiUserChat(roomName + "@conference." + getmConnection().getServiceName());
            // 创建聊天室
            boolean isCreated = muc.createOrJoin(nickName);
            if (isCreated) {
                // 获得聊天室的配置表单
                Form form = muc.getConfigurationForm();
                // 根据原始表单创建一个要提交的新表单。
                Form submitForm = form.createAnswerForm();
                // 向要提交的表单添加默认答复
                List fields = form.getFields();
//                for(int i = 0; fields != null && i < fields.size(); i++) {
//                    if(FormField.Type.hidden != fields.get(i).getType() &&
//                            fields.get(i).getVariable() != null) {
//                        // 设置默认值作为答复
//                        submitForm.setDefaultAnswer(fields.get(i).getVariable());
//                    }
//                }
                Log.d(TAG, fields.get(0) + "");
                // 设置聊天室的新拥有者
                List owners = new ArrayList();
                owners.add(getmConnection().getUser());// 用户JID
                submitForm.setAnswer("muc#roomconfig_roomowners", owners);
                // 设置聊天室是持久聊天室，即将要被保存下来
                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                // 房间仅对成员开放
                submitForm.setAnswer("muc#roomconfig_membersonly", false);
                // 允许占有者邀请其他人
                submitForm.setAnswer("muc#roomconfig_allowinvites", true);
                if (password != null && password.length() != 0) {
                    // 进入是否需要密码
                    submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
                    // 设置进入密码
                    submitForm.setAnswer("muc#roomconfig_roomsecret", password);
                }
                // 能够发现占有者真实 JID 的角色
                // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
                // 登录房间对话
                submitForm.setAnswer("muc#roomconfig_enablelogging", true);
                // 仅允许注册的昵称登录
                submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
                // 允许使用者修改昵称
                submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
                // 允许用户注册房间
                submitForm.setAnswer("x-muc#roomconfig_registration", false);
                // 发送已完成的表单（有默认值）到服务器来配置聊天室
                muc.sendConfigurationForm(submitForm);
            }
        } catch (XMPPException e) {
            e.printStackTrace();
            MyApplication.showToast("创建失败，会议室已存在");
            return null;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            MyApplication.showToast("创建失败，原因是：NoResponseException");
            return null;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            MyApplication.showToast("创建失败，原因是：NotConnectedException");
            return null;
        } catch (SmackException e) {
            e.printStackTrace();
            MyApplication.showToast("创建失败，原因是：SmackException");
            return null;
        }
        //设置聊天室消息监听
        muc.addMessageListener(messageListener);
        return muc;
    }

    //聊天室消息监听
    private MyRoomMessageListener messageListener = new MyRoomMessageListener();

    public List<MultiUserChat> getMucList() {
        return mucList;
    }

    //所有房间的对象
    public static List<MultiUserChat> mucList = new ArrayList<>();

    /**
     * 加入一个群聊聊天室
     *
     * @param roomName 聊天室名字
     * @param nickName 用户在聊天室中的昵称
     * @param password 聊天室密码
     * @return
     */
    public MultiUserChat joinChatRoom(String roomName, String nickName, String password) {
        if (!isLoginSucceed()) {
            throw new NullPointerException("服务器连接失败，请先连接服务器");
        }
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(getmConnection()).
                    getMultiUserChat(roomName + "@conference." + getmConnection().getServiceName());
            // 聊天室服务将会决定要接受的历史记录数量
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxChars(0);
            // history.setSince(new Date());
            // 用户加入聊天室
            if (TextUtils.isEmpty(password)) {
                muc.join(nickName);
            } else {
                muc.join(nickName, password);
            }
            //设置聊天室消息监听
            muc.addMessageListener(messageListener);
            mucList.add(muc);
            return muc;
        } catch (XMPPException | SmackException e) {
            Log.d(TAG + "聊天室XMPPException", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取发送文件的发送器
     *
     * @param jid 一个完整的jid(如：laohu@192.168.0.108/Smack
     *            后面的Smack应该客户端类型，不加这个会出错)
     * @return
     */
    public OutgoingFileTransfer getSendFileTransfer(String jid) {
        if (isLoginSucceed()) {
            return FileTransferManager.getInstanceFor(getmConnection()).createOutgoingFileTransfer(jid);
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    /**
     * 添加文件接收的监听
     *
     * @param fileTransferListener
     */
    public void addFileTransferListener(FileTransferListener fileTransferListener) {
        if (isLoginSucceed()) {
            FileTransferManager.getInstanceFor(getmConnection()).addFileTransferListener(fileTransferListener);
            return;
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    private boolean isLoginSucceed() {
        return XMPPConnectionService.isConnected();
    }

    private AbstractXMPPConnection getmConnection() {
        return XMPPConnectionService.getmConnection();
    }


}
