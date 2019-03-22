package org.androidpn.client;///*
// * Copyright (C) 2010 Moduad Co., Ltd.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.androidpn.client;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.ktcd.malc.kt_ma_lc.activity.EventListActivity;
//import com.ktcd.malc.kt_ma_lc.activity.HomeTabActivity;
//import com.ktcd.malc.kt_ma_lc.activity.NoticeActivity;
//import com.ktcd.malc.kt_ma_lc.activity.OrderDetailsNewActivity;
//import com.ktcd.malc.kt_ma_lc.model.ItemInstructTypeInfo;
//import com.ktcd.malc.kt_ma_lc.model.PushOrderInfo;
//import com.ktcd.malc.utilslibrary.tools.GsonUtils;
//
//import java.util.Random;
//
///**
// * This class is to notify the user of messages with NotificationManager.
// *
// * @author Sehwan Noh (devnoh@gmail.com)
// */
//public class Notifiernew {
//
//    private static final String LOGTAG = LogUtil.makeLogTag(Notifier.class);
//
//    private static final Random random = new Random(System.currentTimeMillis());
//
//    private Context context;
//
//    private SharedPreferences sharedPrefs;
//
//    private NotificationManager notificationManager;
//
//    public Notifier(Context context) {
//        this.context = context;
//        this.sharedPrefs = context.getSharedPreferences(
//                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
//        this.notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    public void notify(String notificationId, String apiKey, String title,
//                       String message, String uri) {
//        Log.d(LOGTAG, "notify()...");
//
//        Log.d(LOGTAG, "notificationId=" + notificationId);
//        Log.d(LOGTAG, "notificationApiKey=" + apiKey);
//        Log.d(LOGTAG, "notificationTitle=" + title);
//        Log.d(LOGTAG, "notificationMessage=" + message);
//        Log.d(LOGTAG, "notificationUri=" + uri);
//
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
//            //notification.tickerText = message;
//
//            //            Intent intent;
//            //            if (uri != null
//            //                    && uri.length() > 0
//            //                    && (uri.startsWith("http:") || uri.startsWith("https:")
//            //                            || uri.startsWith("tel:") || uri.startsWith("geo:"))) {
//            //                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            //            } else {
//            //                String callbackActivityPackageName = sharedPrefs.getString(
//            //                        Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
//            //                String callbackActivityClassName = sharedPrefs.getString(
//            //                        Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");
//            //                intent = new Intent().setClassName(callbackActivityPackageName,
//            //                        callbackActivityClassName);
//            //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            //            }
//
//            Intent intent1 = new Intent(context, HomeTabActivity.class);
//            Intent intent2 = new Intent(context, NoticeActivity.class);
//            Intent intent3 = new Intent(context, OrderDetailsNewActivity.class);
//            Intent intent4 = new Intent(context, EventListActivity.class);
//            intent1.putExtra(Constants.NOTIFICATION_ID, notificationId);
//            intent1.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
//            intent1.putExtra(Constants.NOTIFICATION_TITLE, title);
//            intent1.putExtra(Constants.NOTIFICATION_MESSAGE, message);
//            intent1.putExtra(Constants.NOTIFICATION_URI, uri);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            PushOrderInfo info=GsonUtils.parseJsonWithGson(uri, PushOrderInfo.class);
//            PushOrderInfo.DataBean dataBean=info.getData();
//            Bundle bundle=new Bundle();
//            Intent[] intents = new Intent[2];
//            intents[0] = intent1;
//            if ("announcement".equals(info.getType())) {
//                intents[1] = intent2;
//            }else if ("directive".equals(info.getType())){
//                ItemInstructTypeInfo.DataBean.RowsBean rowsBean=new ItemInstructTypeInfo.DataBean.RowsBean();
//                ItemInstructTypeInfo.DataBean.RowsBean.EapdStatusBean status=new ItemInstructTypeInfo.DataBean.RowsBean.EapdStatusBean();
//                rowsBean.setCreateTime(dataBean.getCreateTime());
//                rowsBean.setUpdateTime(dataBean.getUpdateTime());
//                rowsBean.setEapdId(dataBean.getEapdId());
//                rowsBean.setEapdStep(dataBean.getEapdStep());
//                rowsBean.setEapdDes(dataBean.getEapdDes());
//                rowsBean.setEapdCreateBy(dataBean.getEapdCreateBy());
//                rowsBean.setEapdOrderType(dataBean.getEapdOrderType());
//                rowsBean.setEapdBdmId(dataBean.getEapdBdmId());
//                rowsBean.setEapdCpId(dataBean.getEapdCpId());
//                rowsBean.setRefuseReason(dataBean.getRefuseReason());
//                if (dataBean.getEapdStatus()!=null) {
//                    status.setValue(dataBean.getEapdStatus().getValue());
//                    status.setDescription(dataBean.getEapdStatus().getDescription());
//                    rowsBean.setEapdStatus(status);
//                }
//                bundle.putSerializable("rows",rowsBean);
//                intent3.putExtras(bundle);
//                intents[1] = intent3;
//            }else if("eventEvolution".equals(info.getType())){
//                intents[1] =intent4;
//            }
//            PendingIntent contentIntent = PendingIntent.getActivities(context,0, intents, PendingIntent.FLAG_UPDATE_CURRENT,bundle);
//            //notification.setLatestEventInfo(context, title, message, contentIntent);
//            notification.setContentIntent(contentIntent);
//            Notification notificationBuilder = notification.build();
//            notificationBuilder.flags=Notification.FLAG_AUTO_CANCEL;
//            notificationManager.notify(random.nextInt(), notificationBuilder);
//
//            //            Intent clickIntent = new Intent(
//            //                    Constants.ACTION_NOTIFICATION_CLICKED);
//            //            clickIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
//            //            clickIntent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
//            //            clickIntent.putExtra(Constants.NOTIFICATION_TITLE, title);
//            //            clickIntent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
//            //            clickIntent.putExtra(Constants.NOTIFICATION_URI, uri);
//            //            //        positiveIntent.setData(Uri.parse((new StringBuilder(
//            //            //                "notif://notification.adroidpn.org/")).append(apiKey).append(
//            //            //                "/").append(System.currentTimeMillis()).toString()));
//            //            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(
//            //                    context, 0, clickIntent, 0);
//            //
//            //            notification.setLatestEventInfo(context, title, message,
//            //                    clickPendingIntent);
//            //
//            //            Intent clearIntent = new Intent(
//            //                    Constants.ACTION_NOTIFICATION_CLEARED);
//            //            clearIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
//            //            clearIntent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
//            //            //        negativeIntent.setData(Uri.parse((new StringBuilder(
//            //            //                "notif://notification.adroidpn.org/")).append(apiKey).append(
//            //            //                "/").append(System.currentTimeMillis()).toString()));
//            //            PendingIntent clearPendingIntent = PendingIntent.getBroadcast(
//            //                    context, 0, clearIntent, 0);
//            //            notification.deleteIntent = clearPendingIntent;
//            //
//            //            notificationManager.notify(random.nextInt(), notification);
//
//        } else {
//            Log.w(LOGTAG, "Notificaitons disabled.");
//        }
//    }
//
//    private int getNotificationIcon() {
//        return sharedPrefs.getInt(Constants.NOTIFICATION_ICON, 0);
//    }
//
//    private boolean isNotificationEnabled() {
//        return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED,
//                true);
//    }
//
//    private boolean isNotificationSoundEnabled() {
//        return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
//    }
//
//    private boolean isNotificationVibrateEnabled() {
//        return sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
//    }
//
//    private boolean isNotificationToastEnabled() {
//        return sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
//    }
//
//}
