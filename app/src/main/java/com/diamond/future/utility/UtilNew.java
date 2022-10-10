package com.diamond.future.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class UtilNew {
    public static final String APP_DIRECTORY = "future";
    public static String getItemDir() {
        File dirReports = new File(Environment.getExternalStorageDirectory(),
                UtilNew.APP_DIRECTORY);
        if (!dirReports.exists()) {
            if (!dirReports.mkdirs()) {
                return null;
            }
        }
        return dirReports.getAbsolutePath();
    }

}
