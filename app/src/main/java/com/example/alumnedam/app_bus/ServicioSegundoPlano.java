package com.example.alumnedam.app_bus;

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by ALUMNEDAM on 16/02/2017.
 */

public class ServicioSegundoPlano extends Service {

    public LocationManager locationManager;
    public LocationListener locationListener;
    int cod;
    //Cursor c = db.rawQuery("SELECT , PASSWORD FROM Login WHERE (MATRICULA = '" + editTextMatric.getText() + "') AND (PASSWORD = '" + editTextPass.getText() + "')", null);;
    public void onCreate() {

        Toast.makeText(this, "Servicio creado",
                Toast.LENGTH_SHORT).show();

    }

    private void muestraPosicion(Location location) {
        Toast.makeText(this, "Entro en muestra posicion",
                Toast.LENGTH_SHORT).show();
        if(location != null){
            Toast.makeText(this, "muestraposicion if",
                    Toast.LENGTH_SHORT).show();
            Log.e("Latitud: ", "" + location.getLatitude());
            Log.e("Longitude: ", "" + location.getLongitude());

            SQLiteDatabase db;
            int fecha = Calendar.getInstance().get(Calendar.DATE);
            int mes = Calendar.getInstance().get(Calendar.MONTH);
            int any = Calendar.getInstance().get(Calendar.YEAR);
            int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minut = Calendar.getInstance().get(Calendar.MINUTE);
            int segon = Calendar.getInstance().get(Calendar.SECOND);
            String matric = "A00001";
            cod ++;

            BDAutobus usdbh =
                    new BDAutobus(this,"BDAutobus", null, 1);

            db = usdbh.getWritableDatabase();

            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("ID_LOC", cod);
            nuevoRegistro.put("LATITUD", location.getLatitude());
            nuevoRegistro.put("LONGITUD", location.getLongitude());
            nuevoRegistro.put("FECHA", fecha+"/"+mes+"/"+any+" "+hora+":"+minut+":"+segon);
            nuevoRegistro.put("MATRICULA", matric);

            db.insert("Localizacion", null, nuevoRegistro);

            Toast.makeText(this, "Insertado!A",
                    Toast.LENGTH_SHORT).show();

        }
    }


    @Override

    public int onStartCommand(Intent intenc, int flags, int idArranque) {

        Toast.makeText(this,"Servicio arrancado "+ idArranque,
               Toast.LENGTH_SHORT).show();
        LocationManager myManager;
        MyLocationListener loc;
        locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Toast.makeText(this, "Antes del if",
                Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return flags;
        }
        Toast.makeText(this, "Despues del ifA3",
                Toast.LENGTH_SHORT).show();
        Location location =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Mostramos la última posición conocida
        Toast.makeText(this, "antes de metodo",
                Toast.LENGTH_SHORT).show();
        muestraPosicion(location);

        //Nos registramos para recibir actualizaciones de la posición
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                muestraPosicion(location);
            }
            public void onProviderDisabled(String provider){
                //lblEstado.setText("Provider OFF");
            }
            public void onProviderEnabled(String provider){
                //lblEstado.setText("Provider ON");
            }
            public void onStatusChanged(String provider, int status, Bundle extras){
                Log.i("LocAndroid", "Provider Status: " + status);
                //lblEstado.setText("Provider Status: " + status);
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 15000, 0, locationListener);


        //onLocationChanged(Location location);


        //reproductor.start();

       // return START_STICKY;

        return flags;
    }



    @Override

    public void onDestroy() {

        Toast.makeText(this,"Servicio detenido",
                Toast.LENGTH_SHORT).show();

       //reproductor.stop();

    }



    @Override

    public IBinder onBind(Intent intent) {
        Toast.makeText(this,"Servicio arrancado2 ",
                Toast.LENGTH_SHORT).show();
        return null;

    }
}
