package com.example.libretto_universitario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB_LIBRETTO.db";
    private static final String TABELLA_UTENTI = "UTENTI";
    private static final String TABELLA_ESAMI = "ESAMI";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABELLA_UTENTI+ "(USERNAME TEXT PRIMARY KEY, NAME TEXT, SURNAME TEXT, BIRTHDATE TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABELLA_ESAMI+ "(NOME_ESAME TEXT, NUMERO_CFU INTEGER, VOTO_OTTENUTO INTEGER, UTENTE TEXT, PRIMARY KEY(NOME_ESAME, UTENTE))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_UTENTI);
        db.execSQL("DROP TABLE IF EXISTS " + TABELLA_ESAMI);
        onCreate(db);
    }
    public boolean insertDataUtenti(String username, String name, String surname, String birthdate, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERNAME", username);
        values.put("NAME", name);
        values.put("SURNAME", surname);
        values.put("BIRTHDATE", birthdate);
        values.put("PASSWORD", password);

        long var = db.insert(TABELLA_UTENTI, null, values);
        if (var == -1){
            return false;
        } else {
            return true;
        }
    }
    public boolean insertDataEsami(String nome_esame, int numero_CFU, int voto_ottenuto, String utente){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOME_ESAME", nome_esame);
        values.put("NUMERO_CFU", numero_CFU);
        values.put("VOTO_OTTENUTO", voto_ottenuto);
        values.put("UTENTE", utente);

        long var = db.insert(TABELLA_ESAMI, null, values);
        if (var == -1){
            return false;
        } else {
            return true;
        }
    }
    public boolean getUtentiLogin(String username, String password){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM UTENTI WHERE USERNAME = ? AND PASSWORD = ?", new String[] {username, password});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public Cursor getEsamiByUser(String username){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery("SELECT * FROM ESAMI WHERE UTENTE=?", new String[]{username});
        return cursor;
    }
    public Boolean deleteExam(String nome, String utente){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ESAMI where NOME_ESAME = ? AND UTENTE = ?", new String[]{nome, utente});
        if (cursor.getCount() > 0){
            db.execSQL("DELETE FROM ESAMI WHERE NOME_ESAME = ? AND UTENTE = ?", new String[]{nome, utente});
            return true;
        } else{
            return false;
        }
    }
    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS UTENTI");
        db.execSQL("DROP TABLE IF EXISTS ESAMI");
        onCreate(db);
    }
}
