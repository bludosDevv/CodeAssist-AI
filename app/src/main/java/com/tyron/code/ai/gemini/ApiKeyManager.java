package com.tyron.code.ai.gemini;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiKeyManager {
    private static final String PREFS_NAME = "gemini_ai_prefs";
    private static final String KEY_API_KEY = "api_key";
    private static final String KEY_MODEL = "selected_model";
    private static final String DEFAULT_MODEL = "gemini-1.5-flash";
    
    private final SharedPreferences prefs;
    
    public ApiKeyManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public void saveApiKey(String apiKey) {
        prefs.edit().putString(KEY_API_KEY, apiKey).apply();
    }
    
    public String getApiKey() {
        return prefs.getString(KEY_API_KEY, null);
    }
    
    public boolean hasApiKey() {
        String key = getApiKey();
        return key != null && !key.trim().isEmpty();
    }
    
    public void saveSelectedModel(String model) {
        prefs.edit().putString(KEY_MODEL, model).apply();
    }
    
    public String getSelectedModel() {
        return prefs.getString(KEY_MODEL, DEFAULT_MODEL);
    }
    
    public void clearApiKey() {
        prefs.edit().remove(KEY_API_KEY).apply();
    }
    
    public String[] getAvailableModels() {
        return new String[]{
            "gemini-1.5-flash",
            "gemini-1.5-flash-8b",
            "gemini-1.5-pro",
            "gemini-2.0-flash-exp"
        };
    }
}
