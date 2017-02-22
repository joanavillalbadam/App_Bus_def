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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by ALUMNEDAM on 16/02/2017.
 */

public class ServicioSegundoPlano extends Service {

    public LocationManager locationManager;
    public LocationListener locationListener;
    int cod = 20;


    //Cursor c = db.rawQuery("SELECT , PASSWORD FROM Login WHERE (MATRICULA = '" + editTextMatric.getText() + "') AND (PASSWORD = '" + editTextPass.getText() + "')", null);;
    public void onCreate() {

        Toast.makeText(this, "Servicio creado",
                Toast.LENGTH_SHORT).show();

    }

    private void muestraPosicion(Location location) {
        //pido la latitud y la longitud
            Log.e("Latitud: ", "" + location.getLatitude());
            Log.e("Longitude: ", "" + location.getLongitude());

            SQLiteDatabase db;
        //pedimos la fecha
            int fecha = Calendar.getInstance().get(Calendar.DATE);
            int mes = Calendar.getInstance().get(Calendar.MONTH);
            int any = Calendar.getInstance().get(Calendar.YEAR);
            int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minut = Calendar.getInstance().get(Calendar.MINUTE);
            int segon = Calendar.getInstance().get(Calendar.SECOND);
            //introducimos la matricula
            String matric = "A00001";
            //y el codigo que incrementaremos
            cod ++;

        //Insertamos en la bd interna
            BDAutobus usdbh =
                    new BDAutobus(this,"BDAutobus", null, 1);

            db = usdbh.getWritableDatabase();

            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("idloc", cod);
            nuevoRegistro.put("latitud", location.getLatitude());
            nuevoRegistro.put("longitud", location.getLongitude());
            nuevoRegistro.put("fecha", fecha+"/"+mes+"/"+any+" "+hora+":"+minut+":"+segon);
            nuevoRegistro.put("matricula", matric);

            db.insert("Localizacion", null, nuevoRegistro);

            Toast.makeText(this, "Insertado en interna",
                    Toast.LENGTH_SHORT).show();

        }



    @Override

    public int onStartCommand(Intent intenc, int flags, int idArranque) {

        Toast.makeText(this,"Servicio arrancado "+ idArranque,
               Toast.LENGTH_SHORT).show();
        LocationManager myManager;
        //declaramos en location manager
        locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

        Location location =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Mostramos la última posición conocida
        //iniciamos el metodo muestraPosicion para hacer el insert en la interna
   //     muestraPosicion(location);
        //iniciamos el metodo conexionExterna para hacer el insert en la externa
    //    ConexionExterna con = new ConexionExterna();
     //   con.execute();

        //Nos registramos para recibir actualizaciones de la posición
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //cada vez que se cambie de posicion se iniciaran muestraposicion y cinexion externa
                muestraPosicion(location);
                ConexionExterna con = new ConexionExterna();
                con.execute();
            }
            public void onProviderDisabled(String provider){

            }
            public void onProviderEnabled(String provider){

            }
            public void onStatusChanged(String provider, int status, Bundle extras){
                Log.i("LocAndroid", "Provider Status: " + status);

            }
        };

        //esperara un tiempo antes de volver a insertas
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 15000, 0, locationListener);



        return flags;
    }




    @Override

    public void onDestroy() {
    //cuando entramos aqui paramos el servicio en segundo plano
        Toast.makeText(this,"Servicio detenido",
                Toast.LENGTH_SHORT).show();
        super.onDestroy();
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }

    }



    @Override

    public IBinder onBind(Intent intent) {
        Toast.makeText(this,"Servicio arrancado2 ",
                Toast.LENGTH_SHORT).show();
        return null;

    }


    private class ConexionExterna extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... voids) {

            //ponemos un boolean para comprobar si se insertan los datos

            boolean resul = true;
            //Inicializamos HttpClient
            HttpClient httpClient = new DefaultHttpClient();
            //Creamos un httpPost con el http://"mi web service rest"
            HttpPost post = new HttpPost("http://192.168.120.74:8080/AutobusNetBeans/webresources/generic/insertLocalizacion");
            post.setHeader("content-type", "application/json");
            //recojemos las variables
          int fecha = java.util.Calendar.getInstance().get(java.util.Calendar.DATE);
            int mes = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
            int any = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            int hora = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
            int minut = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
            int segon = java.util.Calendar.getInstance().get(Calendar.SECOND);

            Location location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            int x = cod++;
            double la =location.getLatitude() ;
            double lo =location.getLongitude();
            String fe = fecha+"/"+mes+"/"+any+" "+hora+":"+minut+":"+segon+"";
            String matr = "A0001";

            try {

                //Creamos el objeto JSON
                JSONObject objson = new JSONObject();
                //ponemos las variable en el objeto JSON

                objson.put("idloc", x);
                objson.put("latitud",la);
                objson.put("longitud",lo);
                objson.put("fecha", fe);
                objson.put("matricula", matr);

                //Pasamos el objeto JSON a String

                     StringEntity entity = new StringEntity(objson.toString());
                     post.setEntity(entity);

                     //Ejecutamos el JSON
                    HttpResponse resp = httpClient.execute(post);
                //nos devolvera true o false que nos ara saber si se a insertado el insert into o no
                    String respStr = EntityUtils.toString(resp.getEntity());
                   // Si devuelve true sera igual true
                    if (respStr.equals("true")) {
                        resul = true;
                    } else {

                        resul=false;
                    }

            } catch (Exception e) {

                Log.e("ServicioRest", "Error!", e);
                resul = false;
            }
            return resul;
        }



        protected void onPostExecute(Boolean result) {
         //entrara aqui despues de el ... si es ture nos dira Insertado
            if (result) {
                Toast.makeText(ServicioSegundoPlano.this, "Insertado", Toast.LENGTH_SHORT).show();

            } else {
            //sino no insertado
                Toast.makeText(ServicioSegundoPlano.this, "No Insertado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
