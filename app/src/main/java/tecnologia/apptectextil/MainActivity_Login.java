package tecnologia.apptectextil;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tecnologia.apptectextil.Modelo.Modelo;



public class MainActivity_Login extends AppCompatActivity {


    public SQLLiteAdmin adminBDin;
    SQLiteDatabase bdMovil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  /*              Snackbar.make(view, "Configuracion", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
                Intent  myIntent = new Intent(getApplicationContext(), MainActivity_Configuracion.class);
                startActivity(myIntent);
            }
        });

        //INICIAR BASE DATOS LOCAL - SQLLITE
        //**********************************************************************************************************
        adminBDin = new SQLLiteAdmin(this, "administracion", null, 1);
        //*********************************************************************************************************


        EditText uitxtUSUARIO = (EditText) findViewById(R.id.txtUSUARIO);
        EditText uitxtPASSWORD = (EditText) findViewById(R.id.txtPASSWORD);

     //   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      //  imm.hideSoftInputFromWindow(uitxtUSUARIO.getWindowToken(), 0);
/*
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(uitxtUSUARIO.getWindowToken(), 0);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(uitxtPASSWORD.getWindowToken(), 0);
*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.mUI();


    }

    EditText uitxtUSUARIO , uitxtPASSWORD ;
    CheckBox uicheckBoxRECORDARLOGIN;
    private  void mUI()
    {

        uitxtUSUARIO = (EditText) findViewById(R.id.txtUSUARIO);
        uitxtPASSWORD = (EditText) findViewById(R.id.txtPASSWORD);

        uicheckBoxRECORDARLOGIN = (CheckBox) findViewById(R.id.checkBoxRECORDARLOGIN);

        mObtenerDatosGuardados();

    }



    private Menu menu_lateral;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu_lateral = menu;
        String sVersionGradle="";
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            sVersionGradle = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        this.menu_lateral.getItem(1).setTitle("Version: "+sVersionGradle+" ");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent myIntent;

        switch (item.getItemId()) {
            case R.id.action_versionactual:
                break;

            case R.id.action_descargar_drive:
                 myIntent = new Intent(this, WebViewActivity.class);
                startActivity(myIntent);
                break;

            case R.id.action_configuracion:
                 myIntent = new Intent(this, MainActivity_Configuracion.class);
                startActivity(myIntent);
                break;
        }
        return false;
    }




    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void onClick_btnINICIAR(View v) {

        EditText uitxtUSUARIO = (EditText) findViewById(R.id.txtUSUARIO);
        EditText uitxtPASSWORD = (EditText) findViewById(R.id.txtPASSWORD);

        String sUsuario= uitxtUSUARIO.getText().toString().trim();
        String sPassword= uitxtPASSWORD.getText().toString().trim();

        if (this.mConectado_WIFI_DATOS() != 0) {

            WS_Connexion_ConfigMobil ws = new WS_Connexion_ConfigMobil(this);
            String sBaseUrlServicio = ws.mObtenerCadenaConexionWS();

            if(sBaseUrlServicio.equals(""))
            {
                mMsg("Ups.. Verifique la Configuracion de Servicio Web. \nModifique y vuelva a ingresar.", 1);
            }
            else {
                this.mLogin(sUsuario,sPassword);
            }

        } else {
            mMsg("Ups.. Paquete de DATOS o WIFFI ´´DESACTIVADOS´´", 1);
        }
    }



    protected Boolean mConectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean mConectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {

            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }


    public int mConectado_WIFI_DATOS() {

        int iRpta = 0;

        if (this.mConectadoWifi()) {
            iRpta = 1;
        } else if (this.mConectadoRedMovil()) {
            iRpta = 2;
        }

        return iRpta;
    }

    public void mLogin(String sUsuario, String sPassword) {

        try {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Autenticando, por favor espere...");
            // progressDialog.setProgressStyle(android.R.attr.progressBarStyleLarge);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                progressDialog.setIndeterminateDrawable(drawable);
            }
            new MyTaskInicioSesion(progressDialog, this,sUsuario,sPassword).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
    }





    public class MyTaskInicioSesion extends AsyncTask<Void, Void, Void> {

        Modelo mod = new Modelo();

        ProgressDialog progress;
        MainActivity_Login act;
        String sUsuario="";
        String sPassword="";

        String sRpta="";

        public MyTaskInicioSesion(ProgressDialog progress, MainActivity_Login act,String sUsuario, String sPassword) {
            this.progress = progress;
            this.act = act;
            this.sUsuario = sUsuario;
            this.sPassword = sPassword;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Object obj = new Object();

                obj =  mLogin_Consulta(this.sUsuario ,this.sPassword );
                mod=(Modelo)obj;

            }
            catch (Exception ee)
            {
                sRpta="(Movil): "+ee.getMessage().toString();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

            progress.dismiss();

            if(mod.bEstado==true)
            {
                JSONArray ss= (JSONArray)mod.obj;

                if( ss.length() >0)
                {
                    //   mMsg("Bienvenido!.."+mod.sRpta, 0);

                    // mDelay(1000);

                    mGuardardar_Login(this.sUsuario,this.sPassword,"S");

                    Intent intent=new Intent(MainActivity_Login.this,MainActivity.class);
                    intent.putExtra("Usuario",  this.sUsuario);
                    startActivity(intent);
                    finish();//destruye activity Splashf
                }
                else
                {
                    mMsg("Usuario y Password incorrectos!..", 2);
                }

            }
            else {

                mMsg("Error!. "+mod.sRpta +sRpta, 1);
            }
        }
    }






   // RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.
    //http://localhost:8092/Api/AppTecnologiaTextil/mLogin/JEFSIST/76CC1E1C71
    //String baseUrl = "http://192.168.166.12:8092/Api/AppTecnologiaTextil/mLogin/";  // This is the API base URL (GitHub API)
  //  String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.

   // HttpURLConnection urlConnection;

    private  Object mLogin_Consulta(String sUsuario,String sPassword)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mLogin";
        String sParametros="/"+ sUsuario +"/"+ sPassword + "";

        try {

            WS_Connexion_ConfigMobil ws = new WS_Connexion_ConfigMobil(this);
            String sBaseUrlServicio = ws.mObtenerCadenaConexionWS();

            objRsult = wsConsumo.mProcesarWS_PorParametros(sBaseUrlServicio,sFuncionMetodo,sParametros);

        }catch( Exception e) {

            e.printStackTrace();

            JSONArray obj = new JSONArray();
            mod.bEstado=false;
            mod.iCodigo=100;
            mod.sRpta=e.getMessage().toString();
            mod.obj=obj;

            objRsult= mod;
        }
        finally {
          //  urlConnection.disconnect();
        }

        return objRsult;
    }









    private  void mObtenerDatosGuardados()
    {
        try{
            bdMovil = adminBDin.getReadableDatabase();
            Cursor c = bdMovil.rawQuery("SELECT USUARIO ,PASSWORD ,RECORDAR_LOGIN "+
                    "FROM TB_LOGIN ", null);

            if(c.getCount()>0)
            {
                String sUSUARIO="";
                String sPASSWORD="";
                String sRECORDAR_LOGIN="";

                if (c.moveToFirst()){
                    do {
                        // Passing values
                        sUSUARIO = c.getString(0);//URL_WS
                        sPASSWORD = c.getString(1);//IP
                        sRECORDAR_LOGIN = c.getString(2);//PUERTO


                        uitxtUSUARIO.setText(sUSUARIO);
                        uitxtPASSWORD.setText(sPASSWORD);

                        if(sRECORDAR_LOGIN.equals("S"))
                            uicheckBoxRECORDARLOGIN.setChecked(true);
                        else
                            uicheckBoxRECORDARLOGIN.setChecked(false);

                        break;
                        // Do something Here with values
                    } while(c.moveToNext());
                }
                bdMovil.close();

            }
        }
        catch (Exception  exc )
        {
            mMsg("error: mObtenerDatosGuardados: "+exc.toString(),2);
        }
    }


    public String  mGuardardar_Login(String sUSUARIO,String sPASSWORD,String sRECORDARLOGIN)
    {
        String sRpta="";

        try{

            bdMovil = adminBDin.getReadableDatabase();
            Cursor c = bdMovil.rawQuery(
                    "SELECT * FROM TB_LOGIN ", null);

            if(c.getCount()>0)
            {
                bdMovil.close();

                SQLiteDatabase db = adminBDin.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
                db.delete("TB_LOGIN", null, null);
            }

            bdMovil.close();

            //USUARIO TEXT,PASSWORD TEXT,GUARDAR_LOGIN TEXT
            bdMovil = adminBDin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("USUARIO", sUSUARIO);//COLUMNA
            registro.put("PASSWORD", sPASSWORD);//COLUMNA
            registro.put("RECORDAR_LOGIN", sRECORDARLOGIN);//COLUMNA

            bdMovil.insert("TB_LOGIN", null, registro);//NOMBRE TABLA
            bdMovil.close();

        }
        catch (Exception  exc )
        {
            //this.mMsg("mGuardardar_ConexionWS: "+exc.toString(),1);
            sRpta="mGuardardar_Login: "+exc.toString();
        }

        return  sRpta;
    }





    public void mDelay(int iSegundos) {
        try {
            Thread.sleep(iSegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
