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
import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserAPI {
    private UserAPIService userAPIService;

    public UserAPI(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userAPIService = retrofit.create(UserAPIService.class);
    }
    public void getUsers(final Response.Listener<JsonArray> successListener, final Response.ErrorListener errorListener) {
        Call<JsonArray> call = userAPIService.getUsers();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    successListener.onResponse(response.body());
                } else {
                    errorListener.onErrorResponse(new VolleyError("Request failed"));
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                errorListener.onErrorResponse(new VolleyError(t));
            }
        });
    }

    public void getUserByUsername(String username, final Response.Listener<JsonObject> successListener, final Response.ErrorListener errorListener) {
        Call<JsonObject> call = userAPIService.getUserByUsername(username);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    successListener.onResponse(response.body());
                } else {
                    errorListener.onErrorResponse(new VolleyError("Request failed"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call(Call<JsonObject> call, Throwable t) {
                errorListener.onErrorResponse(new VolleyError(t));
            }
        });
    }

    public boolean addUser(String username, String password) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("username", username);
        requestBody.addProperty("password", password);

        Call<Void> call = userAPIService.addUser(requestBody);
        try {
            Response<Void> response = call.execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}