package com.carusto.ReactNativePjSip.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;

public class NotificationUtils {

    public static Notification buildNotification(Context context, Map notificationConfig) {
        if (notificationConfig == null) {
            Log.e("NotificationUtils", "buildNotification: invalid config");
            return null;
        }
        Class mainActivityClass = getMainActivityClass(context);
        if (mainActivityClass == null) {
            return null;
        }
        Intent notificationIntent = new Intent(context, mainActivityClass);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = notificationConfig.get("channelId").toString();
            if (channelId == null) {
                Log.e("NotificationUtils", "buildNotification: invalid channelId");
                return null;
            }
            notificationBuilder = new Notification.Builder(context, channelId);
        } else {
            notificationBuilder = new Notification.Builder(context);
        }

        notificationBuilder.setContentTitle(notificationConfig.get("title").toString())
                .setContentText(notificationConfig.get("text").toString())
                .setPriority(Notification.PRIORITY_MIN)
                .setContentIntent(pendingIntent);

        if (notificationConfig.containsKey("subtitle")) {
            notificationBuilder.setSubText(notificationConfig.get("subtitle").toString());
        }

        if (notificationConfig.containsKey("group")) {
            notificationBuilder.setGroup(notificationConfig.get("group").toString());
        }

        if (notificationConfig.containsKey("icon")) {
            String iconName = notificationConfig.get("icon").toString();
            if (iconName != null) {
                notificationBuilder.setSmallIcon(getResourceIdForResourceName(context, iconName));
            }
        }

        return notificationBuilder.build();
    }

    public static Class getMainActivityClass(Context context) {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent == null || launchIntent.getComponent() == null) {
            Log.e("NotificationUtils", "Failed to get launch intent or component");
            return null;
        }
        try {
            return Class.forName(launchIntent.getComponent().getClassName());
        } catch (ClassNotFoundException e) {
            Log.e("NotificationUtils", "Failed to get main activity class");
            return null;
        }
    }

    private static int getResourceIdForResourceName(Context context, String resourceName) {
        int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        if (resourceId == 0) {
            resourceId = context.getResources().getIdentifier(resourceName, "mipmap", context.getPackageName());
        }
        return resourceId;
    }

}
