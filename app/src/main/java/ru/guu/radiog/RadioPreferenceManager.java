package ru.guu.radiog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import ru.guu.radiog.network.model.UserData;

/**
 * Created by dmitry on 22.06.17.
 */

public class RadioPreferenceManager {
    private static final String KEY_USER_DATA = "key_user_data";
    private static final String KEY_SINGED_IN = "key_signed_in";
    private static final boolean KEY_SINGED_IN_DEFAULT = false;
    private Context mContext;

    public static RadioPreferenceManager newInstance(Context context) {
        RadioPreferenceManager preferences = new RadioPreferenceManager();
        preferences.mContext = context;
        return preferences;
    }

    public boolean getSingedIn() {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(KEY_SINGED_IN, KEY_SINGED_IN_DEFAULT);
    }

    public void setSingedIn(boolean singedIn) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        ed.putBoolean(KEY_SINGED_IN, singedIn);
        ed.apply();
    }

    @Nullable
    public UserData getUserData() {
        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(KEY_USER_DATA, null);
        return json != null ? gson.fromJson(json, UserData.class) : null;
    }

    public void putUserData(UserData userData) {
        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        ed.putString(KEY_USER_DATA, json);
        ed.apply();
    }

}
