package com.example.desarrollo.Datos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.desarrollo.Entidades.HistorialConsumo;
import com.example.desarrollo.Entidades.Nino;
import com.example.desarrollo.ExportJSON.Model.ModelFrutas;
import com.example.desarrollo.ExportJSON.Reader.ReaderFrutas;
import com.example.desarrollo.Precentacion.Home.HijoRegistroActivity;
import com.example.desarrollo.Ultilidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class NinoDao {

    private static SQLiteDatabase database;
    private static HijoRegistroActivity gustos;
    private static final String TAG = "NinoDao";

    public static boolean addNino(String TAG, Context context, int idUsuario, String nombre, String apPaterno, String apMaterno, int edad, Double peso, Double estatura, Double medida, Double lineabultra, Double lineabv, Double leneabf, int totfich, Double esfuerzoultra, Double esfuerzof, Double esfuerzov) {

        try {
            ConexionSQLHelper connection = new ConexionSQLHelper(context);
            database = null;
            database = connection.getWritableDatabase();


            String inset = "INSERT INTO " + Utilidades.TABLA_Nino + "( " +
                    Utilidades.CAMPO_idUsuarioN + ", " +
                    Utilidades.CAMPO_NombreN + ", " +
                    Utilidades.CAMPO_ApellidoPaternoN + "," +
                    Utilidades.CAMPO_ApellidoMaternoN + ", " +
                    Utilidades.CAMPO_Edad + ", " +
                    Utilidades.CAMPO_Peso + ", " +
                    Utilidades.CAMPO_Estatura + ", " +
                    Utilidades.CAMPO_Medida + ", " +
                    Utilidades.CAMPO_LineaBaseUltraprocesado + ", " +
                    Utilidades.CAMPO_LineaBaseVerdura + ", " +
                    Utilidades.CAMPO_LIneaBaseFruta + ", " +
                    Utilidades.CAMPO_TotalFichas + ", " +
                    Utilidades.CAMPO_EsfuerzoUltraprocesado + ", " +
                    Utilidades.CAMPO_EsfuerzoFruta + ", " +
                    Utilidades.CAMPO_EsfuerzoVerdura + ") " +
                    "VALUES ( " +
                    idUsuario + ", '" +
                    nombre + "', '" +
                    apPaterno + "', '" +
                    apMaterno + "', " +
                    edad + ", " +
                    peso + ", " +
                    estatura + ", " +
                    medida + ", " +
                    lineabultra + ", " +
                    lineabv + ", " +
                    leneabf + ", " +
                    totfich + ", " +
                    esfuerzoultra + ", " +
                    esfuerzof + ", " +
                    esfuerzov + ")";


            database.execSQL(inset);

            SharedPreferences preferenc = context.getSharedPreferences("Calculo", context.MODE_PRIVATE);
            int inpre = preferenc.getInt("instalacion", 0);
            if(inpre ==0){
                TimeZone timezone = TimeZone.getDefault();
                Calendar calendar = new GregorianCalendar(timezone);
                int dias = calendar.get(Calendar.DAY_OF_WEEK);
                String fecha=Calculos.getFecha();
                SharedPreferences.Editor editor = preferenc.edit();
                editor.putInt("instalacion", 1);
                editor.putInt("dia", dias);
                editor.putInt("llave", 0);
                editor.putInt("queEs", 0);
                editor.putInt("LB", 0);
                editor.putInt("pase", 1);
                editor.putInt("seguro", 0);
                editor.putInt("llaveLBF1", 0);
                editor.putInt("llaveLBF2", 0);
                editor.putInt("llaveLBV1", 0);
                editor.putInt("llaveLBV2", 0);
                editor.putString("FechaInicio",fecha);
                editor.putString("FechaFin","");
                editor.putString("valLBF1","");
                editor.putString("valLBF2","");
                editor.putString("esfuerzoF1","");
                editor.putString("esfuerzoF2","");
                editor.putString("valLBV1","");
                editor.putString("valLBV2","");
                editor.putString("esfuerzoV1","");
                editor.putString("esfuerzoV2","");
                editor.putInt("llaveESF1", 0);
                editor.putInt("llaveESF2", 0);
                editor.putInt("llaveESV1", 0);
                editor.putInt("llaveESV 2", 0);
                editor.commit();
            }

            String getId = "SELECT last_insert_rowid();";
            Cursor c = database.rawQuery(getId, null);
            c.moveToFirst();

            gustos.idNino = c.getInt(0);
            c.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error " + e);
            return false;
        } finally {
            database.close();
        }
    }

    public static int countNino(String TAG, Context context) {
        int count = 0;
        try {
            ConexionSQLHelper connection = new ConexionSQLHelper(context);
            database = null;
            database = connection.getWritableDatabase();

            Cursor cursor = database.rawQuery("Select COUNT(" + Utilidades.CAMPO_idNino + ") AS 'id' FROM " + Utilidades.TABLA_Nino, null);

            while (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("id"));
            }

            Log.v(TAG, "Count: " + count);

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "ERROR: " + e);
        } finally {
            database.close();
        }

        return count;
    }

    public static ArrayList countFichasNino(String TAG, Context context) {
        //int totalFichas = 0;
        ArrayList datosNino = new ArrayList();
        try {
            ConexionSQLHelper conection = new ConexionSQLHelper(context);
            database = null;
            database = conection.getReadableDatabase();

            Cursor cursor = database.rawQuery("SELECT " + Utilidades.CAMPO_TotalFichas + ", " +
                    Utilidades.CAMPO_NombreN + " FROM " + Utilidades.TABLA_Nino, null);
            while (cursor.moveToNext()) {
                datosNino.add(cursor.getInt(0));
                datosNino.add(cursor.getString(1));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "ERROR " + e);
        } finally {
            database.close();
        }

        return datosNino;
    }

    public static void consultarNino(String TAG, Context context, ArrayList ninoList) {
        try {
            ConexionSQLHelper conection = new ConexionSQLHelper(context);
            Nino nino = null;
            database = null;
            database = conection.getReadableDatabase();

            Cursor cursor = database.rawQuery("SELECT " + Utilidades.CAMPO_idNino + ", " + Utilidades.CAMPO_NombreN + " FROM " +
                    Utilidades.TABLA_Nino, null);

            while (cursor.moveToNext()) {
                nino = new Nino();
                nino.setIdNino(cursor.getInt(0));
                nino.setNombre(cursor.getString(1));

                ninoList.add(nino);
            }

            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "Error " + e);
        } finally {
            database.close();
        }
    }

    public static void consultarItemsHistorialDetalleConsumo(String TAG, Context context, int idNino, ArrayList consumoList) {
        try {
            ConexionSQLHelper conection = new ConexionSQLHelper(context);
            database = null;
            database = conection.getReadableDatabase();
            Calculos calculos = new Calculos();
            HistorialConsumo historialConsumo;
            ModelFrutas modelFrutas = new ModelFrutas();
            ArrayList<ReaderFrutas> frutasList;

            String fecha = calculos.getFecha();

            Cursor cursor = database.rawQuery("SELECT " + Utilidades.TABLA_DetalleRegistro + "." + Utilidades.CAMPO_IdAlimento + ", " + Utilidades.TABLA_DetalleRegistro + "." + Utilidades.CAMPO_Cantidad + ", " + Utilidades.TABLA_DetalleRegistro + "." + Utilidades.CAMPO_Tipo + ", " + Utilidades.TABLA_DetalleRegistro + "." + Utilidades.CAMPO_HoraRegistro + ", " + Utilidades.TABLA_DetalleRegistro + "." + Utilidades.CAMPO_UnidadMedida +
                    " FROM " + Utilidades.TABLA_DetalleRegistro +
                    " INNER JOIN " + Utilidades.TABLA_Registro + " ON " + Utilidades.TABLA_Registro + "." + Utilidades.CAMPO_idRegistro + " = " + Utilidades.TABLA_DetalleRegistro + "." + Utilidades.CAMPO_IdDetalleRegistro +
                    " WHERE " + Utilidades.TABLA_DetalleRegistro + "." + Utilidades.CAMPO_idNino + " = " + idNino +
                    " AND " + Utilidades.TABLA_Registro + "." + Utilidades.CAMPO_FechaRegistro + " = '" + fecha + "' ", null);

            while (cursor.moveToNext()) {
                historialConsumo = new HistorialConsumo();
                frutasList = new ArrayList();
                modelFrutas.addItemsFromJSONHistorial(frutasList, TAG, cursor.getString(cursor.getColumnIndex("Tipo")), context);


                for (int i = 0; i < frutasList.size(); i++) {
                    if (frutasList.get(i).getId().equals(String.valueOf(cursor.getInt(cursor.getColumnIndex("idalimento"))))) {
                        historialConsumo.setNombreAlimentos(frutasList.get(i).getNombre());
                        historialConsumo.setBackgroundAlimento(frutasList.get(i).getBackground());
                        historialConsumo.setImgUrl(frutasList.get(i).getImgUrl());
                    }
                }

                historialConsumo.setCantidadAlimento(cursor.getDouble(cursor.getColumnIndex("cad")));
                historialConsumo.setHora(cursor.getString(cursor.getColumnIndex("hora")));
                historialConsumo.setUnidadMedida(cursor.getDouble(cursor.getColumnIndex("umedr")));

                consumoList.add(historialConsumo);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error " + e);
        } finally {
            database.close();
        }
    }

}
