package com.manyi.mall.common.location;

import android.location.Location;

import com.manyi.mall.common.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class LBSHelper {

    public static Date getCurrentDate() {
        return Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).getTime();
    }

    private static boolean isCacheTimeOut(Location location) {
        if (location == null) {
            return true;
        }
        if (getCurrentDate().getTime() - location.getTime() >= Constants.DEFAULT_TIME_OUT) {
            return true;
        }
        return false;
    }

    public static boolean isLocationCacheValid(Location localLocation) {
        if (localLocation != null && isRightLocation(localLocation) && !isCacheTimeOut(localLocation)
                && localLocation.getAccuracy() <= 30.0) {
            return true;
        }
        return false;
    }

    public static boolean isRightLocation(Location location) {
        if (Math.abs(location.getLatitude()) <= 90 && Math.abs(location.getLongitude()) <= 180) {
            return true;
        }
        return false;
    }

}
