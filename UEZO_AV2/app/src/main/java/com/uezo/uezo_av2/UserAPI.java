// package com.uezo.uezo_av2;
// import android.content.Context;

// import com.android.volley.Request;
// import com.android.volley.RequestQueue;
// import com.android.volley.Response;
// import com.android.volley.VolleyError;
// import com.android.volley.toolbox.JsonArrayRequest;
// import com.android.volley.toolbox.JsonObjectRequest;
// import com.android.volley.toolbox.Volley;

// import org.json.JSONArray;
// import org.json.JSONException;
// import org.json.JSONObject;

// public class UserAPI {
//     private RequestQueue requestQueue;

//     public UserAPI(Context context) {
//         // Initialize Volley request queue
//         requestQueue = Volley.newRequestQueue(context.getApplicationContext());
//     }

//     public void getUsers(Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
//         String url = "http://localhost:8080/users";

//         JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
//                 successListener, errorListener);

//         // Add the request to the Volley queue
//         requestQueue.add(request);
//     }

//     public void getUserByUsername(String username, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
//         String url = "http://localhost:8080/users/" + username;

//         JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                 successListener, errorListener);

//         // Add the request to the Volley queue
//         requestQueue.add(request);
//     }


//     public boolean addUser(String username, String password) {
//         String url = "http://localhost:8080/users";

//         JSONObject requestBody = new JSONObject();
//         try {
//             requestBody.put("username", username);
//             requestBody.put("password", password);
//         } catch (JSONException e) {
//             e.printStackTrace();
//             return false; // Return false if there is an exception
//         }

//         final boolean[] isSuccess = new boolean[1]; // Using an array to store the boolean value

//         JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
//                 new Response.Listener<JSONObject>() {
//                     @Override
//                     public void onResponse(JSONObject response) {
//                         // Success callback
//                         isSuccess[0] = true;
//                     }
//                 },
//                 new Response.ErrorListener() {
//                     @Override
//                     public void onErrorResponse(VolleyError error) {
//                         // Error callback
//                         isSuccess[0] = false;
//                     }
//                 });

//         // Add the request to the Volley queue
//         requestQueue.add(request);

//         return isSuccess[0]; // Return the boolean value indicating success or failure
//     }

//     public boolean checkUsername(String username) {
//         final boolean[] exists = {false}; // Using an array to store the boolean value

//         getUserByUsername(username,
//                 new Response.Listener<JSONObject>() {
//                     @Override
//                     public void onResponse(JSONObject response) {
//                         // Username exists if a user object is returned
//                         exists[0] = (response != null);
//                     }
//                 },
//                 new Response.ErrorListener() {
//                     @Override
//                     public void onErrorResponse(VolleyError error) {
//                         // Handle the error
//                     }
//                 }
//         );

//         return exists[0]; // Return the boolean value indicating if the username exists
//     }

//     public boolean checkUsernamePassword(String username, String password) {
//         final boolean[] isValid = {false}; // Using an array to store the boolean value

//         getUserByUsername(username,
//                 new Response.Listener<JSONObject>() {
//                     @Override
//                     public void onResponse(JSONObject response) {
//                         // Check if user exists and the password matches
//                         if (response != null) {
//                             try {
//                                 String storedPassword = response.getString("password");
//                                 isValid[0] = password.equals(storedPassword);
//                             } catch (JSONException e) {
//                                 e.printStackTrace();
//                             }
//                         }
//                     }
//                 },
//                 new Response.ErrorListener() {
//                     @Override
//                     public void onErrorResponse(VolleyError error) {
//                         // Handle the error
//                     }
//                 }
//         );

//         return isValid[0]; // Return the boolean value indicating if the username and password are valid
//     }
// }

package com.uezo.uezo_av2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserAPI {
    interface NetworkRequestTask {
        String performRequest() throws IOException;
    }


    public static String executeInBackground(NetworkRequestTask task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(task::performRequest);

        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return null;
    }
    public static String GetRequest(String urlAddress) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlAddress);
            System.out.println(urlAddress);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            System.out.println(connection);
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    System.out.println(line);
                }
                reader.close();
            } else {
                System.out.println("HTTP request failed with response code: " + responseCode);
                System.out.println(responseCode);
                response.append("NULL");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public static String PostRequest(String endpointUrl, String jsonPayload) {
        String success = "false";

        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                success = "true";
            } else {
                System.out.println("HTTP request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }
}