package com.uezo.uezo_av2;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;


public class APIUtils {
    UserAPI restAPI;
    public Boolean checkUsername(String username, String url) {
        Gson gson = new Gson();
        restAPI = new UserAPI();
        String response = UserAPI.executeInBackground(() -> UserAPI.GetRequest(url));

        assert response != null;
        if (!response.equals("NULL")) {
            User user = gson.fromJson(response, User.class);
            String hashed_username = HashUtils.hashString(username);
            String user_username = HashUtils.hashString(user.getUsername());
            assert user_username != null;
            assert hashed_username != null;
            return hashed_username.equals(user_username);
        } else {
            return false;
        }
    }

    public Boolean checkUsernamePassword(String username, String password, String url) {
        Gson gson = new Gson();
        String response = UserAPI.executeInBackground(() -> UserAPI.GetRequest(url));
        assert response != null;
        if (!response.equals("NULL")) {
            System.out.println(response);
            User user = gson.fromJson(response, User.class);
            if (user != null) {
                String hashed_username = HashUtils.hashString(username);
                String hashed_password = HashUtils.hashString(password);
                String user_username = HashUtils.hashString(user.getUsername());
                String user_password = HashUtils.hashString(user.getPassword());

                assert user_username != null;
                assert user_password != null;
                assert hashed_username != null;
                assert hashed_password != null;
                return hashed_username.equals(user_username) && hashed_password.equals(user_password);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
