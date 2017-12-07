package com.materialdesign.myapplication.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/12/8
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoRequestService.class);
        context.startService(i);
    }
}
