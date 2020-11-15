package com.anzer.hospitalebook.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StorageHelper {

    public static boolean checkStorage() {
        boolean mExternalStorageAvailable;
        boolean mExternalStorageWriteable;
        String state = Environment.getExternalStorageState();

        switch (state) {
            case Environment.MEDIA_MOUNTED:
                // We can read and write the media
                mExternalStorageAvailable = mExternalStorageWriteable = true;
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                // We can only read the media
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
                break;
            default:
                // Something else is wrong. It may be one of many other states, but
                // all we need
                // to know is we can neither read nor write
                mExternalStorageAvailable = mExternalStorageWriteable = false;
                break;
        }
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }


    public static File createNewAttachmentFile(Context mContext, String extension) {
        File f = null;
        if (checkStorage()) {
            f = new File(mContext.getExternalFilesDir(null), createNewAttachmentName(extension));
        }
        return f;
    }
    public static synchronized String createNewAttachmentName(String extension) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(ConstantsBase.DATE_FORMAT_SORTABLE);
        String name = sdf.format(now.getTime());
        name += extension != null ? extension : "";
        return name;
    }
}
