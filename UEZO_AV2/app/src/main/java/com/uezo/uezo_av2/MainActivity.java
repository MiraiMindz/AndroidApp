package com.uezo.uezo_av2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signin, signup;
    UserAPI restAPI;
    APIUtils APIUtilities;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signin = (Button) findViewById(R.id.btnsignin);
        signup = (Button) findViewById(R.id.btnsignup);
        restAPI = new UserAPI();
        gson = new Gson();
        APIUtilities = new APIUtils();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = HashUtils.hashString(username.getText().toString());
                String pass = HashUtils.hashString(password.getText().toString());
                String repass = HashUtils.hashString(repassword.getText().toString());
                assert user != null;
                assert pass != null;
                assert repass != null;

                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(MainActivity.this, "Por favor insira todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = APIUtilities.checkUsername(user, String.format("%s/%s", Globals.ServerAddress, user));
                        if (!checkuser) {
                            User newUser = new User();
                            newUser.setPassword(pass);
                            newUser.setUsername(user);
                            String userJson = gson.toJson(newUser);
                            String insert = UserAPI.executeInBackground(() -> UserAPI.PostRequest(Globals.ServerAddress, userJson));
                            assert insert != null;
                            if (!insert.equals("true")) {
                                Toast.makeText(MainActivity.this, "Cadastro concluido", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Houve um erro no cadastro", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Usuário já cadastrado, favor fazer login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }
}