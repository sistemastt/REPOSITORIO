package tecnologia.apptectextil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import tecnologia.apptectextil.Modelo.FAB085_GuardarRegistro_Model;
import tecnologia.apptectextil.Modelo.Modelo;

public class MainActivity_Inventario_FAB085 extends AppCompatActivity {

    String sUsuarioAll = "";
    ImageButton btnScan;

    ImageButton btnBuscar;
    Button btnSalir;
    Button btnGuardar;
    Button btnNuevo;

    EditText txtETIQUETA,  txtZONA,txtANDAMIO ,txtCOORDENADA ,txtCONTADOR,
            txtCODIGOART , txtDESCRIPCION , txtKILOS , txtCONOS ,txtCOLOR ,txtPROVEEDOR , txtLOTE ,txtUNIDAD_MEDIDA;

    Spinner cboCompania;
    Spinner cboAlmacen;
    Spinner cboTipoExistencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__inventario__fab085);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //*******************FLECHA ATRAS******
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //*************************************

        this.mUI();

        if (this.mConectado_WIFI_DATOS() != 0) {

            this.mObtenerInfoSistema();

        } else {
            mMsg("Ups.. Paquete de DATOS o WIFFI ´´DESACTIVADOS´´", 1);
        }

        this.txtETIQUETA.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
              /*  if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(getApplicationContext(), "Escaneado:" + txtRollo.getText(), Toast.LENGTH_SHORT).show();
                    mProcesarBusqueda();
                    //return true;
                }
                return true;*/
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP && txtETIQUETA.length() > 0) {

                      String sEtiqueta= txtETIQUETA.getText().toString().trim();

                        String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
                        String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;
                        String  sCodTipoExistencia = ((Item)cboTipoExistencia.getSelectedItem())._id;

                        String  sZona = txtZONA.getText().toString().trim();
                        String sAndamio= txtANDAMIO.getText().toString();
                        String sCoordenada= txtCOORDENADA.getText().toString();

                        String sRpta= mValidar(sEtiqueta,sZona,sAndamio,sCoordenada);
                        if(sRpta.equals(""))
                        {
                            mProcesarBusqueda(sCodCompania,sCodAlmacen,sCodTipoExistencia,sEtiqueta,sUsuarioAll,sZona,sAndamio,sCoordenada);
                        }
                        else
                            mMsg("Error!. "+sRpta, 1);


                    }
                    return true;
                }
                return false;
            }
        });



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
            case android.R.id.home:
                Log.e("ActionBar", "Atrás!");
                finish();
                break;

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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(this, "Scanner Cancelado por el usuario", Toast.LENGTH_LONG).show();

            } else {
                String sRptaScan = result.getContents();

                this.txtETIQUETA.setText(sRptaScan.trim());


                String sEtiqueta= txtETIQUETA.getText().toString().trim();
                String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
                String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;
                String  sCodTipoExistencia = ((Item)cboTipoExistencia.getSelectedItem())._id;

                String  sZona = this.txtZONA.getText().toString().trim();
                String sAndamio= this.txtANDAMIO.getText().toString();
                String sCoordenada= this.txtCOORDENADA.getText().toString();

                String sRpta= mValidar(sEtiqueta,sZona,sAndamio,sCoordenada);
                if(sRpta.equals(""))
                {
                    this.mProcesarBusqueda(sCodCompania,sCodAlmacen,sCodTipoExistencia,sEtiqueta,this.sUsuarioAll,sZona,sAndamio,sCoordenada);
                }
                else
                    mMsg("Error!. "+sRpta, 1);
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }






    private void mUI() {
        sUsuarioAll = (String) getIntent().getExtras().getSerializable("Usuario");


        btnScan = findViewById(R.id.btnScan);
        btnBuscar = findViewById(R.id.btnBuscar);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnNuevo = findViewById(R.id.btnNuevo);
        txtETIQUETA = findViewById(R.id.txtETIQUETA);

        cboCompania = findViewById(R.id.spnCompania);
        cboAlmacen = findViewById(R.id.spnAlmacen);
        cboTipoExistencia = findViewById(R.id.spnTipoExistencia);

        txtZONA = findViewById(R.id.txtZONA);
        txtANDAMIO = findViewById(R.id.txtANDAMIO);
        txtCOORDENADA = findViewById(R.id.txtCOORDENADA);

        txtCONTADOR = findViewById(R.id.txtCONTADOR);

        txtCODIGOART = findViewById(R.id.txtCODIGOART);
        txtDESCRIPCION = findViewById(R.id.txtDESCRIPCION);
        txtKILOS = findViewById(R.id.txtKILOS);
        txtCONOS = findViewById(R.id.txtCONOS);
        txtCOLOR = findViewById(R.id.txtCOLOR);
        txtPROVEEDOR = findViewById(R.id.txtPROVEEDOR);
        txtLOTE = findViewById(R.id.txtLOTE);
        txtUNIDAD_MEDIDA = findViewById(R.id.txtUNIDAD_MEDIDA);


    }




    public void onClick_btnScan(View v) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

    }

    public void onClick_btnBuscar(View v) {

        String sEtiqueta= txtETIQUETA.getText().toString().trim();
        String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
        String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;
        String  sCodTipoExistencia = ((Item)cboTipoExistencia.getSelectedItem())._id;
         String  sZona = this.txtZONA.getText().toString().trim();
         String sAndamio= this.txtANDAMIO.getText().toString();
         String sCoordenada= this.txtCOORDENADA.getText().toString();

        String sRpta= mValidar(sEtiqueta,sZona,sAndamio,sCoordenada);
        if(sRpta.equals(""))
        {
            this.mProcesarBusqueda(sCodCompania,sCodAlmacen,sCodTipoExistencia,sEtiqueta,this.sUsuarioAll,sZona,sAndamio,sCoordenada);
        }
        else
            mMsg("Error!. "+sRpta, 1);
    }

    public void onClick_btnNuevo(View v) {
        this.ClearControls();
    }

    public void onClick_btnGuardar(View v) {

        String sEtiqueta= txtETIQUETA.getText().toString().trim();

        String sCodCompania = ((Item)this.cboCompania.getSelectedItem())._id;
        String  sCodAlmacen = ((Item)this.cboAlmacen.getSelectedItem())._id;
        String  sCodTipoExistencia = ((Item)this.cboTipoExistencia.getSelectedItem())._id;

        String  sZona = this.txtZONA.getText().toString().trim();
        String sAndamio= this.txtANDAMIO.getText().toString();
        String sCoordenada= this.txtCOORDENADA.getText().toString();

        String sKilos= this.txtKILOS.getText().toString();
        String sConos= this.txtCONOS.getText().toString();

        String sCodArt= this.txtCODIGOART.getText().toString();

        //  float fsKilos = Float.valueOf(sKilos.trim());
       // sKilos = String.format("%.06f", fsKilos);
        if(sKilos.equals(""))
            sKilos="0.0";

        double dou = Double.parseDouble(sKilos);
        sKilos = String.format("%.6f", dou );

        if(sKilos.indexOf('.')!= -1)
        {
            sKilos= sKilos.replace('.', 'A');
        }


        if(dou==0.0)
        {
            this.mMsg("Debe asignar un valor al campo [CANTIDAD]",2);
            this.txtKILOS.requestFocus();
            return;
        }

        if(sEtiqueta.equals("")==true || sZona.equals("")==true|| sAndamio.equals("")==true|| sCoordenada.equals("")==true
                || sKilos.equals("")==true || sConos.equals("")==true || sCodArt.equals("")==true)
            mMsg("Error!. Debe ingresar todos los parametros basicos para continuar.", 1);
        else
            this.mGuardarEtiqueta(sCodCompania,sCodAlmacen,sCodTipoExistencia,sEtiqueta,sZona,sAndamio,sCoordenada,sKilos,sConos,this.sUsuarioAll);


    }


//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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






    private void ClearControls() {
        this.txtETIQUETA.setText("");
        this.txtCODIGOART.setText("");
        this.txtDESCRIPCION.setText("");
        this.txtKILOS.setText("");
        this.txtCONOS.setText("");
        this.txtCOLOR.setText("");
        this.txtPROVEEDOR.setText("");
        this.txtLOTE.setText("");
        this.txtUNIDAD_MEDIDA.setText("");
    }


//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private Spinner mySpinner;
    // Custom Spinner adapter (ArrayAdapter<User>)
    // You can define as a private to use it in the all class
    // This is the object that is going to do the "magic"
    private SpinAdapter adapter;

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
        catch (Exception ex)
        {
            String ss= "Error: mObtenerInfoSistema - \n"+ex.getMessage();
            this.mMsg(ss,1);
        }
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
                items[i].setId(mainObject.getString("CODCIA"));
                items[i].setName(mainObject.getString("DESCIA"));

            }

            adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

            mySpinner = (Spinner) findViewById(R.id.spnCompania);

            mySpinner.setAdapter(adapter);

        }
        catch (Exception ex)
        {
            String ss= "Error: mCargar_Compania - \n"+ex.getMessage();
            this.mMsg(ss,1);
        }
    }

    private void mCargar_Almacen(JSONArray sdata)
    {
        int iVal = sdata.length();

        Item[] items = new Item[iVal];

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());
                items[i] = new Item();
                items[i].setId(mainObject.getString("CODALG"));
                items[i].setName(mainObject.getString("DESALG"));
            }

            adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

            mySpinner = (Spinner) findViewById(R.id.spnAlmacen);

            mySpinner.setAdapter(adapter);

        }
        catch (Exception ex)
        {

            String ss= "Error: mCargar_Almacen - \n"+ex.getMessage();
            this.mMsg(ss,1);
        }

/*
        adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

        mySpinner = (Spinner) findViewById(R.id.spnAlmacen);

        mySpinner.setAdapter(adapter);*/
        //  this.uicmbDEP.setOnItemSelectedListener(this);
    }

    private void mCargar_TipoExistencia(JSONArray sdata)
    {
        int iVal = sdata.length();

        Item[] items = new Item[iVal];

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());
                items[i] = new Item();
                items[i].setId(mainObject.getString("CODTEX"));
                items[i].setName(mainObject.getString("DESTEX"));
            }

            adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

            mySpinner = (Spinner) findViewById(R.id.spnTipoExistencia);

            mySpinner.setAdapter(adapter);

        }
        catch (Exception ex)
        {

            String ss= "Error: mCargar_Almacen - \n"+ex.getMessage();
            this.mMsg(ss,1);
        }

/*
        adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

        mySpinner = (Spinner) findViewById(R.id.spnAlmacen);

        mySpinner.setAdapter(adapter);*/
        //  this.uicmbDEP.setOnItemSelectedListener(this);
    }



    private String mValidar(String sEtiqueta,String sZona,String sAndamio,String sCoordenada)
    {
        String sRpta="";

        if(sEtiqueta.equals("")==true || sZona.equals("")==true|| sAndamio.equals("")==true|| sCoordenada.equals("")==true)
        {
            sRpta="Ingrese los parametros Basicos";
        }

        return sRpta;
    }

    private void mProcesarBusqueda(String sCompania,String sAlmacen,String sCodTipoExistencia,String sEtiqueta,String sUsuario, String sZona, String sAndamio, String sCoordenada )
    {


        Toast.makeText(getApplicationContext(), "Escaneado:" + sEtiqueta, Toast.LENGTH_SHORT).show();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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
            new MyTask_mProcesarBusqueda(progressDialog, this, sCompania, sAlmacen,sCodTipoExistencia,sEtiqueta,sUsuario,sZona,  sAndamio,  sCoordenada  ).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }


    }


    private void mGuardarEtiqueta(String sCodCompania,String sCodAlmacen,String sCodTipoExistencia,String sEtiqueta,String sZona,String sAndamio,String sCoordenada
                                ,String sKilos,String sConos,String sUsuario ) {
        try {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Guardando Informacion, por favor espere...");
            // progressDialog.setProgressStyle(android.R.attr.progressBarStyleLarge);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                progressDialog.setIndeterminateDrawable(drawable);
            }
            new MyTask_mGuardarRegistro(progressDialog, this,sCodCompania,sCodAlmacen,sCodTipoExistencia,sEtiqueta,sZona,sAndamio, sCoordenada, sKilos,sConos,sUsuario).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
    }








    private void mCargar_ObjetosInterfaz(JSONArray sdata)
    {
        int iVal = sdata.length();

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());
                String sCODEXI= mainObject.get("CODEXI").toString();//Codigo
                String sCODCHI= mainObject.get("CODCHI").toString();//COLOR
                String sNLHMAG= mainObject.get("NLHMAG").toString();//LOTES
                String sCANINV= mainObject.get("CANINV").toString();//KILOS
                String stxtCONOS= mainObject.get("CAEINV").toString();//CONOS
                String sCODPRV= mainObject.get("CODPRV").toString();//COD PROVEEDOR
                String sURHMAG= mainObject.get("URHMAG").toString();//UNIDAD DE MEDIDA


                double dousCANINV = Double.parseDouble(sCANINV);
                sCANINV = String.format("%.6f", dousCANINV );


                txtCODIGOART.setText(sCODEXI);
                txtDESCRIPCION.setText("");
                txtKILOS.setText(sCANINV);
                txtCONOS.setText(stxtCONOS);
                txtCOLOR.setText(sCODCHI);
                txtPROVEEDOR.setText(sCODPRV);
                txtLOTE.setText(sNLHMAG);
                txtUNIDAD_MEDIDA.setText(sURHMAG);


                break;
            }
        }
        catch (Exception ex)
        {

            String ss= "Error: mCargar_ObjetosInterfaz - \n"+ex.getMessage();
            this.mMsg(ss,1);
        }

/*
        adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

        mySpinner = (Spinner) findViewById(R.id.spnAlmacen);

        mySpinner.setAdapter(adapter);*/
        //  this.uicmbDEP.setOnItemSelectedListener(this);
    }






    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.
    //http://localhost:8092/Api/AppTecnologiaTextil/mLogin/JEFSIST/76CC1E1C71
    // String baseUrl = "http://192.168.166.61:8092/Api/AppTecnologiaTextil/";  // This is the API base URL (GitHub API)
    // String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.

    //  HttpURLConnection urlConnection;


    public class MyTask_mObtenerInfoSistema extends AsyncTask<Void, Void, Void> {

        Modelo modAlmacen = new Modelo();
        Modelo modCompania = new Modelo();
        Modelo modTipoExistencia = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_FAB085 act;

        String sRpta="";

        public MyTask_mObtenerInfoSistema(ProgressDialog progress, MainActivity_Inventario_FAB085 act) {
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
                obj =  mObtenerAlmacen();
                modAlmacen=(Modelo)obj;

                obj = new Object();
                obj =  mObtenerCompania();
                modCompania=(Modelo)obj;
                //   modCompania =  mObtenerCompania();


                obj = new Object();
                obj =  mObtenerTipoExistencia();
                modTipoExistencia=(Modelo)obj;

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

            if(modAlmacen.bEstado==true)
            {
                JSONArray sdata= (JSONArray)modAlmacen.obj;

                if( sdata.length() >0)
                {
                    mCargar_Almacen(sdata);
                }
                else
                    mMsg("No se pudo obtener Informacion del sistema!..", 2);
            }
            else
                mMsg("Error!. "+modAlmacen.sRpta +sRpta, 1);


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

            if(modTipoExistencia.bEstado==true)
            {
                JSONArray sdata= (JSONArray)modTipoExistencia.obj;

                if( sdata.length() >0)
                {
                    mCargar_TipoExistencia(sdata);
                }
                else
                    mMsg("No se pudo obtener Informacion del sistema!..", 2);
            }
            else
                mMsg("Error!. "+modTipoExistencia.sRpta +sRpta, 1);

        }
    }

    private  Object mObtenerCompania()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="FAB085_mObtenerCompania";
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
            //   urlConnection.disconnect();
        }

        return objRsult;
    }

    private  Object mObtenerAlmacen()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="FAB085_mObtenerAlmacen";
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
            //  urlConnection.disconnect();
        }

        return objRsult;
    }

    private  Object mObtenerTipoExistencia()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="FAB085_mObtenerTipoExistencia";
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
            //   urlConnection.disconnect();
        }

        return objRsult;
    }






    public class MyTask_mProcesarBusqueda extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();
        Modelo modContador = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_FAB085 act;

        String sRpta="",sEtiqueta="", sCodCompania="", sCodAlmacen="",sCodTipoExistencia="",sUsuario="",sZona="",sAndamio="",sCoordenada="";
        boolean bRpta=false;

        String sCONT="";

        JSONArray sDataContador=null;
        JSONArray sDataInfoObj=null;


        public MyTask_mProcesarBusqueda(ProgressDialog progress, MainActivity_Inventario_FAB085 act
             ,String sCompania,String sAlmacen,String sCodTipoExistencia ,String sEtiqueta,String sUsuario, String sZona, String sAndamio, String sCoordenada) {
            this.progress = progress;
            this.act = act;

            this.sCodCompania = sCompania;
            this.sCodAlmacen = sAlmacen;
            this.sCodTipoExistencia = sCodTipoExistencia;

            this.sEtiqueta = sEtiqueta;
            this.sUsuario =  sUsuario;
            this.sZona =  sZona;
            this.sAndamio =  sAndamio;
            this.sCoordenada =  sCoordenada;

        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object obj = new Object();
                obj =  mLecturaEtiqueta( this.sCodCompania,this.sCodAlmacen,this.sCodTipoExistencia,this.sEtiqueta,this.sUsuario);
                modRpta=(Modelo)obj;

                if(modRpta.bEstado==true)
                {
                    JSONArray sdata= (JSONArray)modRpta.obj;

                    if( sdata.length() > 0)
                    {
                        bRpta=true;

                        sDataInfoObj= sdata;

                        Object objJson = sdata.get(0);
                        JSONObject mainObject = new JSONObject(objJson.toString());

                        String sFECINV= mainObject.get("FECINV").toString();
                        if(sFECINV.equals("null") ==false )
                        {
                            bRpta=false;
                            String sError= "Error WS: "+modRpta.sRpta+"-";
                            sRpta="La Etiqueta ya Fue inventariada. \n"+sError;
                        }
                    }
                    else
                    {
                        bRpta=false;
                        String sError= "Error WS: "+modRpta.sRpta+"-";
                        sRpta="N° de Etiqueta no existe. \n"+sError;
                    }

                    obj = new Object();
                    obj =  mContador( this.sCodCompania,this.sCodAlmacen,this.sCodTipoExistencia, sZona,  sAndamio,  sCoordenada);
                    modContador=(Modelo)obj;
                    if(modContador.bEstado==true)
                    {
                        sdata= (JSONArray)modContador.obj;

                        if( sdata.length() > 0)
                        {
                            sDataContador= sdata;
                            Object objJson = sdata.get(0);
                            JSONObject mainObject = new JSONObject(objJson.toString());

                            sCONT= mainObject.get("CONT").toString();
                        }
                    }
                }
                else
                {
                    bRpta=false;
                    String sError= "Error WS: "+modRpta.sRpta+"-";
                    sRpta= sError;
                }
            }
            catch (Exception ee)
            {
                sRpta="(Movil): "+ee.getMessage().toString();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

            progress.dismiss();

            if(modRpta.bEstado==true)
            {
                if(bRpta)
                {
                    if( sDataInfoObj.length() >0) {
                        mCargar_ObjetosInterfaz(sDataInfoObj);
                    }
                }
                else {
                    mMsg(this.sRpta, 1);
                    txtETIQUETA.setText("");
                }
            }
            else
            {
                mMsg("Error!. "+modRpta.sRpta +sRpta, 1);
                txtETIQUETA.setText("");
            }

            if(modContador.bEstado==true)
            {
                if(sDataContador.length()>0)
                {
                    txtCONTADOR.setText(sCONT);
                }
            }

        }
    }

    private  Object mLecturaEtiqueta(String sCodCompania,String sCodAlmacen,String sCodTipoExistencia,String sEtiqueta,String sUsuario )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="FAB085_mLecturaEtiqueta";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sCodTipoExistencia+"/"+sEtiqueta+"/"+sUsuario;

        try {
            WS_Connexion_ConfigMobil ws = new WS_Connexion_ConfigMobil(this);   //ddd
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


    private  Object mContador(String sCodCompania, String sCodAlmacen,String sCodTipoExistencia, String sZona, String sAndamio, String sCoordenada )
    {

        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="FAB085_mContador";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sCodTipoExistencia+"/"+sZona+"/"+sAndamio+"/"+sCoordenada;

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







    public class MyTask_mGuardarRegistro extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();

        Modelo modContador = new Modelo();
        String sCONT="";
        JSONArray sDataContador=null;

        ProgressDialog progress;
        MainActivity_Inventario_FAB085 act;

        String sRpta="",sCodAlmacen="",sCodCompania="",sCodTipoExistencia="",sEtiqueta="",sZona="",sAndamio="",sCoordenada="",sKilos="",sConos="",sUsuario="";

        public MyTask_mGuardarRegistro(ProgressDialog progress, MainActivity_Inventario_FAB085 act
                ,String sCodCompania,String sCodAlmacen,String sCodTipoExistencia,String sEtiqueta,String sZona,String sAndamio,String sCoordenada
                ,String sKilos,String sConos,String sUsuario ) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania = sCodCompania;
            this.sCodAlmacen = sCodAlmacen;
            this.sCodTipoExistencia=sCodTipoExistencia;
            this.sEtiqueta = sEtiqueta;
            this.sZona=sZona;
            this.sAndamio=sAndamio;
            this.sCoordenada=sCoordenada;
            this.sKilos=sKilos;
            this.sConos=sConos;
            this.sUsuario=sUsuario;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object obj = new Object();
                obj =  mGuardarRegistro( this.sCodCompania,this.sCodAlmacen,this.sEtiqueta,this.sZona,this.sAndamio,this.sCoordenada
                        ,this.sKilos,this.sConos,this.sUsuario);
                modRpta=(Modelo)obj;

                obj = new Object();
                obj =  mContador( this.sCodCompania,this.sCodAlmacen,this.sCodTipoExistencia, this.sZona,  this.sAndamio,  this.sCoordenada);
                modContador=(Modelo)obj;
                if(modContador.bEstado==true)
                {
                    JSONArray  sdata= (JSONArray)modContador.obj;

                    if( sdata.length() > 0)
                    {
                        sDataContador= sdata;
                        Object objJson = sdata.get(0);
                        JSONObject mainObject = new JSONObject(objJson.toString());

                        sCONT= mainObject.get("CONT").toString();
                    }
                }
            }
            catch (Exception ee)
            {
                sRpta="(Movil): "+ee.getMessage().toString();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

            progress.dismiss();

            if(modRpta.bEstado==true)
            {
                JSONArray sdata= (JSONArray)modRpta.obj;

                if( sdata.length() >0)
                {
                    String sRp="Registro guardado correctamente!";

                    Toast.makeText(getApplicationContext(), "Inventario: " + sRp, Toast.LENGTH_SHORT).show();

                    ClearControls();
                }
                else {
                    mMsg("Error!. no se guardó el inventario. "+modRpta.sRpta, 1);
                }
            }
            else
                mMsg("Error!. "+modRpta.sRpta +" - "+sRpta, 1);

            if(modContador.bEstado==true)
            {
                if(sDataContador.length()>0)
                {
                    txtCONTADOR.setText(sCONT);
                }
            }

        }
    }


    private  Object mGuardarRegistro(String sCodCompania,String sCodAlmacen,String sEtiqueta,String sZona,String sAndamio,String sCoordenada
            ,String sKilos,String sConos,String sUsuario )
    {

        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="FAB085_mGuardarRegistro";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sEtiqueta+"/"+sZona+"/"+sAndamio+"/"+sCoordenada+"/"+sKilos+"/"+sConos+"/"+sUsuario;

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

    private  Object mGuardarRegistro_(String sCodCompania,String sCodAlmacen,String sEtiqueta,String sZona,String sAndamio,String sCoordenada
            ,String sKilos,String sConos,String sUsuario )
    {
        FAB085_GuardarRegistro_Model modgr = new FAB085_GuardarRegistro_Model();
        modgr.sCodCompania = sCodCompania;
        modgr.sCodAlmacen = sCodAlmacen;
        modgr.sEtiqueta = sEtiqueta;
        modgr. sZona = sZona;
        modgr.sAndamio = sAndamio;
        modgr.sCoordenada = sCoordenada;
        modgr.sKilo = sKilos;
        modgr.sConos = sConos;
        modgr.sUsuario = sUsuario;

        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="FAB085_mGuardarRegistro";
        String sParametros="";

        try {
            WS_Connexion_ConfigMobil ws = new WS_Connexion_ConfigMobil(this);
            String sBaseUrlServicio = ws.mObtenerCadenaConexionWS();

            objRsult = wsConsumo.mProcesarWS_Post(sBaseUrlServicio,sFuncionMetodo,modgr);

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
