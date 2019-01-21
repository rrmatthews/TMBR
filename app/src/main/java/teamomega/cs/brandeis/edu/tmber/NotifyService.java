package teamomega.cs.brandeis.edu.tmber;

import android.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static teamomega.cs.brandeis.edu.tmber.MainFragment.isNotifActive;
import static teamomega.cs.brandeis.edu.tmber.MainFragment.notificationManager;

public class NotifyService extends Service {


    int notifID= 33;
    String duration;
    private DataBaseHelper mDbHelper;
    String entryID;

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.teamomega.cs.brandeis.edu.tmber.service.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mDbHelper = new DataBaseHelper(this);

        Cursor res = mDbHelper.getAllData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            duration = intent.getStringExtra("duration");
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        String strRingtonePreference = preference.getString("ring", "DEFAULT_SOUND");

        NotificationCompat.Builder notificationBuilder= (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setContentTitle("Check-In")
                            .setContentText("Is everything OK?")
                            .setTicker("TMBR")
                            .setSmallIcon(R.drawable.ic_dialog_alert)
                            .setAutoCancel(true)
                            .setSound(Uri.parse(strRingtonePreference));

        Intent intent = new Intent(this, ChoiceActivity.class);
        intent.putExtra("duration", duration);

        Cursor res = mDbHelper.getAllData();

        int count = 0;
        while(res.moveToNext()) {
            if(count == 0){
                intent.putExtra("number", res.getString(2));
                count++;
            }
        }

        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifID,notificationBuilder.build());
        isNotifActive=true;

        //Now we end the service
        stopSelf();

    }
}