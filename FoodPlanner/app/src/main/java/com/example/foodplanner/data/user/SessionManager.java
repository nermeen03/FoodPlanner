package com.example.foodplanner.data.user;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "userSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save user details
    public void saveUser(String userId, String userName, String userEmail) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.apply();
    }

    // Get user ID
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    // Get user name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    // Get user email
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USER_ID);
    }

    // Logout user (Clear data)
    public void clearUserSession() {
        editor.clear();
        editor.apply();
    }
}
