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

import android.util.Log;

import org.androidpn.utils.EventBusUtils;
import org.androidpn.utils.XmppEvent;

/**
 * A thread class for recennecting the server.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ReconnectionThread extends Thread {

    private static final String LOGTAG = LogUtil
            .makeLogTag(ReconnectionThread.class);

    private final XmppManager xmppManager;

    private int waiting;

    ReconnectionThread(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
        this.waiting = 0;
    }

    public void run() {
        try {
            while (!isInterrupted()) {

                Thread.sleep((long)10 * 1000L);
                xmppManager.connect();
                xmppManager.getConnection().connect();
                waiting++;
                Log.e("xb", "Trying to reconnect in 10 "
                        + " seconds");
                EventBusUtils.post(new XmppEvent.XmmppConnect(true));
            }
        } catch (final Exception e) {
            Log.e("xb", "e");
            EventBusUtils.post(new XmppEvent.XmmppConnect(false));
            xmppManager.getHandler().postDelayed(new Runnable() {
                public void run() {
                    xmppManager.getConnectionListener().connectionClosedOnError(e);
                    xmppManager.getConnectionListener().reconnectionFailed(e);
                }
            },10000);
        }
    }

    private int waiting() {
        if (waiting > 20) {
            return 600;
        }
        if (waiting > 13) {
            return 300;
        }
        return waiting <= 7 ? 10 : 60;
    }
}
