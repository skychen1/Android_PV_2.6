/*
 * Copyright (C) 2010 Moduad Co., Ltd.
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
package org.androidpn.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.LinkedList;

import static org.androidpn.client.AppBroadcastReceiverManager.ACTION_NOTIFICATION_NETSTATE;
import static org.androidpn.client.AppBroadcastReceiverManager.DATE_NETLINKED;

/**
 * 接受网络变化广播
 */
public final class NetLinkReceiver extends BroadcastReceiver {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NetLinkReceiver.class);

    public interface NetLinkListener {
        void isNetconnected(boolean isConnected);
    }

    private LinkedList<NetLinkListener> mNetLinkListenerList = new LinkedList<>();

    public void addNetLinkListener(NetLinkListener listener) {
        mNetLinkListenerList.add(listener);
    }

    public void removeNetLinkListener(NetLinkListener listener) {
        mNetLinkListenerList.remove(listener);
    }

    public void removeAllNetLinkListener() {
        mNetLinkListenerList.clear();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "NetLinkReceiver.onReceive()...");
        String action = intent.getAction();
        Log.d(LOGTAG, "action=" + action);
        if (ACTION_NOTIFICATION_NETSTATE.equals(action)) {
            for (NetLinkListener netLinkListener : mNetLinkListenerList) {
                netLinkListener.isNetconnected(intent.getBooleanExtra(DATE_NETLINKED, true));
            }
        }

    }

}
