package io.supabase.client;

/**
 * Minimal stub of the Supabase client to allow compilation when "io.supabase" library
 * is not resolved. This is a simple in-memory placeholder for integration tests
 * or local runs. Replace with the official client as needed.
 */
public class SupabaseClient {
    private final String url;
    private final String key;

    public SupabaseClient(String url, String key) {
        this.url = url;
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "SupabaseClient{" +
                "url='" + url + '\'' +
                ", key='" + (key != null ? "***" : "null") + '\'' +
                '}';
    }
}
