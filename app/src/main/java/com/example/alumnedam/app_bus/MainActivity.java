package com.example.alumnedam.app_bus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaramos los botones
        Button btnGuardar = (Button) findViewById(R.id.button);
        btnGuardar.setOnClickListener(this);

        Button btnFin = (Button) findViewById(R.id.button2);
        btnFin.setOnClickListener(this);

        //cuando se pulsa el boton fianlizar jornada
        btnFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TextView txtRes2 = (TextView) findViewById(R.id.textView2);
                //paramos el servicio
                Intent i = new Intent(getApplicationContext(),ServicioSegundoPlano.class);
                stopService(i);
                //declaramos la fecha de hoy
                int fecha = java.util.Calendar.getInstance().get(java.util.Calendar.DATE);
                int mes = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
                int any = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                int hora = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
                int minut = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
                int segon = java.util.Calendar.getInstance().get(java.util.Calendar.SECOND);

                //imprimimos
                txtRes2.setText("Final de jornada a las :"+fecha+"/"+mes+"/"+any+" "+hora+":"+minut+":"+segon);


            }
        });


    }

    @Override
    public void onClick(View view) {
        EditText editTextMatric = (EditText) findViewById(R.id.editText2);
        EditText editTextPass = (EditText) findViewById(R.id.editText);

        editTextMatric.getText();
        editTextPass.getText();
        SQLiteDatabase db;
        //Iniciamos la base de datos
        BDAutobus usdbh =
                new BDAutobus(this,"BDAutobus", null, 1);

        db = usdbh.getWritableDatabase();

        //hacemos una busqueda con los parametros que recojemos dekl usuario
        Cursor c = db.rawQuery("SELECT MATRICULA, PASSWORD FROM Login WHERE (MATRICULA = '" + editTextMatric.getText() + "') AND (PASSWORD = '" + editTextPass.getText() + "')", null);
        TextView txtResultado = (TextView) findViewById(R.id.txtResultado);


        TextView txtRes2 = (TextView) findViewById(R.id.textView2);
        //si da algun resultsdo la busqueda
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya mas registros

            do {
                //comparar lo que devuelve la BD con lo que introduce el usuario por pantalla
                String cod = c.getString(0);
                String horini = c.getString(1);

                if(cod.getBytes()!= null){

                    //declaramos la fecha de hoy para poder imprimir a la hora a ala que se hace la conexion
                    int fecha = java.util.Calendar.getInstance().get(java.util.Calendar.DATE);
                    int mes = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
                    int any = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                    int hora = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
                    int minut = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
                    int segon = java.util.Calendar.getInstance().get(java.util.Calendar.SECOND);

                    txtRes2.setText("Usuario Correcto inicio de jornada a las :"+fecha+"/"+mes+"/"+any+" "+hora+":"+minut+":"+segon);

                    //Hacemos un startService de la clase ServicioSegundoPlano
                    startService(new Intent(this, ServicioSegundoPlano.class));



                }
            } while (c.moveToNext());
            //si en la consulta no hay nada imprimimos usuario incorrecto
        }else{
            txtRes2.setText("usuario incorrecto");

        }
    }

}
