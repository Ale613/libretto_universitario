package com.example.libretto_universitario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AggiungiEsami extends AppCompatActivity {

    protected EditText nome_esame, numero_CFU, voto_ottenuto;
    protected TextView torna_home;
    protected Button aggiungibtn;
    protected DatabaseHelper myDB;
    protected Intent username_intent;
    protected String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_esami);

        nome_esame = findViewById(R.id.nome_esame);
        numero_CFU = findViewById(R.id.numero_cfu);
        voto_ottenuto = findViewById(R.id.voto_ottenuto);
        torna_home = findViewById(R.id.torna_home);
        aggiungibtn = findViewById(R.id.aggiungibtn);

        username_intent = getIntent();
        user = username_intent.getStringExtra("UTENTE");

        torna_home.setOnClickListener(v -> {
            // Creo l'intento di passare da AggiungiEsami a Homepage
            Intent intent = new Intent(getApplicationContext(), Homepage.class);
            intent.putExtra("UTENTE", user);
            startActivity(intent); // Lancio l'intento
            finish();
        });

        myDB = new DatabaseHelper(this);

        //Aggiungo esami al database
        addDataEsami();
    }
    public void addDataEsami(){
        aggiungibtn.setOnClickListener(v -> {
            // Se gli input sono validi aggiunge i dati del database
            if(checkInput()){
                boolean isInserted = myDB.insertDataEsami(nome_esame.getText().toString(),
                        Integer.parseInt(numero_CFU.getText().toString()),
                        Integer.parseInt(voto_ottenuto.getText().toString()),
                        user
                );
                if (isInserted){
                    Toast.makeText(AggiungiEsami.this, "Esame aggiunto correttamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AggiungiEsami.this, "Hai già inserito questo esame!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    protected boolean checkInput() {
        int errors = 0;

        if (nome_esame.getText() == null || nome_esame.getText().length() == 0) {
            errors++;
            nome_esame.setError("Inserire il nome dell'esame");
        } else
            nome_esame.setError(null);

        if (numero_CFU.getText().length() == 0 || (Integer.parseInt(numero_CFU.getText().toString())) < 3
            || (Integer.parseInt(numero_CFU.getText().toString()) > 15)) {
            errors++;
            numero_CFU.setError("Inserire il numero di CFU (da 3 a 15)");
        } else
            numero_CFU.setError(null);

        if (voto_ottenuto.getText().length() == 0 || (Integer.parseInt(voto_ottenuto.getText().toString())) < 18
            || (Integer.parseInt(voto_ottenuto.getText().toString())) > 30) {
            errors++;
            voto_ottenuto.setError("Inserire il voto (da 18 a 30)");
        } else
            voto_ottenuto.setError(null);

        return errors == 0; // True se non vi sono errori, altrimenti false
    }
}
