package com.alzaem1416.atry;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class MyService extends Service {
    boolean serviceStopped;
    private Handler mHandler;
    public MyService() {
    }



    private Runnable updateRunnable = new Runnable() {


        @Override
        public void run() {

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            BroadcastReceiver mReceiver = new ScreenReceiver();
            registerReceiver(mReceiver, filter);

            if (ScreenReceiver.wasScreenOn) {
            if (serviceStopped == false)
            {

                //مهم م ه م
                ActivityManager am = (ActivityManager) MyService.this.getSystemService(ACTIVITY_SERVICE);
// The first in the list of RunningTasks is always the foreground task.
                ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
                    if(!foregroundTaskPackageName.equals("com.alzaem1416.atry")){
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 200);

                PackageManager pm = MyService.this.getPackageManager();
                PackageInfo foregroundAppPackageInfo = null;
                try {
                    foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


                String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();


                //show open app name

                Toast.makeText(getApplicationContext(),foregroundTaskAppName.toString()+" <>",Toast.LENGTH_LONG).show();
        }}

        }
            queueRunnable();
    }
    };


    private void queueRunnable() {
        // 600000 : cada 10 minutos, comprueba si hay nuevas notificaciones y actualiza la
        // notification BAR
        mHandler.postDelayed(updateRunnable, 5000);

    }
        @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(),"cmd",Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    protected void onHandleIntent(Intent intent) {
        Toast.makeText(getApplicationContext(),"Intent",Toast.LENGTH_LONG).show();

    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        BroadcastReceiver mReceiver = new ScreenReceiver();
       // if (ScreenReceiver.wasScreenOn) {
            Intent restartService = new Intent(getApplicationContext(),
                    this.getClass());
            restartService.setPackage(getPackageName());
            PendingIntent restartServicePI = PendingIntent.getService(
                    getApplicationContext(), 1, restartService,
                    PendingIntent.FLAG_ONE_SHOT);

            //Restart the service once it has been killed android

            //if(!foregroundTaskAppName.equals("@string/app_name"))


            AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);
            Toast.makeText(getApplicationContext(),"task",Toast.LENGTH_LONG).show();
            serviceStopped = true;

       // }

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        serviceStopped = false;
        Toast.makeText(getApplicationContext(),"create",Toast.LENGTH_LONG).show();
        // //////////////////////////////////////MANEJADOR SIMILAR A UN HILO
        mHandler = new Handler();
        queueRunnable();
        //start a separate thread and start listening to your network object
    }

    public void onDestroy() {
        serviceStopped = true;
        Toast.makeText(getApplicationContext(),"des",Toast.LENGTH_LONG).show();
    }



    @Override
    public void onStart(Intent intent, int startid) {
        //serviceStopped = false;
        Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_LONG).show();
    }


}
