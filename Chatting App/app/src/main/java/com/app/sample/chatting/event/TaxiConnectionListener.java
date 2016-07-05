package com.app.sample.chatting.event;

import android.util.Log;

import com.app.sample.chatting.service.XMPPConnectionService;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.util.TimerTask;

/**
 * Created by Yangbin on 2016/2/22.
 * 重连
 */
public class TaxiConnectionListener implements ConnectionListener {
    private final String TAG = "yangbinTaxiConnectionListener";

    @Override
    public void connected(XMPPConnection connection) {
        XMPPConnectionService.isConnected = true;
        Log.i(TAG, "connected");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.i(TAG, "authenticated");
    }

    @Override
    public void connectionClosed() {
        Log.i(TAG, "connectionClosed");
        XMPPConnectionService.isConnected = false;
//        if (!XMPPConnectionService.getmConnection().isConnected()) {
//            XMPPConnectionService.isConnected = false;
//            XMPPConnectionService.getmBinder().getService().reconnet();
//        }

//        // 關閉連接
//        XMPPConnectionService.getmConnection().closeConnection();
//        // 重连服务器
//        tExit = new Timer();
//        tExit.schedule(new timetask(), logintime);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.i(TAG, "connectionClosedOnError");
        XMPPConnectionService.isConnected = false;
//        if (!XMPPConnectionService.getmConnection().isConnected()) {
//            XMPPConnectionService.isConnected = false;
//            XMPPConnectionService.getmBinder().getService().reconnet();
//        }
    }

    @Override
    public void reconnectionSuccessful() {
        XMPPConnectionService.isConnected = true;
        Log.i(TAG, "reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int seconds) {
        XMPPConnectionService.isConnected = false;
        Log.i(TAG, "reconnectingIn");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        XMPPConnectionService.isConnected = false;
        Log.i(TAG, "reconnectionFailed");
    }

    class timetask extends TimerTask {
        @Override
        public void run() {
        }
    }
}
