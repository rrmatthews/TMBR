package teamomega.cs.brandeis.edu.tmber;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;


public class ChoiceActivity extends AppCompatActivity{
    Button goodBtn;
    Button badBtn;
    EditText phone;
    GPSTracker gps;
    String location = null;
    long duration;
    private DataBaseHelper mDbHelper;
    boolean gpsPermit;
    String name;
    String num;


    private ScheduleClient scheduledClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        goodBtn = (Button) findViewById(R.id.safe_btn);
        badBtn = (Button) findViewById(R.id.unsafe_btn);

        gps = new GPSTracker(ChoiceActivity.this);

        scheduledClient = new ScheduleClient(this);
        scheduledClient.doBindService();

        mDbHelper = new DataBaseHelper(this);

        Cursor res = mDbHelper.getAllData();

        if(res.getCount() != 0) {
            while(res.moveToNext()) {
                name = res.getString(1);
                num = res.getString(2);
            }
        }

        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongtitude();

            location = latitude + ", " + longitude;
        }

        duration = Long.parseLong(getIntent().getStringExtra("duration"));
        num = getIntent().getStringExtra("number");

        goodBtn.setOnClickListener( (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChoiceActivity.this);
                int checkIn = Integer.parseInt(prefs.getString("time",null).toString())*60*1000;
                c.setTimeInMillis(c.getTimeInMillis() + checkIn);

                if(c.getTimeInMillis() < duration) {
                    scheduledClient.setAlarmForNotification(c, duration, false);

                    Toast.makeText(ChoiceActivity.this, "Next prompt in "+ checkIn/60/1000 + " minutes", Toast.LENGTH_SHORT).show();
                }

                String msg = getResources().getString(R.string.goodMsg);
                sendSMS(msg);

                ChoiceActivity.super.onBackPressed();
            }
        }));
        badBtn.setOnClickListener( (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChoiceActivity.this);

                int checkIn = Integer.parseInt(prefs.getString("time", null).toString())*60*1000;
                gpsPermit = prefs.getBoolean("location", true);
                System.out.println(gpsPermit);

                c.setTimeInMillis(c.getTimeInMillis() + checkIn);

                if(c.getTimeInMillis() < duration) {
                    scheduledClient.setAlarmForNotification(c, duration, false);

                    Toast.makeText(ChoiceActivity.this, "Next prompt in "+ checkIn/60/1000 + " minutes", Toast.LENGTH_SHORT).show();
                }

                if(gpsPermit) {
                    String msg = getResources().getString(R.string.badMsg);
                    sendSMS(msg + " " + location);
                } else {
                    String msg = getResources().getString(R.string.baddMsg);
                    sendSMS(msg);
                }

                ChoiceActivity.super.onBackPressed();
            }
        }));
    }

    public void sendSMS(String msg) {
        String number = num;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, msg, null, null);
        Toast.makeText(getApplicationContext(), "send successful", Toast.LENGTH_LONG).show();
    }
}
