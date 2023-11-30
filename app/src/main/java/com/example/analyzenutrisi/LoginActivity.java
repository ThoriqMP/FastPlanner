package com.example.analyzenutrisi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView daftar;
    TextInputLayout usernameTextInputLayout, passwordTextInputLayout;
    TextInputEditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find views by their IDs
        login = findViewById(R.id.btn_next);
        usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        daftar = findViewById(R.id.daftar);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daftar = new Intent(LoginActivity.this, FormAkun.class);
                startActivity(daftar);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username and password
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                getDataUser(username,password);

            }
        });
    }

    private void loginUser(String enteredPassword, String storedHashedPassword, User user) {
        // Verify the entered password against the stored hashed password
        BCrypt.Result result = BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHashedPassword);

        if (result.verified) {
            // Di dalam LoginActivity.java, setelah login berhasil
            SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            // Password matches, proceed with login

            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            mainIntent.putExtra("user", (Serializable) user);
            startActivity(mainIntent);
            showLoadingIndicator(false);
        } else {
            // Password doesn't match or other error
            Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataUser(String username, String enteredPassword) {
        showLoadingIndicator(true);
        String url = "https://asia-south1.gcp.data.mongodb-api.com/app/application-2023-hckdc/endpoint/samakanPengguna?username=" + username;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // Assuming the server responds with a JSON object containing hashed password
                            if (response != null) {
                                // Extract the hashed password from the JSON response
                                String id = response.getString("_id");
                                String nama = response.getString("nama");
                                String username = response.getString("username");
                                String email = response.getString("email");
                                String jenis_kelamin = response.getString("jenis_kelamin");
                                String telepon = response.getString("telepon");
                                Integer status_plan = Integer.valueOf(response.getString("status_plan"));
                                Integer berat_badan = Integer.valueOf(response.getString("berat_badan"));
                                Integer tinggi_badan = Integer.valueOf(response.getString("tinggi_badan"));
                                Integer usia = Integer.valueOf(response.getString("usia"));
                                Integer status_pesanan = Integer.valueOf(response.getString("status_pemesanan"));
                                User user = new User(id,nama,username,email,jenis_kelamin,telepon,status_plan,berat_badan,tinggi_badan,usia,status_pesanan);
                                String storedHashedPassword = response.getString("password");

                                SharedPreferences preferences = getSharedPreferences("userModel", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                Gson gson = new Gson();
                                String userJson = gson.toJson(user);
                                editor.putString("user", userJson);
                                editor.apply();

                                // Now you have the hashed password, you can proceed to verify it
                                loginUser(enteredPassword, storedHashedPassword,user);
                            } else {
                                // Handle the case when the response is null or other error
                                Toast.makeText(LoginActivity.this, "Error retrieving data from the server.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            Toast.makeText(LoginActivity.this, "Error parsing JSON response.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoadingIndicator(false);
                        // Handle errors such as network issues
                        Log.e("VolleyError", "Error: " + error.getMessage());
                        Toast.makeText(LoginActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    private void showLoadingIndicator(boolean show) {
        if (show) {
            // Inflate the layout for the loading indicator
            View loadingIndicatorView = getLayoutInflater().inflate(R.layout.layout_loading_indicator, null);

            // Add the loading indicator to the root layout of the activity
            addContentView(loadingIndicatorView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            // Disable user interaction while the loading indicator is visible
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            // Remove the loading indicator view and restore user interaction
            ViewGroup rootLayout = findViewById(android.R.id.content);
            rootLayout.removeViewAt(rootLayout.getChildCount() - 1);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}