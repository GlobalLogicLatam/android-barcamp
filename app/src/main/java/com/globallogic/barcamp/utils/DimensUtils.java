package com.globallogic.barcamp.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.globallogic.barcamp.BarcampApplication;

/**
 * Created by gus on 11/11/16.
 */

public class DimensUtils {

    private static DimensUtils instance;
    private float heigthFix;
    private float widthFix;
    private float dpHeight;
    private float dpWidth;
    private float pxHeight;
    private float pxWidth;
    private Context context;

    private DimensUtils() {
        context = BarcampApplication.get().getApplicationContext();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        pxHeight = displayMetrics.heightPixels;
        pxWidth = displayMetrics.widthPixels;
        dpHeight = pxHeight / displayMetrics.density;
        dpWidth = pxWidth / displayMetrics.density;
        heigthFix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpHeight, displayMetrics);
        widthFix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, displayMetrics);
    }

    public static DimensUtils getInstance() {
        if (instance == null) {
            instance = new DimensUtils();
        }
        return instance;
    }

    public void setHeightProportion(View view, float viewPxHeight, float mockPxHeight) {
        double screenPercent = viewPxHeight / mockPxHeight;
        Double valueh = heigthFix * (screenPercent);
        view.getLayoutParams().height = valueh.intValue();
        view.requestLayout();
    }

    public void setWidthProportion(View view, float viewPxWidth, float mockPxWidth) {
        double screenPercent = viewPxWidth / mockPxWidth;
        Double valuew = widthFix * (screenPercent);
        view.getLayoutParams().width = valuew.intValue();
        view.requestLayout();
    }

    public int setFontSize(@Nullable TextView txt, float txtPxHeight, float mockPxHeight) {
        double screenPercent = txtPxHeight / mockPxHeight;
        int heightDP = convertPixelsToDp(heigthFix);
        int size = (int) (heightDP * screenPercent);
        if (txt != null) {
            txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
        return size;
    }

    private int convertPixelsToDp(float pixelValue) {
        int dp = (int) ((pixelValue) / context.getResources().getDisplayMetrics().density);
        return dp;
    }

}
