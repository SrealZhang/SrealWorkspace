package com.app.sample.chatting.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import com.app.sample.chatting.R;
import com.app.sample.chatting.model.Chat;
import com.app.sample.chatting.model.ChatsDetails;
import com.app.sample.chatting.model.Friend;
import com.app.sample.chatting.model.Group;
import com.app.sample.chatting.model.GroupDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Constant {

    public static String USERID = "未知@192.168.0.6";// 账户

    public static final String XMPP_HOST = "192.168.0.6";// 主机地址
    public static final int XMPP_PORT = 5222;// 端口
    public static final String XMPP_SEIVICE_NAME = "192.168.0.6";// 服务器名称


    public static String getMYDB() {//数据库DB名称
        return "neodb" + USERID;
    }

    public static final int UPFILE_ING = 1;
    public static final int UPFILE_SUCCES = 2;
    public static final int UPFILE_ERROR = 3;

    public static String getMyOpenfireId() {
        return USERID;
    }

    public static Resources getStrRes(Context context) {
        return context.getResources();
    }


    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }


    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "erro ao recuperar a versão da API" + e.getMessage());
        }

        return f.floatValue();
    }

    public static List<Friend> getFriendsData(Context ctx) {
        List<Friend> items = new ArrayList<>();
        String s_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_photos);
        for (int i = 0; i < s_arr.length; i++) {
            Friend fr = new Friend(i, s_arr[i], "yangbin");
            items.add(fr);
        }
        return items;
    }

    public static List<Chat> getChatsData(Context ctx) {
        List<Chat> items = new ArrayList<>();
        String s_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_photos);
        String s_cht[] = ctx.getResources().getStringArray(R.array.chat_snippet);
        String s_date[] = ctx.getResources().getStringArray(R.array.chat_date);
        for (int i = 0; i < 10; i++) {
            items.add(new Chat(i, s_date[i], true, new Friend(s_arr[i + 5], "yangbin"), s_cht[i]));
        }
        return items;
    }

    public static List<Group> getGroupData(Context ctx) {
        List<Group> items = new ArrayList<>();
        String s_name[] = ctx.getResources().getStringArray(R.array.groups_name);
        String s_date[] = ctx.getResources().getStringArray(R.array.groups_date);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.groups_photos);

        items.add(new Group(0, s_date[0], s_name[0], "", drw_arr.getResourceId(0, -1), friendSubList(ctx, 0, 5)));
        items.add(new Group(1, s_date[1], s_name[1], "", drw_arr.getResourceId(1, -1), friendSubList(ctx, 7, 8)));
        items.add(new Group(2, s_date[2], s_name[2], "", drw_arr.getResourceId(2, -1), friendSubList(ctx, 6, 14)));

        return items;
    }

    private static ArrayList<Friend> friendSubList(Context ctx, int start, int end) {
        ArrayList<Friend> friends = new ArrayList<>();
        friends.addAll(getFriendsData(ctx));
        ArrayList<Friend> friends_ = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            friends_.add(friends.get(i));
        }
        return friends_;
    }

    public static List<ChatsDetails> getChatDetailsData(Context ctx, Friend friend) {
        List<ChatsDetails> items = new ArrayList<>();
        String s_date[] = ctx.getResources().getStringArray(R.array.chat_details_date);
        String s_content[] = ctx.getResources().getStringArray(R.array.chat_details_content);

        items.add(new ChatsDetails(0, s_date[0], friend, s_content[0], false));
        items.add(new ChatsDetails(1, s_date[1], friend, s_content[1], true));
        items.add(new ChatsDetails(2, s_date[2], friend, s_content[2], false));

        return items;
    }

    public static List<GroupDetails> getGroupDetailsData(Context ctx) {
        List<GroupDetails> items = new ArrayList<>();
        List<Friend> friends = getFriendsData(ctx);

        String s_date[] = ctx.getResources().getStringArray(R.array.group_details_date);
        String s_content[] = ctx.getResources().getStringArray(R.array.group_details_content);

        items.add(new GroupDetails(0, s_date[0], friends.get(2), s_content[0], false));
        items.add(new GroupDetails(1, s_date[1], friends.get(10), s_content[1], false));
        items.add(new GroupDetails(2, s_date[2], friends.get(7), s_content[2], false));
        items.add(new GroupDetails(3, s_date[3], friends.get(8), s_content[3], false));
        items.add(new GroupDetails(4, s_date[4], friends.get(7), s_content[4], false));
        items.add(new GroupDetails(5, s_date[5], friends.get(14), s_content[5], true));
        items.add(new GroupDetails(6, s_date[6], friends.get(0), s_content[6], false));
        items.add(new GroupDetails(7, s_date[7], friends.get(12), s_content[7], true));
        items.add(new GroupDetails(8, s_date[8], friends.get(3), s_content[8], false));

        return items;
    }

}
