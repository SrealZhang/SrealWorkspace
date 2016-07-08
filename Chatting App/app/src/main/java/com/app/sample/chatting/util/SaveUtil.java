package com.app.sample.chatting.util;

import android.util.Log;

import com.app.sample.chatting.MyApplication;
import com.app.sample.chatting.activity.chat.ChatActivity;
import com.app.sample.chatting.bean.NeoUser;
import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.event.chat.ChatPersonMessageEvent;
import com.app.sample.chatting.event.Event_SureChange;

import org.greenrobot.greendao.query.QueryBuilder;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import greendao.NeoChatHistory;
import greendao.NeoChatHistoryDao;
import greendao.NeoContractLately;
import greendao.NeoContractLatelyDao;

/**
 * Created by Yangbin on 2016/3/16.
 */
public class SaveUtil {
    private static final String TAG = "nilaiSaveUtil";

    //存储消息记录,一般是收到的消息
    //Long id, String myJID, String friendJID, Long time, Integer sendState, String body
    public static synchronized void saveChatHistoryMessage(Message message) {
        Log.d("nilaimessage", message + "");

        NeoChatHistory entity = new NeoChatHistory(null, message.getTo().split("/")[0], message.getFrom().split("/")[0], System.currentTimeMillis(), 0, message.getBody(), false);
        EventBus.getDefault().post(new ChatPersonMessageEvent(message, entity));
        saveChatHistoryMessage(entity);
    }

    //存储消息记录（完整格式）一般是发送的消息
    public static void saveChatHistoryMessage(NeoChatHistory hzChatHistory) {
        getNeoChatHistoryDao().insertOrReplace(hzChatHistory);
        String withJid = hzChatHistory.getFriendJID().split("/")[0];
        //当已经在聊天时，就不需要统计未读数量,并且不需要Bus事件来刷新界面
        Log.d("yangbin", "withJid" + withJid + "==chatwithWho" + ChatActivity.chatwithWho);
        if (!ChatActivity.chatwithWho.equals(withJid)) {
            int numMessage = 1;
            //String fromJID, Integer num, long time, String body
            QueryBuilder<NeoContractLately> qb = getNeoContractLatelyDao().queryBuilder();
            qb.where(NeoContractLatelyDao.Properties.FriendJID.eq(withJid));
            if (qb.count() > 0) numMessage = qb.list().get(0).getNum() + 1;
            SaveUtil.getChatUpdate(withJid, numMessage, hzChatHistory.getTime(), hzChatHistory.getBody() + "", withJid);
            EventBus.getDefault().post(new Event_SureChange(true));
        } else {
            SaveUtil.getChatUpdate(withJid, 0, System.currentTimeMillis(), hzChatHistory.getBody(), withJid);
        }
    }

    //修改更新最近消息
    public static void getChatUpdate(String friendJID, int numMessage, long l, String body, String nickname) {
        NeoContractLately hzContractLately = new NeoContractLately(friendJID, nickname, Constant.getMyOpenfireId(), numMessage, l, body);
        getNeoContractLatelyDao().insertOrReplace(hzContractLately);
    }

    //删除最近消息
    public static void getDelete(NeoContractLately hzContractLately) {
        getNeoContractLatelyDao().delete(hzContractLately);
    }

    //查询单聊历史记录
    public static List<NeoChatHistory> selectUser(String fromUser, int pageSize, int pageNow, long count) {
        QueryBuilder<NeoChatHistory> qb = getNeoChatHistoryDao().queryBuilder();
        qb.whereOr(NeoChatHistoryDao.Properties.FriendJID.eq(fromUser), NeoChatHistoryDao.Properties.FriendJID.eq(fromUser));
        count = qb.count();
        qb.orderAsc(NeoChatHistoryDao.Properties.Time);
//        if (count < pageSize) qb.offset((count < pageSize) ? 0 : (int) (count - pageSize));//查第几个之后的数据
//        qb.limit(pageSize);//每页多少个
//        qb.orderAsc(HZChatHistoryDao.Properties.Time);
//        Log.d("yangbin", count + "个");
        if (qb.count() == 0) {
            return new ArrayList<>();
        } else {
            return qb.list();
        }
    }

//    //查询单聊更多历史记录
//    public static List<HZChatHistory> SelectMoreChat(List<HZChatHistory> list, String fromUser, int pageSize, long count) {
//        QueryBuilder<HZChatHistory> qb = getHZChatHistoryDao().queryBuilder();
//        qb.whereOr(HZChatHistoryDao.Properties.FromJID.eq(fromUser), HZChatHistoryDao.Properties.ToJID.eq(fromUser));
//        count = qb.count();
//        qb.orderAsc(HZChatHistoryDao.Properties.Time);
//        if (count < pageSize)
//            qb.offset((count < pageSize) ? 0 : (int) (count - pageSize));//查第几个之后的数据
//        qb.limit(pageSize);//每页多少个
//        qb.orderAsc(HZChatHistoryDao.Properties.Time);
//        Log.d("yangbin", count + "个");
////        if (qb.count() == 0) {
////            return new ArrayList<>();
////        } else {
////            return qb.list();
////        }
//        return list;
//    }

    //查询房间更多历史记录
    public static List<NeoContractLately> SelectMoreRoom() {
        return null;
    }

    //读取历史记录（分页）
    public static <T> List<T> addList(List<T> listAdapter, List<T> listDb) {
        for (T item : listDb) {
            if (!listAdapter.contains(item)) {
                listAdapter.add(item);
            }
        }
        return listAdapter;
    }


    private static NeoChatHistoryDao getNeoChatHistoryDao() {
        // 通过 BaseApplication 类提供的 getDaoSession() 获取具体 Dao
        return MyApplication.getDaoSession().getNeoChatHistoryDao();
    }

    private static NeoContractLatelyDao getNeoContractLatelyDao() {
        // 通过 BaseApplication 类提供的 getDaoSession() 获取具体 Dao
        return MyApplication.getDaoSession().getNeoContractLatelyDao();
    }
}
