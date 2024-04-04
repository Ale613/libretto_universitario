package com.example.libretto_universitario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class EsamiRegistrati extends AppCompatActivity {

    protected DatabaseHelper myDB;
    protected LinearLayout contenitore;
    protected ImageView torna_home;
    protected Intent username_intent;
    protected String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esami_registrati);

        contenitore = findViewById(R.id.contenitore);
        torna_home = findViewById(R.id.torna_home);

        username_intent = getIntent();
        user = username_intent.getStringExtra("UTENTE");

        torna_home.setOnClickListener(v -> {
            // Creo l'intento di passare da EsamiRegistrati a Homepage
            Intent intent = new Intent(getApplicationContext(), Homepage.class);
            intent.putExtra("UTENTE", user);
            startActivity(intent); // Lancio l'intento
            finish();
        });

        myDB = new DatabaseHelper(this);

        //Visualizzo tutti gli esami
        displayData();
    }

    private void displayData(){
        Cursor cursor = myDB.getEsamiByUser(user);
        if(cursor.getCount() < 0){
            Toast.makeText(EsamiRegistrati.this, "Nessun esame inserito!", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                View view = getLayoutInflater().inflate(R.layout.item_esame, null);
                TextView nome = view.findViewById(R.id.nome_esame);
                TextView numCFU = view.findViewById(R.id.numero_cfu);
                TextView voto = view.findViewById(R.id.voto_esame);
                CardView eliminabtn = view.findViewById(R.id.rimuovi_esame);

                nome.setText(cursor.getString(0));
                numCFU.setText(Integer.toString(cursor.getInt(1))+" CFU");
                voto.setText(Integer.toString(cursor.getInt(2)));
                contenitore.addView(view);

                AlertDialog.Builder builder = new AlertDialog.Builder(EsamiRegistrati.this);

                builder.setMessage("Vuoi eliminare questo esame?");
                builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(myDB.deleteExam(nome.getText().toString(), user)){
                            Toast.makeText(EsamiRegistrati.this, "Esame rimosso!", Toast.LENGTH_SHORT).show();
                            contenitore.removeView(view);
                        } else {
                            Toast.makeText(EsamiRegistrati.this, "Errore, riprova!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                eliminabtn.setOnClickListener(v -> {
                    dialog.show();
                });

            }
        }
    }
}
