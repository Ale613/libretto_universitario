package com.example.libretto_universitario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    protected EditText username, password;
    protected TextView link_registrazione;
    protected Button loginbtn;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginbtn = findViewById(R.id.loginbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        link_registrazione = findViewById(R.id.link_registrazione2);

        myDB = new DatabaseHelper(this);
        checkLogin();

        link_registrazione.setOnClickListener(v -> {
            // Creo l'intento di passare da Login a Registrazione
            Intent intent = new Intent(getApplicationContext(), Registrazione.class);
            startActivity(intent); // Lancio l'intento
            finish();
        });
    }

    public void checkLogin(){
        loginbtn.setOnClickListener(v -> {
            if(checkInput()){
                //controllo se gli input corrispondono ai valori nel db
                boolean isInserted = myDB.getUtentiLogin(username.getText().toString(),
                        password.getText().toString()
                );
                if(isInserted){
                    Toast.makeText(Login.this, "Login avvenuto correttamente", Toast.LENGTH_SHORT).show();
                    // Creo l'intento di passare da Login a Homepage
                    Intent intent = new Intent(getApplicationContext(), Homepage.class);
                    intent.putExtra("UTENTE", username.getText().toString());
                    startActivity(intent); // Lancio l'intento
                    finish();
                } else {
                    username.setError("Username o password errati! Riprova");
                    password.setError("Username o password errati! Riprova");
                }
            }
        });
    }

    protected boolean checkInput() {
        int errors = 0;

        if (username.getText() == null || username.getText().length() == 0) {
            errors++;
            username.setError("Inserire l'username");
        } else{
            username.setError(null);
        }

        if (password.getText() == null || password.getText().length() == 0) {
            errors++;
            password.setError("Inserire la password");
        } else
            password.setError(null);

        return errors == 0; // True se non vi sono errori, altrimenti false
    }
}
