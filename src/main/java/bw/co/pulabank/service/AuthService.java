package bw.co.pulabank.service;

import bw.co.pulabank.config.SupabaseClient;
import bw.co.pulabank.model.User;
import bw.co.pulabank.model.UserRole;
import io.supabase.client.rpc.RpcBuilder;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    public User login(String username, String password) throws Exception {
        // This is a simplified login logic.
        // In a real application, you would query the database to find the user.

        // As an example, we'll create a dummy user. 
        // Replace this with your actual database query.
        User user = new User();
        user.setUsername("testuser");
        // Hashed password for "password"
        user.setPassword("$2a$10$8.j9A3.FFbIm0v/j3j5m8uY/3.d27j3fP6YqjaqB.X5aZbL3nOD.O");
        user.setRole(UserRole.CUSTOMER);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
