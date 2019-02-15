package tecnologia.apptectextil;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity_Configuracion extends AppCompatActivity {

   public SQLLiteAdmin adminBDin;
    SQLiteDatabase bdMovil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__configuracion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //*******************FLECHA ATRAS******
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //*************************************

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //INICIAR BASE DATOS LOCAL - SQLLITE
        //**********************************************************************************************************
        adminBDin = new SQLLiteAdmin(this, "administracion", null, 1);
        //*********************************************************************************************************

       this.mUI();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case android.R.id.home:
                Log.e("ActionBar", "Atrás!");
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    EditText uitxtURL_WS,uitxtIP , uitxtPUERTO, uitxtNOMBRESERVICIO ;
    private  void mUI()
    {

        uitxtURL_WS = (EditText) findViewById(R.id.txtURL_WS);
         uitxtIP = (EditText) findViewById(R.id.txtIP);
         uitxtPUERTO = (EditText) findViewById(R.id.txtPUERTO);
         uitxtNOMBRESERVICIO = (EditText) findViewById(R.id.txtNOMBRESERVICIO);

        this.mObtenerParametrosGuardados();
    }


    private  void mObtenerParametrosGuardados()
    {
        try{
            bdMovil = adminBDin.getReadableDatabase();
            Cursor c = bdMovil.rawQuery("SELECT URL_WS, IP, PUERTO, NOMBRESERVICIO "+
                    "FROM TB_CONFIGURACION ", null);

            if(c.getCount()>0)
            {
                String sURL_WS="";
                String sIP="";
                String sPUERTO="";
                String sNOMBRESERVICIO="";
                if (c.moveToFirst()){
                    do {
                        // Passing values
                        sURL_WS = c.getString(0);//URL_WS
                        sIP = c.getString(1);//IP
                        sPUERTO = c.getString(2);//PUERTO
                        sNOMBRESERVICIO = c.getString(3);//NOMBRESERVICIO

                        uitxtURL_WS.setText(sURL_WS);
                        uitxtIP.setText(sIP);
                        uitxtPUERTO.setText(sPUERTO);
                        uitxtNOMBRESERVICIO.setText(sNOMBRESERVICIO);

                        break;
                        // Do something Here with values
                    } while(c.moveToNext());
                }
                bdMovil.close();

            }
        }
        catch (Exception  exc )
        {
            mMsg("error: mObtenerParametrosGuardados: "+exc.toString(),2);
        }
    }


    public void onClick_btnGUARDAR(View v) {


        String suitxtIP= uitxtIP.getText().toString().trim();
        String suitxtPUERTO= uitxtPUERTO.getText().toString().trim();
        String suitxtNOMBRESERVICIO= uitxtNOMBRESERVICIO.getText().toString().trim();

        String srpta = this.mGuardardar_ConexionWS(suitxtIP,suitxtPUERTO,suitxtNOMBRESERVICIO);

        if(srpta.equals(""))
        {
            this.mMsg("Conexion de Servicio Registrada!",0);
            this.mObtenerParametrosGuardados();

        }
        else
        {
            this.mMsg("Error: "+srpta,1);
        }
    }


    public String  mGuardardar_ConexionWS(String sIp,String sPuerto,String sNombreServicio)
    {
        String sRpta="";

        try{

            bdMovil = adminBDin.getReadableDatabase();
            Cursor c = bdMovil.rawQuery(
                            "SELECT URL_WS, IP, PUERTO, NOMBRESERVICIO "+
                                "FROM TB_CONFIGURACION ", null);

            if(c.getCount()>0)
            {
                bdMovil.close();

                SQLiteDatabase db = adminBDin.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
                db.delete("TB_CONFIGURACION", null, null);
            }

                bdMovil.close();

                String baseUrl = "http://"+sIp+":"+sPuerto+"/Api/"+sNombreServicio+"/";

                bdMovil = adminBDin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("IP", sIp);//COLUMNA
                registro.put("PUERTO", sPuerto);//COLUMNA
                registro.put("NOMBRESERVICIO", sNombreServicio);//COLUMNA
                registro.put("URL_WS", baseUrl);//COLUMNA

                bdMovil.insert("TB_CONFIGURACION", null, registro);//NOMBRE TABLA
                bdMovil.close();

        }
        catch (Exception  exc )
        {
            //this.mMsg("mGuardardar_ConexionWS: "+exc.toString(),1);
            sRpta="mGuardardar_ConexionWS: "+exc.toString();
        }

        return  sRpta;
    }

    public void mMsg(String sMsg, int i) {
        AlertDialog alert = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String sTitulo = "";
        if (i == 0) {
            sTitulo = "Bien";

            builder.setTitle(sTitulo)
                    .setMessage(sMsg)

                    .setIcon(R.drawable.ic_bien)//((bTipo) ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_mood_bad_black_24dp)
                    .setPositiveButton("ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //  listener.onPossitiveButtonClick();
                                }
                            });
        } else if (i == 1) {
            sTitulo = "Error";
            builder.setTitle(sTitulo)
                    .setMessage(sMsg)

                    .setIcon(R.drawable.ic_error)//((bTipo) ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_mood_bad_black_24dp)
                    .setPositiveButton("ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //  listener.onPossitiveButtonClick();
                                }
                            });
        } else if (i == 2) {
            sTitulo = "Advertencia";
            builder.setTitle(sTitulo)
                    .setMessage(sMsg)

                    .setIcon(R.drawable.ic_advertencia)//((bTipo) ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_mood_bad_black_24dp)
                    .setPositiveButton("ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //  listener.onPossitiveButtonClick();
                                }
                            });
        }

        builder.create();
        alert = builder.create();
        alert.show();
    }


}
