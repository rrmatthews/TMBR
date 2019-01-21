package teamomega.cs.brandeis.edu.tmber;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Mesfin on 11/27/2016.
 */

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, "Times Up", "5 sec have passed", "Alert");
    }

    public void createNotification (Context context, String message, String msgtext, String msgAlert){
        PendingIntent notificIntent= PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class),0);
        NotificationCompat.Builder mBuilder= (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(message)
                .setContentText(msgtext)
                .setTicker(msgAlert)
                .setSmallIcon(R.drawable.ic_stat_name);

        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());

    }
}
