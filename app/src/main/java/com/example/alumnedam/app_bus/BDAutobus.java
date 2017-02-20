package com.example.alumnedam.app_bus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ALUMNEDAM on 16/02/2017.
 */

public class BDAutobus extends SQLiteOpenHelper {
    //hacemos un string con la sentencia sql para crear las tablas que necesitamos
    String localiz = "CREATE TABLE Localizacion (ID_LOC INTEGER, LATITUD INTEGER , LONGITUD INTEGER, FECHA TEXT, MATRICULA TEXT)";
    String login = "CREATE TABLE Login (MATRICULA TEXT, ID_LOG INTEGER , PASSWORD TEXT)";


    public BDAutobus(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    public void onCreate(SQLiteDatabase db){
        //exeutamos las sentencias sql
        db.execSQL(localiz);
        db.execSQL(login);

        //hacemos los registros de los autobuses para que puedan loguearse
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("MATRICULA", "A00001");
        nuevoRegistro.put("ID_LOG", 1);
        nuevoRegistro.put("PASSWORD", "1234");
        db.insert("Login", null, nuevoRegistro);

        nuevoRegistro.put("MATRICULA", "A00002");
        nuevoRegistro.put("ID_LOG", 2);
        nuevoRegistro.put("PASSWORD", "1234");
        db.insert("Login", null, nuevoRegistro);
    }

    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS Localizacion");
        db.execSQL("DROP TABLE IF EXISTS Login");
        db.execSQL(localiz);
        db.execSQL(login);

    }



}
