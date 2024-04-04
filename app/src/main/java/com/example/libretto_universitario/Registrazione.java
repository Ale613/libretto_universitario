package com.example.libretto_universitario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Registrazione extends AppCompatActivity {

    protected EditText username, name, surname, password, birthdate;
    protected TextView link_accedi;
    protected Button registerbtn;
    private DatabaseHelper myDB;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);
        initDatePicker();
        birthdate = findViewById(R.id.birthdate);
        birthdate.setFocusable(false);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        password = findViewById(R.id.password);
        registerbtn = findViewById(R.id.registerbtn);
        link_accedi = findViewById(R.id.link_accedi2);

        link_accedi.setOnClickListener(v -> {
            // Creo l'intento di passare da Login a Registrazione
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent); // Lancio l'intento
            finish();
        });

        myDB = new DatabaseHelper(this);

        // Se gli input sono validi aggiunge i dati al database
        addDataUtenti();
    }

    public void addDataUtenti(){
        registerbtn.setOnClickListener(v -> {
            // Se gli input sono validi aggiunge i dati del database
            if(checkInput()){
                boolean isInserted = myDB.insertDataUtenti(username.getText().toString(),
                        name.getText().toString(),
                        surname.getText().toString(),
                        birthdate.getText().toString(),
                        password.getText().toString()
                );
                if (isInserted){
                    Toast.makeText(Registrazione.this, "Registrazione avvenuta correttamente", Toast.LENGTH_SHORT).show();
                    // Creo l'intento di passare da Registrazione a Homepage
                    Intent intent = new Intent(Registrazione.this, Homepage.class);
                    intent.putExtra("UTENTE", username.getText().toString());
                    startActivity(intent); // Lancio l'intento
                } else {
                    Toast.makeText(Registrazione.this, "Scegli un altro nome utente, questo non è disponibile!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    protected boolean checkInput() {
        int errors = 0;

        if (username.getText() == null || username.getText().length() == 0) {
            errors++;
            username.setError("Inserire l'username");
        } else
            username.setError(null);

        if (name.getText() == null || name.getText().length() == 0) {
            errors++;
            name.setError("Inserire il nome");
        } else
            name.setError(null);

        if (surname.getText() == null || surname.getText().length() == 0) {
            errors++;
            surname.setError("Inserire il cognome");
        } else
            surname.setError(null);

        if (password.getText() == null || password.getText().length() == 0) {
            errors++;
            password.setError("Inserire la password");
        } else
            password.setError(null);

        if (birthdate.getText() == null || birthdate.getText().length() == 0) {
            errors++;
            birthdate.setError("Inserire la data di nascita");
        } else
            birthdate.setError(null);

        return errors == 0; // True se non vi sono errori, altrimenti false
    }
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                birthdate.setText(date);
                birthdate.setError(null);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}