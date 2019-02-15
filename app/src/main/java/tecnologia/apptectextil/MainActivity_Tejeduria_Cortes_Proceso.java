package tecnologia.apptectextil;

import android.app.DatePickerDialog;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;

import tecnologia.apptectextil.Modelo.Modelo;

public class MainActivity_Tejeduria_Cortes_Proceso extends AppCompatActivity {

    EditText uitxtFECHA;

    DatePickerDialog datePickerDialog;

    private Spinner mySpinner;
    // Custom Spinner adapter (ArrayAdapter<User>)
    // You can define as a private to use it in the all class
    // This is the object that is going to do the "magic"
    private SpinAdapter adapter;
    Spinner cboCompania;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__tejeduria__cortes__proceso);
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


        this.mUI();

        this.uitxtFECHA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // calender class's instance and get current date , month and year from calender
                String sFH= uitxtFECHA.getText().toString();

                String sDia= sFH.substring(0,2);
                String sMes= sFH.substring(3,5);
                String sAnio= sFH.substring(6);

                int mYear = Integer.parseInt(sAnio); // current year
                int mMonth = Integer.parseInt(sMes); // current month
                int mDay = Integer.parseInt(sDia); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity_Tejeduria_Cortes_Proceso.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                // monthOfYear=monthOfYear+1;
                                String sDia= String.format("%02d", dayOfMonth);
                                String sMes= String.format("%02d", (monthOfYear+1));

                                uitxtFECHA.setText(sDia + "/" + sMes + "/" + year);
                            }
                        }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });



        if (this.mConectado_WIFI_DATOS() != 0) {

            this.mObtenerInfoSistema();

        } else {
            mMsg("Ups.. Paquete de DATOS o WIFFI ´´DESACTIVADOS´´", 1);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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



    private  void mUI() {

        this.uitxtFECHA = (EditText) findViewById(R.id.txtFecha);
        cboCompania = findViewById(R.id.spnCompania);

        Calendar c = Calendar.getInstance();
        int iYear = c.get(Calendar.YEAR); // current year
        int iMonth = c.get(Calendar.MONTH)+1; // current month
        int iDay = c.get(Calendar.DAY_OF_MONTH); // current day

        String sDia= String.format("%02d", iDay);
        String sMes= String.format("%02d", iMonth);

        this.uitxtFECHA.setText(sDia + "/" + sMes + "/" + iYear);
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







    private void mCargar_Compania(JSONArray sdata)
    {

        // String selectedVal = getResources().getStringArray(R.array.values)[cboAlmacen.getSelectedItemPosition()];

        int iVal = sdata.length();

        Item[] items = new Item[iVal];

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());
                items[i] = new Item();
                items[i].setId(mainObject.getString("CODIGO"));
                items[i].setName(mainObject.getString("DESCRIPCION"));

            }

            adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

            mySpinner = (Spinner) findViewById(R.id.spnCompania);

            mySpinner.setAdapter(adapter);

        }
        catch (Exception ex)
        {
            String ss= ex.getMessage();

        }
    }



    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.
    //http://localhost:8092/Api/AppTecnologiaTextil/mLogin/JEFSIST/76CC1E1C71
    String baseUrl = "http://192.168.166.61:8092/Api/AppTecnologiaTextil/";  // This is the API base URL (GitHub API)
    String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.

    HttpURLConnection urlConnection;


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



    public class MyTask_mObtenerInfoSistema extends AsyncTask<Void, Void, Void> {

        Modelo modAlmacen = new Modelo();
        Modelo modCompania = new Modelo();

        ProgressDialog progress;
        MainActivity_Tejeduria_Cortes_Proceso act;

        String sRpta="";

        public MyTask_mObtenerInfoSistema(ProgressDialog progress, MainActivity_Tejeduria_Cortes_Proceso act) {
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
                obj =  mObtenerCompania();
                modCompania=(Modelo)obj;
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

            if(modCompania.bEstado==true)
            {
                JSONArray sdata= (JSONArray)modCompania.obj;

                if( sdata.length() >0)
                {
                    mCargar_Compania(sdata);
                }
                else
                    mMsg("No se pudo obtener Informacion del sistema!..", 2);
            }
            else
                mMsg("Error!. "+modCompania.sRpta +sRpta, 1);
        }
    }



    private  Object mObtenerCompania()
    {
        Object objRsult = null;

        Modelo mod = new Modelo();

        this.url = baseUrl + "mObtenerCompania";
        StringBuilder result = new StringBuilder();
        //"https://api.github.com/users/dmnugent80/repos"
        try {
            URL url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line="",X;
            while ((line = reader.readLine()) != null) {
                StringBuilder ss= result.append(line);
                X = ss.toString();
                break;
            }

            String sjson= line;
            JSONObject mainObject = new JSONObject(sjson);

            Boolean  bEstado = mainObject.getBoolean("bEstado");
            int  iCodigo = mainObject.getInt("iCodigo");
            String  sRpta = mainObject.getString("sRpta");
            JSONArray  obj = mainObject.getJSONArray("obj");

            mod.bEstado=bEstado;
            mod.iCodigo=iCodigo;
            mod.sRpta=sRpta;
            mod.obj=obj;

            objRsult= mod;

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
            urlConnection.disconnect();
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
