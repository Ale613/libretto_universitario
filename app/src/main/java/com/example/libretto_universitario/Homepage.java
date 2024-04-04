package com.example.libretto_universitario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Homepage extends AppCompatActivity {

    protected CardView aggiungi_esame, esami_registrati, logoutbtn;
    protected TextView media_aritmetica, media_ponderata, voto_di_laurea;
    private DatabaseHelper myDB;
    protected Intent username_intent;
    protected String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        aggiungi_esame = findViewById(R.id.aggiungi_esame);
        esami_registrati = findViewById(R.id.esami_registrati);
        media_aritmetica = findViewById(R.id.media_aritmetica);
        media_ponderata = findViewById(R.id.media_ponderata);
        voto_di_laurea = findViewById(R.id.voto_di_laurea);
        logoutbtn = findViewById(R.id.logoutbtn);

        username_intent = getIntent();
        user = username_intent.getStringExtra("UTENTE");

        AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);

        builder.setMessage("Sei sicuro di volerti disconnettere?");
        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // effettuo logout
                // Creo l'intento di passare da Homepage a Login
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent); // Lancio l'intento
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        aggiungi_esame.setOnClickListener(v -> {
            // Creo l'intento di passare da Homepage a AggiungiEsami
            Intent intent = new Intent(getApplicationContext(), AggiungiEsami.class);
            intent.putExtra("UTENTE", user);
            startActivity(intent); // Lancio l'intento
        });

        esami_registrati.setOnClickListener(v -> {
            // Creo l'intento di passare da Homepage a EsamiRegistrati
            Intent intent = new Intent(getApplicationContext(), EsamiRegistrati.class);
            intent.putExtra("UTENTE", user);
            startActivity(intent); // Lancio l'intento
        });

        logoutbtn.setOnClickListener(v -> {
            dialog.show();
        });

        myDB = new DatabaseHelper(this);

        mediaAritmetica();
        mediaPonderata();
        votoDiLaurea();
    }
    private void mediaAritmetica(){

        Cursor cursor = myDB.getEsamiByUser(user);
        float nVoti = 0, totVoti = 0;
        float media;

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                totVoti += cursor.getInt(2);
                nVoti += 1;
            }
            media = totVoti / nVoti;
            media_aritmetica.setText(String.format("%.2f", media));
        } else {
            media_aritmetica.setText("0.00");
        }
    }
    public float mediaPonderata(){

        Cursor cursor = myDB.getEsamiByUser(user);
        float nCFU = 0;
        float media = 0;

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                media += (cursor.getInt(2) * cursor.getInt(1));
                nCFU += cursor.getInt(1);
            }
            media = media / nCFU;
            media_ponderata.setText(String.format("%.2f", media));
        } else {
            media_ponderata.setText("0.00");
        }

        return media;
    }
    private void votoDiLaurea(){

        float media = mediaPonderata(), voto;

        voto = (media * 110) / 30;

        voto_di_laurea.setText(String.format("%.2f", voto));
    }
}
