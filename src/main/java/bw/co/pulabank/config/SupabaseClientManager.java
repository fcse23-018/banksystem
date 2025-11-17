package bw.co.pulabank.config;

import io.supabase.client.SupabaseClient;

public class SupabaseClientManager {
    private static SupabaseClient instance;

    public static void initialize(String url, String key) {
        if (instance == null) {
            instance = new SupabaseClient(url, key);
        }
    }

    public static SupabaseClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Supabase client not initialized. Call initialize() first.");
        }
        return instance;
    }
}
