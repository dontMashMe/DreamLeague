package com.example.dreamleague.DataModels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Utils {

    public Utils(String position, int Balance) {
        Utils.position = position;
        Utils.balance = Balance;
    }
    public static int balance;
    public static String position;


    //getter i setter za trenutan tjedan
    public static void putCurrentWeekSharedPreference(Context context, int currentWeek){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("WEEK", currentWeek);
        editor.commit(); //mora biti commit jer inace ne radi idk
    }

    public static int getCurrentWeek(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("WEEK", 0);
    }

    public static void putBalance(Context context, int balance){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("BALANCE", balance);
        editor.commit();
    }

    public static int getBalance(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("BALANCE", 0);
    }
}
