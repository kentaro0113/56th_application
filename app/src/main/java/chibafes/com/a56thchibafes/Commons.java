package chibafes.com.a56thchibafes;

import android.content.Context;
import android.content.SharedPreferences;

public class Commons {

    // 文字列を保存する
    public static void writeString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();
        e.putString(key, value);
        e.commit();
    }
    // int型整数を保存する
    public static void writeInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();
        e.putInt(key, value);
        e.commit();
    }
    // long型整数を保存する
    public static void writeLong(Context context, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();
        e.putLong(key, value);
        e.commit();
    }

    // 文字列を読み込む
    public static String readString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }
    // int型整数を読み込む
    public static int readInt(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        return pref.getInt(key, Statics.NONE);
    }
    // long型整数を読み込む
    public static long readLong(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        return pref.getLong(key, Statics.NONE);
    }
}
