package com.stephen.paogou.utils;

import android.content.Context;

/**
 * Created by StephenChen on 2017/8/29.
 */

public class StringUtil {

    public static boolean isEmpty(String string){
        return string == null || string.trim().length() <= 0;
    }

    public static String getString(Context context, int stringID){
        return context.getResources().getString(stringID);
    }

}
