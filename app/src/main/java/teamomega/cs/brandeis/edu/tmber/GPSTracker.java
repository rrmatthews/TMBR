package teamomega.cs.brandeis.edu.tmber;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class GPSTracker extends Service implements LocationListener {

    private final Context context;

    boolean isNetworkEnabled = false;
    boolean isGPSEnabled = false;
    boolean canGetLocation = false;

    double latitude;
    double longtitude;

    Location location;

    private static final long MIN_DIST_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1600 * 60 * 1;

    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {

        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DIST_CHANGE_FOR_UPDATES, this);
                }
                if (locationManager != null) {

                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if(location != null) {
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DIST_CHANGE_FOR_UPDATES, this );
                    }
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public double getLatitude() {
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongtitude() {
        if (location != null) {
            longtitude = location.getLongitude();
        }
        return longtitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }


    @Override
    public void onLocationChanged (Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
