package org.openpackage.asf.base.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by micfans on 23/11/2016.
 */

public class AppVersion {

    private static final Loger loger = Loger.getLoger(AppVersion.class);

    /**
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {

        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            loger.e("Can not get version code, " + e.getMessage(), e);
        }
        return info == null ? -1: info.versionCode;
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            loger.e("Can not get version name, " + e.getMessage(), e);
        }
        return info == null ? null : info.versionName;
    }
}
