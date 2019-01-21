package teamomega.cs.brandeis.edu.tmber;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Edwin on 11/13/2016.
 */

public class MainFragment extends Fragment implements View.OnClickListener{

    View vi;
    Button btn;
    Long stopTime;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 0000;
    static NotificationManager notificationManager;
    static boolean isNotifActive= false;
    private ScheduleClient scheduledClient;
    private TimePicker picker;
    private DataBaseHelper mDbHelper;
    int hour;
    int mins;
    long duration;
    long checkInTime;
    boolean chPermit;
    String name;
    String num;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vi = inflater.inflate(R.layout.main_fragment, container, false);
        btn = (Button) vi.findViewById(R.id.preference_btn);

        btn.setOnClickListener(this);

        scheduledClient = new ScheduleClient(this.getActivity());
        scheduledClient.doBindService();

        mDbHelper = new DataBaseHelper(this.getActivity());

        Cursor res = mDbHelper.getAllData();

        if(res.getCount() != 0) {
            while(res.moveToNext()) {
                name = res.getString(1);
                num = res.getString(2);
            }
        }

        picker = (TimePicker) vi.findViewById(R.id.picker);

        // Inflate the layout for this fragment
        return vi;
    }

    public void onClick(final View v) {
        final Calendar c = Calendar.getInstance();

        picker.clearFocus();

        hour = picker.getCurrentHour();
        mins = picker.getCurrentMinute();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        int checkIn = Integer.parseInt(prefs.getString("time", null).toString())*60*1000;

        chPermit = prefs.getBoolean("check_in", true);

        System.out.println(chPermit);
        c.set(Calendar.MINUTE, mins);
        c.set(Calendar.HOUR, hour);

        if(hour > 12) {
            c.setTimeInMillis(c.getTimeInMillis() - 43200000);
        } else {
            c.setTimeInMillis(c.getTimeInMillis());
        }

        final Calendar checkTime = Calendar.getInstance();

        duration = c.getTimeInMillis();
        checkInTime = checkTime.getTimeInMillis();

        checkTime.setTimeInMillis(checkTime.getTimeInMillis() + checkIn);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        switch(v.getId()) {
            case R.id.preference_btn:
                if(btn.getText().toString().equals(getString(R.string.start))) {
                    if(mDbHelper.getAllData().getCount() == 0){
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this.getActivity());
                        dlgAlert.setMessage("Please select a buddy before starting!");
                        dlgAlert.setTitle("No Buddy!");
                        dlgAlert.setPositiveButton("OK",null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    } else {
                        btn.setText(getText(R.string.stop).toString());
                        String msg = getResources().getString(R.string.startMsg);
                        sendSMS(msg);

                        if(chPermit && checkInTime < duration) {
                            scheduledClient.setAlarmForNotification(checkTime, duration, false);
                        }
                        scheduledClient.setAlarmForNotification(c, duration,  true);

                        Toast.makeText(this.getActivity(), "Be Safe Out There!", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    btn.setText(getString(R.string.start));

                       scheduledClient.doUnbindService();
                }
                break;
        }
    }

    public void sendSMS(String msg) {
        String number = num;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, msg, null, null);
    }
}