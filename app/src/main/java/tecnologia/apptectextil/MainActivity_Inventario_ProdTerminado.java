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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tecnologia.apptectextil.Modelo.Modelo;

public class MainActivity_Inventario_ProdTerminado extends AppCompatActivity {


    String sUsuarioAll = "";

    ImageButton btnScan;

    ImageButton btnBuscar;
    Button btnSalir;
    Button btnGuardar;
    Button btnNuevo;

    EditText txtRollo;
    EditText txtdesArticulo;
    EditText txtcodArticulo;
    EditText txtdesColor;
    EditText txtcodColor;
    EditText txtColumna;
    EditText txtFila;
    EditText txtFondo;
    EditText txtContador;
    EditText txtCombinacion;
    EditText txtCuadro;
    EditText txtMetraje;
    EditText txtCalidad;

    Spinner cboCompania;
    Spinner cboAlmacen;
    //ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__inventario__prod_terminado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //*******************FLECHA ATRAS******
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //*************************************

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        this.mUI();

        txtRollo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
              /*  if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(getApplicationContext(), "Escaneado:" + txtRollo.getText(), Toast.LENGTH_SHORT).show();
                    mProcesarBusqueda();
                    //return true;
                }
                return true;*/
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP && txtRollo.length() > 0) {
                        Toast.makeText(getApplicationContext(), "Escaneado:" + txtRollo.getText(), Toast.LENGTH_SHORT).show();
                        mProcesarBusqueda();
                        // new  LecturaPiezaTask().execute(100);
                    }
                    return true;
                }
                return false;
            }
        });


        if (this.mConectado_WIFI_DATOS() != 0) {

            this.mObtenerInfoSistema();

        } else {
            mMsg("Ups.. Paquete de DATOS o WIFFI ´´DESACTIVADOS´´", 1);
        }

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
                //  this.mMsg("Scanned",2);
            } else {
                String sRptaScan = result.getContents();
                //mMostrarVentanaBusqueda(sImei);
                //this.mMsg("Scanned",2);
                this.txtRollo.setText(sRptaScan.trim());

                Toast.makeText(this, "Producto Scaneado: " + sRptaScan, Toast.LENGTH_LONG).show();

                this.mProcesarBusqueda();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void mUI() {
        sUsuarioAll = (String) getIntent().getExtras().getSerializable("Usuario");

        btnScan = findViewById(R.id.btnScan);
        Log.i("FRAGMENTO", "SE CAYO");
        btnBuscar = findViewById(R.id.btnBuscar);
        btnSalir = findViewById(R.id.btnSalir);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnNuevo = findViewById(R.id.btnNuevo);
        Log.i("FRAGMENTO", "CONTINUO");
        txtRollo = findViewById(R.id.editRollo);
        txtdesArticulo = findViewById(R.id.editDescripcionArticulo);
        txtcodArticulo = findViewById(R.id.editCodigoArticulo);
        txtdesColor = findViewById(R.id.editDescripcionColor);
        txtColumna = findViewById(R.id.editColumna);
        txtFila = findViewById(R.id.editFila);
        txtFondo = findViewById(R.id.editFondo);
        txtContador = findViewById(R.id.editContador);
        txtCombinacion = findViewById(R.id.editCombinacion);
        txtCuadro = findViewById(R.id.editCuadro);
        txtMetraje = findViewById(R.id.editMetraje);
        txtCalidad = findViewById(R.id.editCalidad);
        txtcodColor = findViewById(R.id.editCodigoColor);

        cboCompania = findViewById(R.id.spnCompania);
        cboAlmacen = findViewById(R.id.spnAlmacen);

        //   progress = findViewById(R.id.Inventario_progress);


    }


    private void mProcesarBusqueda()
    {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
        String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;
        String  sRollo = txtRollo.getText().toString().trim();

        String sColumna= txtColumna.getText().toString();
        String sFila= txtFila.getText().toString();
        String sFondo= txtFondo.getText().toString();

        if (isValidaUbicacion()) {
            if (isValidaLongitud())
            {
                this.mProcesarLecturaBuscar(sCodCompania,sCodAlmacen,sRollo,this.sUsuarioAll,sColumna,sFila,sFondo);
            }
        }

     //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

       this.mProcesarBusqueda();
    }

    public void onClick_btnNuevo(View v) {

        String sRpta = mAviso();
        if(!sRpta.equals(""))
        {
            AlertDialog alert = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Advertencia")
                    .setMessage(sRpta)
                    .setIcon(R.drawable.ic_advertencia)//((bTipo) ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_mood_bad_black_24dp)
                    .setCancelable(false)
                    .setPositiveButton("ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //  listener.onPossitiveButtonClick();
                                   // finish();

                                    String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
                                    String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;
                                    String  sRollo = txtRollo.getText().toString().trim();
                                    String sColumna= txtColumna.getText().toString();
                                    String sFila= txtFila.getText().toString();
                                    String sFondo= txtFondo.getText().toString();

                                    String sUsuario = sUsuarioAll;

                                    if (!sRollo.trim().isEmpty()) {
                                        //  bloqueRegistro(false);

                                        BloqueoRegistro(sCodCompania,sCodAlmacen,sRollo,sUsuario,false);
                                    }
                                    ClearControls();
                                    txtRollo.setEnabled(true);
                                    txtRollo.setText("");
                                    txtRollo.requestFocus();
                                }
                            })
                    .setNegativeButton("CANCELAR",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //  listener.onPossitiveButtonClick();
                                    // finish();

                                }
                            });

            builder.create();
            alert = builder.create();
            alert.show();
        }
        else {
            ClearControls();
            txtRollo.setEnabled(true);
            txtRollo.setText("");
            txtRollo.requestFocus();
        }


    }

    public void onClick_btnGuardar(View v) {

        try
        {

            final String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
            final String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;
            final String  sRollo = txtRollo.getText().toString().trim();
            final String sColumna= txtColumna.getText().toString();
            final String sFila= txtFila.getText().toString();
            final String sFondo= txtFondo.getText().toString();
            final String stxtcodArticulo= txtcodArticulo.getText().toString();

            //editCodigoArticulo
            if(sRollo.equals("")||sFila.equals("")||sFondo.equals("")||sColumna.equals("")||stxtcodArticulo.equals(""))
            {
                this.mMsg("Debe ingresar parametros basicos",2);
            }
            else
            {
                GuardarRollo(sCodCompania,sCodAlmacen,sRollo,sUsuarioAll,sColumna,sFila,sFondo);
            }
        }
        catch (Exception ex)
        {

            this.mMsg("Error: "+ex.toString(),1);
        }



        /*
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Advertencia")
                .setMessage("Se registrará el inventario para el producto: "+sRollo+", deseas continuar?")
                .setIcon(R.drawable.ic_advertencia)//((bTipo) ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_mood_bad_black_24dp)
                .setCancelable(false)
                .setPositiveButton("ACEPTAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  listener.onPossitiveButtonClick();
                                // finish();


                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  listener.onPossitiveButtonClick();
                                // finish();

                            }
                        });

        builder.create();
        alert = builder.create();
        alert.show();

*/

    }
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private String mAviso()
    {
        String bRpta="";
        String sCodCompania ="",sCodAlmacen="";
        if (cboCompania.getCount() > 0)
            sCodCompania = ((Item)cboCompania.getSelectedItem())._id;

        if (cboAlmacen.getCount() > 0)
            sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;

        String  sRollo = txtRollo.getText().toString().trim();
        String  sCodArticulo = this.txtcodArticulo.getText().toString().trim();

        if (sRollo.trim().isEmpty()==false && sCodArticulo.trim().isEmpty()==false) {
          //  this.mMsg("la Columna es un campo requerido!",2);
            bRpta="El articulo esta siendo utilizado temporalmente en el sistema por usted.\nSi Acepta se desbloqueará este registro!";
            //txtColumna.requestFocus();
        }

        return  bRpta;
    }



    private void ClearControls() {
        this.txtdesArticulo.setText("");
        this.txtcodArticulo.setText("");
        this.txtcodColor.setText("");
        this.txtdesColor.setText("");
        this.txtCalidad.setText("");
        //this.txtContador.setText("");
        this.txtCuadro.setText("");
        this.txtCombinacion.setText("");
        this.txtMetraje.setText("");

    }

private boolean isValidaUbicacion() {
    boolean retorno = false;
    if (txtColumna.getText().toString().trim().isEmpty()) {
        this.mMsg("la Columna es un campo requerido!",2);
        txtColumna.requestFocus();
    } else if (txtFila.getText().toString().trim().isEmpty()) {
        this.mMsg("la Fila es un campo requerido!",2);
        txtFila.requestFocus();
    } else if (txtFondo.getText().toString().trim().isEmpty()) {
        this.mMsg("la Fondo es un campo requerido!",2);
        txtFila.requestFocus();
    } else {
        retorno = true;
    }
    return retorno;
}

    private boolean isValidaLongitud() {
        String texto = txtRollo.getText().toString().trim();
        boolean retorno = false;
        if (texto.length() == 14 ||
                texto.length() == 12) {
            return true;
        } else {
            this.mMsg("N° de Rollo no es valido \ndebe de tener una longitud de\n 12 o 14 digitos",2);
            //txtRollo.setText("");
            //txtRollo.requestFocus();
        }
        return retorno;
    }



    private  void mProcesarLecturaBuscar(String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,String sColumna,String sFila,String sFondo)
    {
        this.registroGuardado(sCodCompania,sCodAlmacen,sRollo,sUsuario,sColumna, sFila, sFondo);
    }

    private void registroGuardado(String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,String sColumna,String sFila,String sFondo) {

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
            new MyTask_mRegistroGuardado(progressDialog, this,sCodCompania,sCodAlmacen,sRollo,sUsuario,sColumna, sFila, sFondo).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }

    }


    private void mLecturaRollo(String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario)
    {
       /* try {
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
            new MyTask_mLeerRollo(progressDialog, this,sCodCompania,sCodAlmacen,sRollo,sUsuario).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }*/
    }


    private void msNullRegistro(String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario)
    {
        /*try {
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
            new MyTask_mIsNullRegistro(progressDialog, this,sCodCompania,sCodAlmacen,sRollo,sUsuario).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }*/
    }


    private void mRegistroUso(String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,JSONArray sdata) {
/*
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
            new MyTask_mRegistroEnUso(progressDialog, this,sCodCompania,sCodAlmacen,sRollo,sUsuario,sdata).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }*/

    }


    private void BloqueoRegistro(String sCodCompania,String sCodAlmacen,String sRollo,String pcname,boolean bBloqueo) {
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
            new MyTask_mBloqueRegistro(progressDialog, this,sCodCompania,sCodAlmacen,sRollo,pcname,bBloqueo).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
    }



    private void LoadContador(String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,String sColumna,String sFila,String sFondo) {
       /* try {
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
            new MyTask_mLoadContador(progressDialog, this,sCodCompania,sCodAlmacen,sRollo,sUsuario,sColumna, sFila, sFondo).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }*/
    }

    private void GuardarRollo(String sCodCompania,String sCodAlmacen,String sRollo,String pcname,String sColumna,String sFila,String sFondo ) {
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
            new MyTask_mGuardarRollo(progressDialog, this,sCodCompania,sCodAlmacen,sRollo,pcname,sColumna, sFila, sFondo).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
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
                items[i].setId(mainObject.getString("CODALM"));
                items[i].setName(mainObject.getString("DESALM"));
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


    private void mCargar_ObjetosInterfaz(JSONArray sdata)
    {
        int iVal = sdata.length();

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());
                String stxtdesArticulo= mainObject.get("ROLLO").toString();
                String sCODART= mainObject.get("CODART").toString();
                String sDESART= mainObject.get("DESART").toString();
                String sCODCOL= mainObject.get("CODCOL").toString();
                String sDESCOL= mainObject.get("DESCOL").toString();
                String sCODCOM= mainObject.get("CODCOM").toString();
                String sCODCUA= mainObject.get("CODCUA").toString();
                String sCODCAL= mainObject.get("CODCAL").toString();
                String sMETMIN= mainObject.get("METMIN").toString();
                String stxtMetraje= mainObject.get("PCNAME").toString();


                txtdesArticulo.setText(sDESART);
                txtcodArticulo.setText(sCODART);
                txtCalidad.setText(sCODCAL);
                txtdesColor.setText(sDESCOL);
                txtcodColor.setText(sCODCOL);
                txtCombinacion.setText(sCODCOM);
                txtCuadro.setText(sCODCUA);
                txtMetraje.setText(sMETMIN);
                //LoadContador();
               // txtRollo.setEnabled(false);

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


    private void mCargar_Contador(JSONArray sdata)
    {
        int iVal = sdata.length();

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {

                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());
                String sCONTADOR= mainObject.get("CONTADOR").toString();

                this.txtContador.setText(sCONTADOR);
                //LoadContador();
                // txtRollo.setEnabled(false);
                break;
            }
        }
        catch (Exception ex)
        {
            String ss= "Error: mCargar_Contador - \n"+ex.getMessage();
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

        ProgressDialog progress;
        MainActivity_Inventario_ProdTerminado act;

        String sRpta="";

        public MyTask_mObtenerInfoSistema(ProgressDialog progress, MainActivity_Inventario_ProdTerminado act) {
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
        }
    }


    private  Object mObtenerAlmacen()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_mObtenerAlmacen";
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


    private  Object mObtenerCompania()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_mObtenerCompania";
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








    public class MyTask_mRegistroGuardado extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_ProdTerminado act;

        String sRpta="",sCodAlmacen="",sCodCompania="",sRollo="",sUsuario="",sColumna="",sFila="",sFondo="";
         boolean bRpta=false;

        JSONArray sDataContador=null;
        JSONArray sDataInfoObj=null;


        public MyTask_mRegistroGuardado(ProgressDialog progress, MainActivity_Inventario_ProdTerminado act
                ,String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,String sColumna,String sFila,String sFondo) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania = sCodCompania;
            this.sCodAlmacen = sCodAlmacen;
            this.sRollo = sRollo;
            this.sUsuario=sUsuario;
            this.sColumna=sColumna;
            this.sFila=sFila;
            this.sFondo=sFondo;


        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Object obj = new Object();
                obj =  mRegistroGuardado( this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario);
                modRpta=(Modelo)obj;

                if(modRpta.bEstado==true)
                {
                    JSONArray sdata= (JSONArray)modRpta.obj;
                    if( sdata.length() > 0)
                    {
                        //mMsg("Error: El N° de Rollo ya fue Guardado",1);
                       /* txtRollo.setText("");
                        txtRollo.requestFocus();*/

                        bRpta=false;

                        String sError= "Error WS: "+modRpta.sRpta+"-";
                        sRpta="El N° de Rollo ya fue Guardado. \n"+sError;
                        return null;
                    }

                    obj = new Object();
                    obj= mIsNullRegistro(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario);
                    modRpta=(Modelo)obj;

                     sdata= (JSONArray)modRpta.obj;

                    if( sdata.length() <= 0)
                    {
                        bRpta=false;
                        String sError= "Error WS: "+modRpta.sRpta+"-";
                        sRpta="No se encontró el artículo para el procedimiento. \n"+sError;
                        return null;
                    }
                   else {
                        Object objJson = sdata.get(0);
                        JSONObject mainObject = new JSONObject(objJson.toString());
                        String sPCNAME= mainObject.get("PCNAME").toString();

                        if(!this.sUsuario.equals(sPCNAME))
                        {
                            bRpta=false;
                            //String sError= "Error WS: "+modRpta.sRpta+"-";
                            sRpta="Este articulo esta bloqueado por:. \n"+sPCNAME;
                            return null;
                        }
                    }



                    obj = new Object();
                    obj=  mLeerRollo( this.sCodCompania, this.sCodAlmacen, this.sRollo );
                    modRpta=(Modelo)obj;

                    sdata= (JSONArray)modRpta.obj;

                    if( sdata.length() >0)
                    {
                        sDataInfoObj= sdata;

                        obj = new Object();
                        obj= mRegistroEnUso(sCodCompania, sCodAlmacen, sRollo, sUsuario);
                        modRpta=(Modelo)obj;

                        sdata= (JSONArray)modRpta.obj;
                        if( sdata.length() >0)
                        {
                            bRpta=true;

                            /*
                            Object objJson = sdata.get(0);
                            JSONObject mainObject = new JSONObject(objJson.toString());
                            String sPCNAME= mainObject.get("PCNAME").toString();
                            if(this.sUsuario!=sPCNAME)
                            {
                                bRpta=false;
                                String sError= "Error WS: "+modRpta.sRpta+"-";
                                sRpta="Error!.No se realizo el Bloqueo del registro. intente nuevamente. \n"+sError;
                            }
                            */

                            obj = new Object();
                            obj= mBloqueoRegistro(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,true);
                            modRpta=(Modelo)obj;
                            sdata= (JSONArray)modRpta.obj;
                            if( sdata.length() >0)
                            {
                               // bRpta=true;
                                sRpta="Registro Bloqueado temporalmente"+modRpta.sRpta;
                            }
                            else {
                               // mMsg("Error!. Bloqueo registro. "+modRpta.sRpta, 1);
                                bRpta=false;
                                String sError= "Error WS: "+modRpta.sRpta+"-";
                                sRpta="Error!.No se realizo el Bloqueo del registro. intente nuevamente. \n"+sError;
                            }

                            obj = new Object();
                            obj= mLoadContador(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,this.sColumna, this.sFila, this.sFondo);
                            modRpta=(Modelo)obj;
                            sDataContador= (JSONArray)modRpta.obj;

                            if( sDataContador.length() <=0)
                            {
                             //   mMsg("Error!. no se ecnuentra contador para el proceso. "+modRpta.sRpta, 1);
                                bRpta=false;
                                String sError= "Error WS: "+modRpta.sRpta+"-";
                                sRpta="Error!. no se ecnuentra contador para el proceso. \n "+sError;
                            }
                        }
                        else {
                            bRpta=false;
                            String sError= "Error WS: "+modRpta.sRpta+"-";
                            sRpta="N° de Rollo esta siendo utilizado por otro dispositivo. \n "+sError;
                        }
                    }
                    else
                    {
                        bRpta=false;
                        String sError= "Error WS: "+modRpta.sRpta+"-";
                        sRpta="N° de Rollo no existe. \n"+sError;
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
                if(bRpta)
                {
                    if( sDataInfoObj.length() >0) {
                        mCargar_ObjetosInterfaz(sDataInfoObj);
                    }

                    if( sDataContador.length() >0)
                    {
                        mCargar_Contador(sDataContador);
                    }

                    Toast.makeText(getApplicationContext(), "Inventario: " + this.sRpta, Toast.LENGTH_SHORT).show();
                }
                else {
                    mMsg(this.sRpta, 1);
                    txtRollo.setText("");
                }
            }
            else
            { mMsg("Error!. "+modRpta.sRpta +sRpta, 1);
                txtRollo.setText("");}
        }
    }



    private  Object mRegistroGuardado(String sCodCompania,String sCodAlmacen,String sRollo, String sUsuario )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_RegistroGuardado";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sRollo+"/"+sUsuario;

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










    public class MyTask_mIsNullRegistro extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_ProdTerminado act;

        String sRpta="",sCodAlmacen="",sCodCompania="",sRollo="",sUsuario="";

        public MyTask_mIsNullRegistro(ProgressDialog progress, MainActivity_Inventario_ProdTerminado act
                ,String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania = sCodCompania;
            this.sCodAlmacen = sCodAlmacen;
            this.sRollo = sRollo;
            this.sUsuario=sUsuario;

        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Object obj = new Object();
                obj =  mIsNullRegistro( this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario);
                modRpta=(Modelo)obj;

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

                if( sdata.length() > 0)
                {
                   // mMsg("",0);
                   /* mMsg(""+modRpta.sRpta,1);
                    txtRollo.setText("");
                    txtRollo.requestFocus();*/
                }
                else {
                    mMsg("No se actualizo la tabla - MyTask_mIsNullRegistro"+modRpta.sRpta,2);
                    //mLecturaRollo(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario);
                }
            }
            else
                mMsg("Error!. "+modRpta.sRpta +sRpta, 1);
        }
    }


    private  Object mIsNullRegistro(String sCodCompania,String sCodAlmacen,String sRollo, String sUsuario )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_IsNullRegistro";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sRollo+"/"+sUsuario;


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



    public class MyTask_mLeerRollo extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_ProdTerminado act;

        String sRpta="",sCodAlmacen="",sCodCompania="",sRollo="",sUsuario="";

        public MyTask_mLeerRollo(ProgressDialog progress, MainActivity_Inventario_ProdTerminado act
                ,String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania = sCodCompania;
            this.sCodAlmacen = sCodAlmacen;
            this.sRollo = sRollo;
            this.sUsuario=sUsuario;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Object obj = new Object();
                obj =  mLeerRollo( this.sCodCompania,this.sCodAlmacen,this.sRollo);
                modRpta=(Modelo)obj;

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
                    //mRegistroUso(sCodCompania, sCodAlmacen, sRollo, sUsuario,sdata);
                }
                else
                {
                    mMsg("N° de Rollo no existe", 1);
                    //txtRollo.setText("");
                    //txtRollo.requestFocus();
                }
            }
            else
                mMsg("Error!. "+modRpta.sRpta +" - "+sRpta, 1);
        }
    }


    private  Object mLeerRollo(String sCodCompania,String sCodAlmacen,String sRollo )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_LeerRollo";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sRollo;


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






    public class MyTask_mRegistroEnUso extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_ProdTerminado act;
        JSONArray sdata_g=null;

        String sRpta="",sCodAlmacen="",sCodCompania="",sRollo="",sUsuario="";

        public MyTask_mRegistroEnUso(ProgressDialog progress, MainActivity_Inventario_ProdTerminado act
                ,String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,JSONArray sdata) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania = sCodCompania;
            this.sCodAlmacen = sCodAlmacen;
            this.sRollo = sRollo;
            this.sUsuario=sUsuario;
            this.sdata_g = sdata;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object obj = new Object();
                obj =  mRegistroEnUso( this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario);
                modRpta=(Modelo)obj;
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
                    mCargar_ObjetosInterfaz(this.sdata_g);

                    String sColumna= txtColumna.getText().toString();
                    String sFila= txtFila.getText().toString();
                    String sFondo= txtFondo.getText().toString();

                  //  BloqueoRegistro(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,true);

                   // LoadContador(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,sColumna, sFila, sFondo);
                }
                else {
                    mMsg("N° de Rollo esta siendo utilizado por otro dispositivo", 1);
                    txtRollo.setText("");
                    txtRollo.requestFocus();
                }
            }
            else
                mMsg("Error!. "+modRpta.sRpta +" - "+sRpta, 1);
        }
    }


    private  Object mRegistroEnUso(String sCodCompania,String sCodAlmacen,String sRollo,String pcname )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_UtilizaRegistro";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sRollo+"/"+pcname;

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








    public class MyTask_mBloqueRegistro extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_ProdTerminado act;

        String sRpta="",sCodAlmacen="",sCodCompania="",sRollo="",sUsuario="";
        boolean bBloqueo=false;

        public MyTask_mBloqueRegistro(ProgressDialog progress, MainActivity_Inventario_ProdTerminado act
                ,String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,boolean bBloqueo) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania = sCodCompania;
            this.sCodAlmacen = sCodAlmacen;
            this.sRollo = sRollo;
            this.sUsuario=sUsuario;
            this.bBloqueo=bBloqueo;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object obj = new Object();
                obj =  mBloqueoRegistro( this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,this.bBloqueo);
                modRpta=(Modelo)obj;
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
                    if(this.bBloqueo)
                        mMsg("Registro Bloqueado temporalmente! "+modRpta.sRpta, 0);
                    else
                        mMsg("Registro Desbloqueado! "+modRpta.sRpta, 0);
                }
                else {
                    mMsg("Error!. Bloqueo registro. "+modRpta.sRpta, 1);
                }
            }
            else
                mMsg("Error!. "+modRpta.sRpta +" - "+sRpta, 1);
        }
    }


    private  Object mBloqueoRegistro(String sCodCompania,String sCodAlmacen,String sRollo,String pcname,boolean bBloqueo )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_BloqueaRegistro";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sRollo+"/"+pcname+"/"+bBloqueo;

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





    private  Object mLoadContador(String sCodCompania,String sCodAlmacen,String sRollo,String pcname,String sColumna,String sFila,String sFondo )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_mContadorRollo";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sColumna+"/"+sFila+"/"+sFondo;

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





    public class MyTask_mGuardarRollo extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();
        Modelo modBloqueReg = new Modelo();
        Modelo modCont = new Modelo();

        JSONArray sDataContador=null;

        ProgressDialog progress;
        MainActivity_Inventario_ProdTerminado act;

        String sRpta="",sCodAlmacen="",sCodCompania="",sRollo="",sUsuario="",sColumna="",sFila="",sFondo="";

        public MyTask_mGuardarRollo(ProgressDialog progress, MainActivity_Inventario_ProdTerminado act
                ,String sCodCompania,String sCodAlmacen,String sRollo,String sUsuario,String sColumna,String sFila,String sFondo) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania = sCodCompania;
            this.sCodAlmacen = sCodAlmacen;
            this.sRollo = sRollo;
            this.sUsuario=sUsuario;

            this.sColumna=sColumna;
            this.sFila=sFila;
            this.sFondo=sFondo;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object obj = new Object();
                obj =  mGuardarRollo( this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,this.sColumna,this.sFila,this.sFondo);
                modRpta=(Modelo)obj;
                JSONArray sdata= (JSONArray)modRpta.obj;
                if( sdata.length() >0)//Guardado?
                {
                    obj = new Object();
                    obj =  mBloqueoRegistro(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,false);
                    modBloqueReg=(Modelo)obj;
                    sdata= (JSONArray)modRpta.obj;

                    obj = new Object();
                    obj= mLoadContador(this.sCodCompania,this.sCodAlmacen,this.sRollo,this.sUsuario,this.sColumna, this.sFila, this.sFondo);
                    modCont=(Modelo)obj;
                    sDataContador= (JSONArray)modCont.obj;
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

                    boolean i=true;
                    String sRpAd=". \nAvise al administrador del sistema para regularizar el registro";
                    sdata= (JSONArray)modBloqueReg.obj;
                    if( sdata.length() <0)
                    {
                        i=false;
                        sRp= sRp+", pero no se puedo Desbloquear el Articulo: "+this.sRollo;
                    }
                   /* sdata= (JSONArray)modCont.obj;
                    if( sdata.length() <0)
                    {
                        i=false;
                        sRp= sRp+". No se pudo tener el contador de inventario.";
                    }*/

                    if(i)
                    {
                        Toast.makeText(getApplicationContext(), "Inventario: " + sRp, Toast.LENGTH_SHORT).show();

                        ClearControls();
                        txtRollo.setText("");
                        txtRollo.requestFocus();
                    }
                    else
                        mMsg(sRp+sRpAd, 2);
                }
                else {
                    mMsg("Error!. no se guardó el inventario. "+modRpta.sRpta, 1);
                }


                if(modCont.bEstado==true)
                {
                    if( sDataContador.length() >0)
                    {
                        mCargar_Contador(sDataContador);
                    }
                }
            }
            else
                mMsg("Error!. "+modRpta.sRpta +" - "+sRpta, 1);
        }
    }


    private  Object mGuardarRollo(String sCodCompania,String sCodAlmacen,String sRollo,String pcname,String sColumna,String sFila,String sFondo )
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="Ipt_mGuardarRollo";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sRollo+"/"+pcname+"/"+sColumna+"/"+sFila+"/"+sFondo;

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
