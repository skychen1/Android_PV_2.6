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

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import org.androidpn.utils.EventBusUtils;
import org.androidpn.utils.XmppEvent;

import java.util.Random;

/**
 * This class is to notify the user of messages with NotificationManager.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class Notifier {

    private static final String LOGTAG = "Notifier";

    private static final Random random = new Random(System.currentTimeMillis());

    private Context context;

    private SharedPreferences sharedPrefs;

    private NotificationManager notificationManager;

    public Notifier(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void notify(String notificationId, String apiKey, String title, String message, String uri) {

        Log.e(LOGTAG, "notify()...");
        Log.e(LOGTAG, "notificationId=" + notificationId);
        Log.e(LOGTAG, "notificationApiKey=" + apiKey);
        Log.e(LOGTAG, "notificationTitle=" + title);
        Log.e(LOGTAG, "notificationMessage=" + message);
        Log.e(LOGTAG, "notificationUri=" + uri);

        EventBusUtils.postSticky(new XmppEvent.EventPushMessageNum(message));
        //注释掉home界面显示提示和跳转Activity
        //        if (isNotificationEnabled()) {
        //            // Show the toast
        //            if (isNotificationToastEnabled()) {
        //                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        //            }
        //
        //            // Notification
        //            Notification.Builder notification = new Notification.Builder(context);
        //            notification.setSmallIcon(getNotificationIcon());
        //            notification.setContentText("新提示");
        //            notification.setContentTitle(title);
        //            notification.setDefaults(Notification.DEFAULT_LIGHTS);
        //            //notification.icon = getNotificationIcon();
        //            //notification.defaults = Notification.DEFAULT_LIGHTS;
        //            if (isNotificationSoundEnabled()) {
        //                notification.setDefaults(Notification.DEFAULT_SOUND);
        //                //notification.defaults |= Notification.DEFAULT_SOUND;
        //            }
        //            if (isNotificationVibrateEnabled()) {
        //                notification.setDefaults(Notification.DEFAULT_VIBRATE);
        //                //notification.defaults |= Notification.DEFAULT_VIBRATE;
        //            }
        //            notification.setAutoCancel(true);
        //            notification.setWhen(System.currentTimeMillis());
        //            //notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //            //notification.when = System.currentTimeMillis();
        //            notification.setTicker(message);
        //
        //            Intent intent = new Intent(context,
        //                    NotificationDetailsActivity.class);
        //            intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        //            intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
        //            intent.putExtra(Constants.NOTIFICATION_TITLE, title);
        //            intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
        //            intent.putExtra(Constants.NOTIFICATION_URI, uri);
        //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //
        //            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
        //                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //            //notification.setLatestEventInfo(context, title, message, contentIntent);
        //            notification.setContentIntent(contentIntent);
        //            Notification notificationBuilder = null;
        //            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
        //                notificationBuilder = notification.build();
        //            }
        //            notificationBuilder.flags = Notification.FLAG_AUTO_CANCEL;
        //            notificationManager.notify(random.nextInt(), notificationBuilder);
        //        } else {
        //            Log.w(LOGTAG, "Notificaitons disabled.");
        //        }
    }

    private int getNotificationIcon() {
        return sharedPrefs.getInt(Constants.NOTIFICATION_ICON, 0);
    }

    private boolean isNotificationEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED,
                true);
    }

    private boolean isNotificationSoundEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
    }

    private boolean isNotificationVibrateEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
    }

    private boolean isNotificationToastEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
    }

}
