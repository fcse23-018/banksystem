package com.botswanabank.project.service;

import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.createSupabaseClient;
import io.github.jan.supabase.gotrue.GoTrue;
import io.github.jan.supabase.postgrest.Postgrest;
import io.github.jan.supabase.storage.Storage;

public class SupabaseService {

    private static SupabaseService instance;
    private final SupabaseClient client;

    private SupabaseService() {
        String supabaseUrl = System.getenv("SUPABASE_URL");
        String supabaseKey = System.getenv("SUPABASE_KEY");

        client = createSupabaseClient(supabaseUrl, supabaseKey, builder -> {
            builder.install(GoTrue.INSTANCE);
            builder.install(Postgrest.INSTANCE);
            builder.install(Storage.INSTANCE);
        });
    }

    public static synchronized SupabaseService getInstance() {
        if (instance == null) {
            instance = new SupabaseService();
        }
        return instance;
    }

    public SupabaseClient getClient() {
        return client;
    }
}
