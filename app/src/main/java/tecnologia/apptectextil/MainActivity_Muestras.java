package tecnologia.apptectextil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tecnologia.apptectextil.Modelo.Modelo;

public class MainActivity_Muestras extends AppCompatActivity {

    adapter_lista_muestras adapter_lista_muestras_;
    ListView listalvlTERMINADO,listalvlCRUDO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__muestras);
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


        mUI();

        if (this.mConectado_WIFI_DATOS() != 0) {

            this.mObtenerInfoSistema();

        } else {
            mMsg("Ups.. Paquete de DATOS o WIFFI ´´DESACTIVADOS´´", 1);
        }

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


    private  void mUI()
    {

        this.listalvlTERMINADO = (ListView) findViewById(R.id.lvlTERMINADO);
        this.listalvlCRUDO = (ListView) findViewById(R.id.lvlCRUDO);

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



    private ListView mySpinner;
    // Custom Spinner adapter (ArrayAdapter<User>)
    // You can define as a private to use it in the all class
    // This is the object that is going to do the "magic"
    private SpinAdapter adapter;



    private void mCargar_terminado(JSONArray sdata)
    {
        int iVal = sdata.length();

        int[] imagenes = new int[iVal];

        String[] UCOMIN = new String[iVal];
        String[] UFIMIN = new String[iVal];
        String[] UFOMIN = new String[iVal];
        String[] PROD = new String[iVal];
        String[] DESCOL = new String[iVal];

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());

                String sUCOMIN= mainObject.get("UCOMIN").toString();
                String sUFIMIN= mainObject.get("UFIMIN").toString();
                String sUFOMIN= mainObject.get("UFOMIN").toString();
                String sPROD= mainObject.get("PROD").toString();
                String sDESCOL= mainObject.get("DESCOL").toString();

                imagenes[i] = R.mipmap.alta1;
                UCOMIN[i]=sUCOMIN;
                UFIMIN[i]=sUFIMIN;
                UFOMIN[i]=sUFOMIN;
                PROD[i]=sPROD;
                DESCOL[i]=sDESCOL;

            }

            this.adapter_lista_muestras_ = new adapter_lista_muestras(this, imagenes,UCOMIN,UFIMIN,UFOMIN,PROD,DESCOL,0);

            this.listalvlTERMINADO.setAdapter(adapter_lista_muestras_);

        }
        catch (Exception ex)
        {

            String ss= ex.getMessage();
        }

    }

    private void mCargar_crudo(JSONArray sdata)
    {
        int iVal = sdata.length();

        int[] imagenes = new int[iVal];

        String[] UCOMIN = new String[iVal];
        String[] UFIMIN = new String[iVal];
        String[] UFOMIN = new String[iVal];
        String[] PROD = new String[iVal];
        String[] DESCOL = new String[iVal];

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());

                String sUCOMIN= mainObject.get("UCOMIC").toString();
                String sUFIMIN= mainObject.get("UFIMIC").toString();
                String sUFOMIN= mainObject.get("UFOMIC").toString();
                String sPROD= mainObject.get("PROD").toString();
                String sDESCOL= mainObject.get("DESCOL").toString();

                imagenes[i] = R.mipmap.alta1;
                UCOMIN[i]=sUCOMIN;
                UFIMIN[i]=sUFIMIN;
                UFOMIN[i]=sUFOMIN;
                PROD[i]=sPROD;
                DESCOL[i]=sDESCOL;
            }

            this.adapter_lista_muestras_ = new adapter_lista_muestras(this, imagenes,UCOMIN,UFIMIN,UFOMIN,PROD,DESCOL,1);

            this.listalvlCRUDO.setAdapter(adapter_lista_muestras_);

        }
        catch (Exception ex)
        {

            String ss= ex.getMessage();
        }
    }




    public void mObtenerInfoSistema() {

        try {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Obteniendo Informacion, por favor espere...");
            // progressDialog.setProgressStyle(android.R.attr.progressBarStyleLarge);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                progressDialog.setIndeterminateDrawable(drawable);
            }
            new MyTask_mObtenerInfoSistema(progressDialog, this).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
    }


    // RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.
    //http://localhost:8092/Api/AppTecnologiaTextil/mLogin/JEFSIST/76CC1E1C71
  //  String baseUrl = "http://192.168.166.61:8092/Api/AppTecnologiaTextil/";  // This is the API base URL (GitHub API)
   // String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.

//    HttpURLConnection urlConnection;


    public class MyTask_mObtenerInfoSistema extends AsyncTask<Void, Void, Void> {

        Modelo mObtenerTERMINADO = new Modelo();
        Modelo mObtenerCRUDO = new Modelo();

        ProgressDialog progress;
        MainActivity_Muestras act;

        String sRpta="";

        public MyTask_mObtenerInfoSistema(ProgressDialog progress, MainActivity_Muestras act) {
            this.progress = progress;
            this.act = act;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Object obj = new Object();
                obj =  mObtenerTERMINADO();
                mObtenerTERMINADO=(Modelo)obj;

                obj = new Object();
                obj =  mObtenerCRUDO();
                mObtenerCRUDO=(Modelo)obj;
                //   modCompania =  mObtenerCompania();

                // mod=(Modelo)obj;

            }
            catch (Exception ee)
            {
                sRpta="(Movil): "+ee.getMessage().toString();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

            progress.dismiss();

            if(mObtenerTERMINADO.bEstado==true)
            {
                JSONArray sdata= (JSONArray)mObtenerTERMINADO.obj;

                if( sdata.length() >0)
                {
                    mCargar_terminado(sdata);
                }
                else
                    mMsg("No se pudo obtener Informacion del sistema  PROD TERMINADO!..", 2);
            }
            else
                mMsg("Error!. "+mObtenerTERMINADO.sRpta +sRpta, 1);


            if(mObtenerCRUDO.bEstado==true)
            {
                JSONArray sdata= (JSONArray)mObtenerCRUDO.obj;

                if( sdata.length() >0)
                {
                    mCargar_crudo(sdata);
                }
                else
                    mMsg("No se pudo obtener Informacion del sistema - CRUDO!..", 2);
            }
            else
                mMsg("Error!. "+mObtenerCRUDO.sRpta +sRpta, 1);
        }
    }


    private  Object mObtenerTERMINADO()
    {

        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mObtenerTERMINADO";
        String sParametros="";

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
            //urlConnection.disconnect();
        }

        return objRsult;
    }


    private  Object mObtenerCRUDO()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mObtenerCRUDO";
        String sParametros="";

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
           // urlConnection.disconnect();
        }

        return objRsult;
    }





    public class Item{

        private String _id;
        private String _name;

        public Item(){
            this._id = "";
            this._name = "";
        }

        public void setId(String id){
            this._id = id;
        }

        public String getId(){
            return this._id;
        }

        public void setName(String name){
            this._name = name;
        }

        public String getName(){
            return this._name;
        }
    }

    public class SpinAdapter extends ArrayAdapter<Item> {

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private Item[] values;

        public SpinAdapter(Context context, int textViewResourceId,
                           Item[] values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.length;
        }

        @Override
        public Item getItem(int position){
            return values[position];
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(values[position].getName());

            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].getName());

            return label;
        }
    }

}
