package com.example.desarrollo.Precentacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.text.DateFormat;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Deque;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.widget.Toast;

import com.example.desarrollo.Datos.Calculos;
import com.example.desarrollo.Datos.ConexionSQLHelper;
import com.example.desarrollo.Datos.NinoDao;
import com.example.desarrollo.Datos.TutorDao;
import com.example.desarrollo.Entidades.Nino;
import com.example.desarrollo.Precentacion.Home.HomeFragment;

import com.example.desarrollo.Precentacion.Motivadores.MotivadoresFragment;
import com.example.desarrollo.Precentacion.Perfil.PerfilFragment;
import com.example.desarrollo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private int contador = 0, estado = 1;
    private int alarmID = 1;
    private String inicio = "";

    private FrameLayout mMainFrame;
    private String currentTag = "home";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // NinoDao.addNino("fff",this,1,"mig","jac","jac",1,1.1,1.1,1.1,1.1,1.1,1.1,3,1.1,1.1,1.1);
       // NinoDao.addNino("fff",this,2,"cyn","mej","car",1,1.1,1.1,1.1,1.1,1.1,1.1,3,1.1,1.1,1.1);

        String fecha=Calculos.getFecha();
        Calculos.registrarDetalleReg("fff",this,1,1,1.1,1.1,"rrr",fecha,"Fruta");
        Calculos.registrarDetalleReg("fff",this,1,1,1.1,1.1,"rrr",fecha,"Fruta");
        Calculos.registrarDetalleReg("fff",this,1,1,1.1,1.1,"rrr",fecha,"Verdura");
        Calculos.registrarDetalleReg("fff",this,1,1,1.1,1.1,"rrr",fecha,"Verdura");
        Calculos.registrarDetalleReg("fff",this,1,1,1.1,1.1,"rrr",fecha,"ULtraProcesado");
        Calculos.registrarDetalleReg("fff",this,1,1,1.1,1.1,"rrr",fecha,"ULtraProcesado");


        Calculos.registrarDetalleReg("fff",this,2,1,1.1,1.1,"rrr",fecha,"Fruta");
        Calculos.registrarDetalleReg("fff",this,2,1,1.1,1.1,"rrr",fecha,"Fruta");
        Calculos.registrarDetalleReg("fff",this,2,1,1.1,1.1,"rrr",fecha,"Verdura");
        Calculos.registrarDetalleReg("fff",this,2,1,1.1,1.1,"rrr",fecha,"Verdura");
        Calculos.registrarDetalleReg("fff",this,2,1,1.1,1.1,"rrr",fecha,"ULtraProcesado");
        Calculos.registrarDetalleReg("fff",this,2,1,1.1,1.1,"rrr",fecha,"ULtraProcesado");


        SharedPreferences preferenc = getSharedPreferences("Calculo", MODE_PRIVATE);
        int llave1 = preferenc.getInt("llave1", 0);
        int llave2 = preferenc.getInt("llave2", 0);
        int llave3 = preferenc.getInt("llave3", 0);

        System.out.println(llave1+" , "+llave2+" , "+llave3);
        if(llave1 ==0 || llave2==0 || llave3==0) {
            Calculos.generaLBF(this, 1);
            Calculos.generaLBF(this, 2);
            Calculos.generaLBV(this, 1);
            Calculos.generaLBV(this, 2);
            Calculos.generaLBUlPro(this, 1);
            Calculos.generaLBUlPro(this, 2);
        }else {
            Calculos.EsfuerzoF(this, 1);
            Calculos.EsfuerzoF(this, 2);
            Calculos.EsfuerzoV(this, 1);
            Calculos.EsfuerzoV(this, 2);
            Calculos.EsfuerzoUP(this, 1);
            Calculos.EsfuerzoUP(this, 2);
        }


        Date date = new Date();
        DateFormat hora = new SimpleDateFormat("HH:mm:ss");
        inicio = "" + hora.format(date);

        Calculos.ConsiguioFicha(this,1);
        Calculos.ConsiguioFicha(this,2);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.btmNavegacion);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        mMainFrame = (FrameLayout) findViewById(R.id.fragmentContainer);


    }

    HomeFragment homeFragment = new HomeFragment();
    MotivadoresFragment motivadoresFragment = new MotivadoresFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.menu_home:
                            loadFragment(homeFragment);
                            return true;

                        case R.id.menu_motivadores:
                            loadFragment(motivadoresFragment);
                            return true;
                    }
                    return false;
                }
            };

    private void loadFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
    }

    @Override
    protected void onDestroy() {

        hora(inicio, this);

        super.onDestroy();

    }

    public static void hora(String inicio, Context con) {
        int inih = 0, inim = 0, inis = 0, finh = 0, finm = 0, fins = 0, segundos1 = 0, segundos2 = 0;
        String cadena = "", valor = "";

        Date date = new Date();
        DateFormat hora = new SimpleDateFormat("HH:mm:ss");
        cadena = "" + hora.format(date);
        System.out.println(cadena);

        valor = "" + inicio.charAt(0);
        valor = valor + "" + inicio.charAt(1);
        inih = Integer.parseInt(valor);
        valor = "";
        valor = "" + inicio.charAt(3);
        valor = valor + "" + inicio.charAt(4);
        inim = Integer.parseInt(valor);
        valor = "";
        valor = "" + inicio.charAt(6);
        valor = valor + "" + inicio.charAt(7);
        inis = Integer.parseInt(valor);
        valor = "";
        System.out.println(inih + "," + inim + "," + inis);

        valor = "" + cadena.charAt(0);
        valor = valor + "" + cadena.charAt(1);
        finh = Integer.parseInt(valor);
        valor = "";
        valor = "" + cadena.charAt(3);
        valor = valor + "" + cadena.charAt(4);
        finm = Integer.parseInt(valor);
        valor = "";
        valor = "" + cadena.charAt(6);
        valor = valor + "" + cadena.charAt(7);
        fins = Integer.parseInt(valor);
        valor = "";
        System.out.println(finh + "," + finm + "," + fins);

        // metodosDB.registrar(con,""+finh,""+finm, ""+fins);

        segundos1 = (inih * 60) * 60;
        segundos1 += inim * 60;
        segundos1 += inis;

        segundos2 = (finh * 60) * 60;
        segundos2 += finm * 60;
        segundos2 += fins;

        segundos1 = segundos2 - segundos1;

        finh = segundos1 / 3600;
        finm = (segundos1 - (3600 * finh)) / 60;
        fins = segundos1 - ((finh * 3600) + (finm * 60));

    }

    public void proceso() {
        Context con = this;
        SharedPreferences preferences = getSharedPreferences("Bayern", con.MODE_PRIVATE);
        int inpref = preferences.getInt("dia", 0);
        //Toast.makeText(this,""+inpref, Toast.LENGTH_SHORT).show();

        if (inpref == 0) {
            SharedPreferences preference = getSharedPreferences("Bayern", MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();
            editor.putInt("dia", 1);
            editor.putInt("cambio", 0);


            TimeZone timezone = TimeZone.getDefault();
            Calendar calendar = new GregorianCalendar(timezone);
            int nD = calendar.get(Calendar.DAY_OF_WEEK);
            switch (nD) {
                case 1:
                    editor.putInt("alerma", 1);
                    break;
                case 2:
                    editor.putInt("alerma", 2);
                    break;
                case 3:
                    editor.putInt("alerma", 3);
                    break;
                case 4:
                    editor.putInt("alerma", 4);
                    break;
                case 5:
                    editor.putInt("alerma", 5);
                    break;
                case 6:
                    editor.putInt("alerma", 6);
                    break;
                case 7:
                    editor.putInt("alerma", 7);
                    break;
            }
            editor.commit();

        } else {
            TimeZone timezone = TimeZone.getDefault();
            Calendar calendar = new GregorianCalendar(timezone);
            int nD = calendar.get(Calendar.DAY_OF_WEEK);
            Context cont = this;
            SharedPreferences preferenc = getSharedPreferences("Bayern", cont.MODE_PRIVATE);
            int inpre = preferences.getInt("alerma", 0);

            int cam = preferenc.getInt("cambio", 0);
            int van = 0;
            switch (nD) {
                case 1:
                    if (1 == inpre) {
                        van = 1;
                        llamada();
                    }
                    break;
                case 2:
                    if (2 == inpre) {
                        van = 1;
                        llamada();
                    }
                    break;
                case 3:
                    if (3 == inpre) {
                        van = 1;
                        llamada();
                    }
                    break;
                case 4:
                    if (4 == inpre) {
                        van = 1;
                        llamada();
                    }
                    break;
                case 5:
                    if (5 == inpre) {
                        van = 1;
                        llamada();
                    }
                    break;
                case 6:
                    if (6 == inpre) {
                        van = 1;
                        llamada();
                    }
                    break;
                case 7:
                    if (7 == inpre) {
                        van = 1;
                        llamada();
                    }
                    break;
            }

            if (van == 0) {
                SharedPreferences.Editor editor = preferenc.edit();
                editor.remove("cambio");
                editor.putInt("cambio", 1);
                editor.remove("dia");
                editor.putInt("dia", 0);
                editor.commit();
            }

        }
    }

    public void llamada() {
        Context cont = this;
        SharedPreferences preferenc = getSharedPreferences("Bayern", cont.MODE_PRIVATE);
        int di = preferenc.getInt("dia", 0);
        int cam = preferenc.getInt("cambio", 0);
        if (di == 1 && cam == 1) {
            SharedPreferences.Editor editor = preferenc.edit();
            editor.remove("dia");
            editor.putInt("dia", 0);
            editor.commit();
            Context context = this;
            Calendar today = Calendar.getInstance();
            int hour = today.get(Calendar.HOUR_OF_DAY);
            int minute = today.get(Calendar.MINUTE);
            today.set(Calendar.HOUR_OF_DAY, hour);
            today.set(Calendar.MINUTE, minute);
            today.set(Calendar.SECOND, 0);
            alarma(MainActivity.this, alarmID, today.getTimeInMillis());
        }
    }

    public void alarma(Context context, int i, Long timestamp) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(context, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

}

