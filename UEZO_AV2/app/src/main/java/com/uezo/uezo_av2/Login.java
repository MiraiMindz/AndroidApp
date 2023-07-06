package com.uezo.uezo_av2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    UserAPI restAPI;
    APIUtils APIUtilities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        restAPI = new UserAPI();
        APIUtilities = new APIUtils();


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(Login.this, "Por favor insira todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserPassword = APIUtilities.checkUsernamePassword(user, pass, String.format("%s%s", "http://192.168.0.15:8080/users/", user));
                    if (checkUserPassword) {
                        Toast.makeText(Login.this, "Login Concluido", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Credenciais Invalidas", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}