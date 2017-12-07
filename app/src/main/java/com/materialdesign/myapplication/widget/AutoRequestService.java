package com.materialdesign.myapplication.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.materialdesign.myapplication.config.Config;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/8
 */

public class AutoRequestService extends Service {
    private static final Object lock = new Object();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long halfAnHour = 15 * 60 * 1000L;// 每15分钟发送一次请求
        long triggerAtTime = SystemClock.elapsedRealtime() + halfAnHour;
        Intent intent2 = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(AutoRequestService.this, 0, intent2, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        Intent intent1 = new Intent(Config.ACTION_DATA_UPDATED);
        sendBroadcast(intent1);
        return flags;
    }
}
