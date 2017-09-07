package com.github.azenhuang.praticedemo.util;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Method;

public class ProcessUtil {

    private static String currentProcessName;
    private static String currentPackageName;

    public static void init(Context context) {
        currentProcessName = getCurrentProcessName();
        currentPackageName = context.getPackageName();
    }

    public static String getCurrentProcessName() {
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method tCurrentActivityThreadMethod = clazz.getDeclaredMethod("currentActivityThread");
            tCurrentActivityThreadMethod.setAccessible(true);
            Object tCurrentActivityThread = tCurrentActivityThreadMethod.invoke(null);

            Method tGetProcessNameMethod = clazz.getDeclaredMethod("getProcessName");
            tGetProcessNameMethod.setAccessible(true);
            return (String) tGetProcessNameMethod.invoke(tCurrentActivityThread);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String peekCurrentProcessName() {
        return currentProcessName;
    }

    public static boolean isMainProcess(Context context) {
        if (TextUtils.isEmpty(currentProcessName) || TextUtils.isEmpty(currentPackageName)) {
            init(context);
        }

        return currentProcessName != null ? currentProcessName.equalsIgnoreCase(currentPackageName) : true;
    }
}
