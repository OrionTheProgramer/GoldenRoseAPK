package com.example.golden_rose_apk.config;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerV1 {
    private SharedPreferences prefs;

    public SessionManagerV1(Context context) {
        prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString("TOKEN", token).apply();
    }

    public String getToken() {
        return prefs.getString("TOKEN", null);
    }

    public void saveUserRole(String role) {
        prefs.edit().putString("ROLE", role).apply();
    }

    public String getUserRole() {
        return prefs.getString("ROLE", null);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
