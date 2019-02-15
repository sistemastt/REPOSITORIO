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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import tecnologia.apptectextil.Modelo.Modelo;


public class MainActivity_Inventario_Crudo extends AppCompatActivity {



    // ArrayAdapter<ResultLista> dp;
    private ProgressBar progressInventario;
    Spinner cboCompania;
    Spinner cboSecuencia;
    Spinner cboAlmacen;
    ImageButton btnScan;
    Button btnGuardar;
    Button btnNuevo;
    Button btnSalir;
    EditText txtPieza;
    EditText txtDescripcionArticulo;
    EditText txtUrdido;
    EditText txtTrama;
    EditText txtPasadas;
    EditText txtDibujo;
    EditText txtSecuecia;
    EditText txtMetroMinimo;
    EditText txtCalidad;
    EditText txtKilaje;
    EditText txtZona;
    EditText txtNivel;
    EditText txtRuma;
    String usuario;
    //TextView hiddenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__inventario__crudo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //*******************FLECHA ATRAS******
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //*************************************


    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        //fab.setVisibility(View.GONE);

        this.ConfigureControls();


        txtPieza.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP && txtPieza.length() > 0) {
                        Toast.makeText(getApplicationContext(), "Escaneado:" + txtPieza.getText(), Toast.LENGTH_SHORT).show();
                        mProcesarScan();
                        // new  LecturaPiezaTask().execute(100);
                    }
                    return true;
                }
                return false;
              /*  if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    mProcesarScan();
                }
                return true;*/
            }
        });



        txtZona.requestFocus();

        cboSecuencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("AQUIII", "TextView Value before spinner change: " + hiddenTextView.getText());

                // LeerPiezaxSecuencia();

                //hiddenTextView.setText(cboSecuencia.getSelectedItem().toString());
                //Log.d("AQUIII", "TextView Value after spinner change: " + hiddenTextView.getText());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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



//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

String sUsuarioAll="";

    public void ConfigureControls() {
        txtCalidad = findViewById(R.id.editCalidad);
        //txtCodigoArticulo = findViewById(R.id.editc)
        btnScan = findViewById(R.id.btnScan);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSalir = findViewById(R.id.btnSalir);
        txtPieza = findViewById(R.id.editPieza);
        txtDescripcionArticulo = findViewById(R.id.editDescripcionArticulo);
        cboCompania = findViewById(R.id.spnCompania);
        cboAlmacen = findViewById(R.id.spnAlmacen);
        btnNuevo = findViewById(R.id.btnNuevo);
        // txtCodigoArticulo = findViewById(R.id.codig);
        txtUrdido = findViewById(R.id.editUrdido);
        txtTrama = findViewById(R.id.editTrama);
        txtPasadas = findViewById(R.id.editPasada);
        txtDibujo = findViewById(R.id.editDibujo);
        txtSecuecia = findViewById(R.id.editSeccion);
        txtMetroMinimo = findViewById(R.id.editMetraje);
        txtCalidad = findViewById(R.id.editCalidad);
        txtKilaje = findViewById(R.id.editKilaje);
        cboSecuencia = findViewById(R.id.spnSecuencia);
        txtZona = findViewById(R.id.editZona);
        txtRuma = findViewById(R.id.editRuma);
        txtNivel = findViewById(R.id.editNivel);
        progressInventario = findViewById(R.id.Inventario_progress);

        sUsuarioAll = (String) getIntent().getExtras().getSerializable("Usuario");
    }



    private  void mProcesarScan()
    {


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
        String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;

        //codigoalmacen = Integer.parseInt(((ResultLista)cboAlmacen.getSelectedItem()).getCodigo());
        String sPieza = txtPieza.getText().toString();
        // String secuencia = cboSecuencia.getSelectedItem().toString();

        String sUsuario= this.sUsuarioAll;
        //  this.promptSpeechInput2();
        if (sPieza.length() > 0) {
            LeerPieza(sCodCompania,sCodAlmacen,sPieza,sUsuario);
        } else {
            //showError("N° de Pieza no debe de estar vacio");
            this.mMsg("N° de Pieza no debe de estar vacio",1);
            txtPieza.requestFocus();
        }
    }

    public void onClick_btnScan(View v) {

   this.mProcesarScan();

    }


    public void onClick_btnSalir(View v) {

       final String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
        final String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;

        final String sPieza = txtPieza.getText().toString();
        final String secuencia = ((Item)cboSecuencia.getSelectedItem())._id;

        final  String sUsuario= this.sUsuarioAll;

       /* if (cboSecuencia.getSelectedItem() != null) {
            secuencia = cboSecuencia.getSelectedItem().toString();
        }*/

        String sRpta = mAviso();
        if(!sRpta.equals(""))
        {

            if (!txtPieza.getText().toString().trim().isEmpty() && !secuencia.isEmpty()) {


                AlertDialog alert = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Advertencia")
                        .setMessage("Desea salir del sistema?. Se desbloqueará su articulo de inventario.")
                        .setIcon(R.drawable.ic_advertencia)//((bTipo) ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_mood_bad_black_24dp)
                        .setCancelable(false)
                        .setPositiveButton("ACEPTAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        BloqueoRegistro(sCodCompania,sCodAlmacen,sPieza,sUsuario,secuencia,false);

 //                                       finish();
                                        txtPieza.setEnabled(true);
                                        btnScan.setEnabled(true);
                                        LimpiarControls();
                                        txtPieza.requestFocus();
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
        }
        else
        {

            txtPieza.setEnabled(true);
            btnScan.setEnabled(true);
            LimpiarControls();
            txtPieza.requestFocus();

        }


    }


    String  sCodAlmacen = "",sCodCompania="",secuencia="";
    public void onClick_btnNuevo(View v) {

        if(cboCompania.getCount()>0)
            sCodCompania = ((Item)cboCompania.getSelectedItem())._id;

        if(cboAlmacen.getCount()>0)
             sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;

        if(cboSecuencia.getCount()>0)
         secuencia = ((Item)cboSecuencia.getSelectedItem())._id;

        final String sPieza = txtPieza.getText().toString();

        final  String sUsuario= this.sUsuarioAll;

        String sRpta = mAviso();
        if(!sRpta.equals(""))
        {
            if (!txtPieza.getText().toString().trim().isEmpty() && !secuencia.isEmpty()) {

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

                                        BloqueoRegistro(sCodCompania,sCodAlmacen,sPieza,sUsuario,secuencia,false);


                                        txtPieza.setEnabled(true);
                                        btnScan.setEnabled(true);
                                        LimpiarControls();
                                        txtPieza.requestFocus();
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
        }
        else
        {

            txtPieza.setEnabled(true);
            btnScan.setEnabled(true);
            LimpiarControls();
            txtPieza.requestFocus();
        }

    }


    public void onClick_btnGuardar(View v) {

        final String sCodCompania = ((Item)cboCompania.getSelectedItem())._id;
        final String  sCodAlmacen = ((Item)cboAlmacen.getSelectedItem())._id;
        final String sPieza = txtPieza.getText().toString();

        final String secuencia = ((Item)cboSecuencia.getSelectedItem())._id;
        final  String sUsuario= this.sUsuarioAll;

        final String   sZona = txtZona.getText().toString();
        final String   sRuma = txtRuma.getText().toString();
        final String   sNivel = txtNivel.getText().toString();

        if (isValido()) {

            GuardarPieza(sCodCompania,sCodAlmacen,sPieza,secuencia,sUsuario,sZona,sRuma,sNivel);
        }

    }
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
private void LimpiarControls() {
    txtCalidad.setText("");
    txtSecuecia.setText("");
    txtMetroMinimo.setText("");
    txtPasadas.setText("");
    txtTrama.setText("");
    txtDibujo.setText("");
    txtUrdido.setText("");
    txtKilaje.setText("");
    txtDescripcionArticulo.setText("");
    txtPieza.setText("");
  //  cboSecuencia.setAdapter(null);
}

    private String mAviso()
    {
        String bRpta="";

        String  sPieza = this.txtPieza.getText().toString().trim();
        String  sDescArt = this.txtDescripcionArticulo.getText().toString().trim();

        if (sPieza.trim().isEmpty()==false && sDescArt.trim().isEmpty()==false) {
            //  this.mMsg("la Columna es un campo requerido!",2);
            bRpta="El articulo esta siendo utilizado temporalmente en el sistema por usted.\nSi Acepta se desbloqueará este registro!";
            //txtColumna.requestFocus();
        }

        return  bRpta;
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
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
    }

    private void LeerPieza(String sCodCompania,String sCodAlmacen,String sPieza,String sUsuario) {

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
            new MyTask_mProcesaPieza(progressDialog, this,sCodCompania, sCodAlmacen,sPieza,sUsuario).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }

       /* try {
            ResponsePieza objeto = getResponsePieza();

            if (objeto.getLeerPiezaResult().getMensaje().equals("OK")) {

                if (LoadSecuencias()) {
                    txtPieza.setEnabled(false);
                    btnScan.setEnabled(false);

                    isNullRegistro();
                    if (UtilizaRegistro()) {
                        BloqueoRegistro(true);
                        LoadPieza(objeto.getLeerPiezaResult().getData());
                    }
                } else {
                    showError("Nº Pieza ingresado: 1º No existe ó 2º Ya fue guardado");
                    LimpiarControls();
                    txtPieza.setText("");
                    txtPieza.requestFocus();
                    return;
                }
            }
        } catch (Exception e) {
            Log.i("VALORES", "HOORRRRORRESS");
            e.printStackTrace();
        }*/


    }

    private void  BloqueoRegistro(String sCodCompania,String sCodAlmacen,String sPieza,String sUsuario,String secuencia,boolean bBloqueo) {

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
            new MyTask_mBloqueoRegistro(progressDialog, this,sCodCompania,sCodAlmacen,sPieza,sUsuario,secuencia,bBloqueo).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }



    }


    private void  GuardarPieza(String sCodCompania, String sCodAlmacen,String pieza, String secuencia, String pcname,
                               String sZOna, String sRuma, String sNivel) {

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
            new MyTask_mGuardarPieza(progressDialog, this,sCodCompania,sCodAlmacen,pieza,secuencia,pcname,sZOna,sRuma,sNivel).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
    }




    private boolean isValido() {
        String pieza, zona, ruma, nivel, secuencia,uitxtDescripcionArticulo;
        boolean retorno = false;
        pieza = txtPieza.getText().toString();
        zona = txtZona.getText().toString();
        ruma = txtRuma.getText().toString();
        nivel = txtNivel.getText().toString();

        uitxtDescripcionArticulo = this.txtDescripcionArticulo.getText().toString();

        if (cboSecuencia.getSelectedItem() != null)
            secuencia = cboSecuencia.getSelectedItem().toString();
        else
            secuencia = "";

        if (uitxtDescripcionArticulo.trim().isEmpty()) {
            this.mMsg("No ha buscado ninguna Pieza!",1);
            txtPieza.requestFocus();
        } else if (pieza.trim().isEmpty()) {
            this.mMsg("Debes de ingresar una pieza!",1);
            txtPieza.requestFocus();
        } else if (zona.trim().isEmpty()) {
            this.mMsg("Debes de ingresar una zona!",1);
            txtZona.requestFocus();
        } else if (ruma.trim().isEmpty()) {
            this.mMsg("Debes de ingresar una ruma!",1);
            txtRuma.requestFocus();
        } else if (nivel.trim().isEmpty()) {
            this.mMsg("Debes de ingresar una nivel!",1);
            txtNivel.requestFocus();
        } else if (secuencia.trim().isEmpty()) {
            this.mMsg("Debes de elegir una secuencia!",1);
            cboSecuencia.requestFocus();
        } else {
            retorno = true;
        }
        return retorno;
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

            String ss= ex.getMessage();
        }

/*
        adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

        mySpinner = (Spinner) findViewById(R.id.spnAlmacen);

        mySpinner.setAdapter(adapter);*/
        //  this.uicmbDEP.setOnItemSelectedListener(this);
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

    private void mCargar_Secuencia(JSONArray sdata)
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
                items[i].setId(mainObject.getString("SPZRC1"));
                items[i].setName( mainObject.getString("SPZRC1") );
            }

            adapter = new SpinAdapter(this,    android.R.layout.simple_spinner_item,   items);

            mySpinner = (Spinner) findViewById(R.id.spnSecuencia);

            mySpinner.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            String ss= ex.getMessage();
            this.mMsg("Error: "+ss,1);
        }
    }

    private void mAutocompletar(JSONArray sdata)
    {
        // String selectedVal = getResources().getStringArray(R.array.values)[cboAlmacen.getSelectedItemPosition()];

        int iVal = sdata.length();

        try
        {
            Object objJson=null;
            for (int i=0; i < iVal; i++) {
                objJson = sdata.get(i);
                JSONObject mainObject = new JSONObject(objJson.toString());
                String snumeropieza= mainObject.getString("NPZRC1");
                String secuenciapieza= mainObject.getString("SPZRC1");
                String titulourdido= mainObject.getString("TURCRU");
                String titulotrama= mainObject.getString("TTRCRU");
                String numeropasada= mainObject.getString("PASCRU");
                String dibujo= mainObject.getString("DIBCRU");
                String secuencia= mainObject.getString("SECCRU");
                String codigocalidad= mainObject.getString("CODCAL");
                String codigoarticulo= mainObject.getString("CODART");
                String descripcionarticulo= mainObject.getString("DESARC");
                String metrorollo= mainObject.getString("METMIC");
                String kilajerollo= mainObject.getString("KILMIC");


                txtUrdido.setText(titulourdido);
                txtPasadas.setText(numeropasada);
                txtTrama.setText(titulotrama);
                txtDibujo.setText(dibujo);
                txtSecuecia.setText(secuencia);
                txtCalidad.setText(codigocalidad);
                txtDescripcionArticulo.setText(descripcionarticulo);
                txtMetroMinimo.setText(metrorollo);
                txtKilaje.setText(kilajerollo);

                break;
            }

        }
        catch (Exception ex)
        {
            String ss= ex.getMessage();
            this.mMsg("Error: "+ss,1);
        }
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
   // RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.
    //http://localhost:8092/Api/AppTecnologiaTextil/mLogin/JEFSIST/76CC1E1C71
    //String baseUrl = "http://192.168.166.61:8092/Api/AppTecnologiaTextil/";  // This is the API base URL (GitHub API)
   // String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.

  // HttpURLConnection urlConnection;


    public class MyTask_mObtenerInfoSistema extends AsyncTask<Void, Void, Void> {

        Modelo modAlmacen = new Modelo();
        Modelo modCompania = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_Crudo act;

        String sRpta="";

        public MyTask_mObtenerInfoSistema(ProgressDialog progress, MainActivity_Inventario_Crudo act) {
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

        String sFuncionMetodo="mObtenerAlmacen";
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


    private  Object mObtenerCompania()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mObtenerCompania";
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







    public class MyTask_mProcesaPieza extends AsyncTask<Void, Void, Void> {

        boolean bRpta=false;
        Modelo modRegistroGuardado = new Modelo();
        Modelo modProcesaPieza = new Modelo();
        Modelo modSecuencia = new Modelo();
        Modelo modIsnullregistro = new Modelo();
        Modelo modUtilizaRegistro = new Modelo();
        Modelo modBloqueoregistro = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_Crudo act;

        String sRpta="",sCodCompania="",  sCodAlmacen="", sPieza="",sUsuario="";

        public MyTask_mProcesaPieza(ProgressDialog progress, MainActivity_Inventario_Crudo act,
                                    String sCodCompania,String sCodAlmacen,String sPieza,String sUsuario) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania=sCodCompania;
            this.sCodAlmacen=sCodAlmacen;
            this.sPieza=sPieza;
            this.sUsuario=sUsuario;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object objJson=null;
                JSONObject mainObject=null;

                Object obj = new Object();

                obj =   mRegistroGuardado(this.sCodCompania, this.sCodAlmacen,this.sPieza);
                modRegistroGuardado=(Modelo)obj;
                JSONArray sdataPieza= (JSONArray)modRegistroGuardado.obj;
                if( sdataPieza.length() >0)
                {
                    objJson = sdataPieza.get(0);
                    mainObject = new JSONObject(objJson.toString());
                    String sPCNAME= mainObject.get("PCNAME").toString();

                    sRpta="Esta pieza ya fue inventariada por: "+sPCNAME+" \n";
                    return null;
                }
                //-------
                obj =  mProcesarPieza(this.sCodCompania, this.sCodAlmacen,this.sPieza);
                modProcesaPieza=(Modelo)obj;
                sdataPieza= (JSONArray)modProcesaPieza.obj;
                if( sdataPieza.length() >0)
                {
                    obj = new Object();
                    obj =  mObtenerSecuencia(this.sCodCompania, this.sCodAlmacen,this.sPieza);
                    modSecuencia=(Modelo)obj;
                    sdataPieza= (JSONArray)modSecuencia.obj;

                    if( sdataPieza.length() >0)
                    {
                        objJson = sdataPieza.get(0);
                        mainObject = new JSONObject(objJson.toString());
                        String sSecuencia= mainObject.get("SPZRC1").toString();

                        obj = new Object();
                        obj =  mIsNullRegistro(this.sCodCompania, this.sCodAlmacen,this.sPieza, sUsuario, sSecuencia);
                        modIsnullregistro=(Modelo)obj;
                        sdataPieza= (JSONArray)modIsnullregistro.obj;

                        if( sdataPieza.length() <= 0)
                        {
                            String sError= "Error WS: "+modIsnullregistro.sRpta+"-";
                            sRpta="No se encontró el artículo para el procedimiento. \n"+sError;
                            return null;
                        }
                        else {
                            objJson = sdataPieza.get(0);
                            mainObject = new JSONObject(objJson.toString());
                            String sPCNAME= mainObject.get("PCNAME").toString();

                            if(!this.sUsuario.equals(sPCNAME))
                            {
                                sRpta="Este articulo esta bloqueado por:. \n"+sPCNAME;
                                return null;
                            }
                        }

                            obj = new Object();
                            obj =  mUtilizaRegistro(this.sCodCompania, this.sCodAlmacen,this.sPieza, sUsuario, sSecuencia);
                            modUtilizaRegistro=(Modelo)obj;
                            sdataPieza= (JSONArray)modUtilizaRegistro.obj;
                            if( sdataPieza.length() >0)
                            {
                                bRpta=true;

                                obj = new Object();
                                obj =  mBloqueoRegistro(this.sCodCompania, this.sCodAlmacen,this.sPieza, sUsuario, sSecuencia,true);
                                modBloqueoregistro=(Modelo)obj;
                                sdataPieza= (JSONArray)modBloqueoregistro.obj;
                                if( sdataPieza.length() >0)
                                {
                                    sRpta= "Registro Bloqueado Temporalmente!. ";
                                }
                                else
                                {
                                    bRpta=false;
                                    sRpta= "Error: No se pudo bloquear registro. "+modProcesaPieza.sRpta;
                                }
                            }
                            else
                            {
                                bRpta=false;
                                String sError= "Error WS: "+modUtilizaRegistro.sRpta+"-";
                                sRpta="N° de Rollo esta siendo utilizado por otro dispositivo. \n "+sError;
                            }
                    }
                    else
                        sRpta= "No se obtuvo secuencia - "+modProcesaPieza.sRpta;
                }
                else
                   sRpta= "N° Pieza no existe, ó ya fue inventariado. verifique en la seccion muestras "+modProcesaPieza.sRpta;

            }
            catch (Exception ee)
            {
                sRpta="(Movil): "+ee.getMessage().toString();
            }

            return null;
        }

        public void onPostExecute(Void unused) {

            progress.dismiss();

            if(modRegistroGuardado.bEstado==true)
            {
                if(modProcesaPieza.bEstado==true) {
                    if (bRpta) {
                        JSONArray sdata = (JSONArray) modProcesaPieza.obj;
                        mAutocompletar(sdata);

                        sdata = (JSONArray) modSecuencia.obj;
                        mCargar_Secuencia(sdata);

                        //   mMsg( sRpta, 0);
                        Toast.makeText(getApplicationContext(), "Inventario: " + this.sRpta, Toast.LENGTH_SHORT).show();

                        txtPieza.setEnabled(false);
                        btnScan.setEnabled(false);
                    } else {
                        mMsg("Error!. " + sRpta, 1);
                        txtPieza.setText("");
                    }
                }
                else {
                    mMsg("Error!. " + modProcesaPieza.sRpta + sRpta, 1);
                    txtPieza.setText("");
                }
            }
            else {
                mMsg("Error!. " + modRegistroGuardado.sRpta + sRpta, 1);
                txtPieza.setText("");
            }

            /*if(modProcesaPieza.bEstado==true)
            {
                if(bRpta)
                {
                    JSONArray sdata= (JSONArray)modProcesaPieza.obj;
                    mAutocompletar(sdata);

                    sdata= (JSONArray)modSecuencia.obj;
                    mCargar_Secuencia(sdata);

                 //   mMsg( sRpta, 0);
                    Toast.makeText(getApplicationContext(), "Inventario: " + this.sRpta, Toast.LENGTH_SHORT).show();

                    txtPieza.setEnabled(false);
                    btnScan.setEnabled(false);
                }
                else
                {
                    mMsg("Error!. " + sRpta, 1);
                    txtPieza.setText("");
                }
            }
            else {
                mMsg("Error!. " + modProcesaPieza.sRpta + sRpta, 1);
                txtPieza.setText("");
            }*/
        }
    }



    private  Object mRegistroGuardado(String sCodCompania, String sCodAlmacen,String sPieza)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mRegistroGuardado";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sPieza;

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


    private  Object mProcesarPieza(String sCodCompania, String sCodAlmacen,String sPieza)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mLeerPieza";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sPieza;

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


    private  Object mObtenerSecuencia(String sCodCompania, String sCodAlmacen,String sPieza)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mSecuencia";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sPieza;

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

    private  Object mIsNullRegistro(String sCodCompania, String sCodAlmacen,String sPieza
                                    , String sUsuario, String secuencia)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mIsNullRegistro";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sPieza+"/"+sUsuario+"/"+secuencia;


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


    private  Object mUtilizaRegistro(String sCodCompania, String sCodAlmacen,String sPieza
            , String sUsuario, String secuencia)
    {

        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mUtilizaRegistro";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sPieza+"/"+sUsuario+"/"+secuencia;

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






    public class MyTask_mBloqueoRegistro extends AsyncTask<Void, Void, Void> {

        boolean bRpta=false;
        Modelo modRpta = new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_Crudo act;

        String sRpta="",sCodCompania="",  sCodAlmacen="", sPieza="",sUsuario="",secuencia="";
boolean bBloqueo=false;


        public MyTask_mBloqueoRegistro(ProgressDialog progress, MainActivity_Inventario_Crudo act,
                                       String sCodCompania, String sCodAlmacen,String sPieza, String sUsuario, String secuencia,boolean bBloqueo) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania=sCodCompania;
            this.sCodAlmacen=sCodAlmacen;
            this.sPieza=sPieza;
            this.sUsuario=sUsuario;
            this.secuencia=secuencia;
            this.bBloqueo=bBloqueo;

        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object obj = new Object();
                obj= mBloqueoRegistro(sCodCompania,  sCodAlmacen, sPieza,  sUsuario,  secuencia, bBloqueo);
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
                mMsg("Error!."+   bBloqueo+modRpta.sRpta +" - "+sRpta, 1);

        }
    }

    private  Object mBloqueoRegistro(String sCodCompania, String sCodAlmacen,String sPieza, String sUsuario, String secuencia,boolean bBloqueo)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mBloqueoRegistro";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+sPieza+"/"+sUsuario+"/"+secuencia+"/"+bBloqueo;

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
        //    urlConnection.disconnect();
        }

        return objRsult;
    }



    public class MyTask_mGuardarPieza extends AsyncTask<Void, Void, Void> {

        boolean bRpta=false;
        Modelo modRpta = new Modelo();
        Modelo modBloqueReg= new Modelo();

        ProgressDialog progress;
        MainActivity_Inventario_Crudo act;

        String sRpta="",sCodCompania="",  sCodAlmacen="", sPieza="",sUsuario="",secuencia="",sZOna="",sRuma="",sNivel="";



        public MyTask_mGuardarPieza(ProgressDialog progress, MainActivity_Inventario_Crudo act,
                                    String sCodCompania, String sCodAlmacen,String pieza, String secuencia, String pcname,
                                    String sZOna, String sRuma, String sNivel) {
            this.progress = progress;
            this.act = act;
            this.sCodCompania=sCodCompania;
            this.sCodAlmacen=sCodAlmacen;
            this.sPieza=pieza;
            this.secuencia=secuencia;
            this.sUsuario=pcname;
            this.sZOna=sZOna;
            this.sRuma=sRuma;
            this.sNivel=sNivel;


        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Object obj = new Object();
                obj= mGuardarPieza( sCodCompania,  sCodAlmacen, sPieza,  secuencia,  sUsuario,     sZOna,  sRuma,  sNivel);
                modRpta=(Modelo)obj;

                JSONArray sdata= (JSONArray)modRpta.obj;
                if( sdata.length() >0)//Guardado?
                {
                    obj = new Object();
                    obj =  mBloqueoRegistro(sCodCompania, sCodAlmacen,sPieza, sUsuario, secuencia, false);
                    modBloqueReg=(Modelo)obj;
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
                        sRp= sRp+", pero no se puedo Desbloquear el Articulo: "+ sPieza;
                    }

                    if(i)
                    {
                        Toast.makeText(getApplicationContext(), "Inventario: " + sRp, Toast.LENGTH_SHORT).show();


                        txtPieza.setEnabled(true);
                        btnScan.setEnabled(true);
                        txtPieza.requestFocus();
                        LimpiarControls();
                    }
                    else
                        mMsg(sRp+sRpAd, 2);
                }
                else {
                    mMsg("Error!. no se guardó el inventario. "+modRpta.sRpta, 1);
                }
            }
            else
                mMsg("Error!. "+modRpta.sRpta +" - "+sRpta, 1);

        }
    }

    private  Object mGuardarPieza(String sCodCompania, String sCodAlmacen,String pieza, String secuencia, String pcname,
                                  String sZOna, String sRuma, String sNivel)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mGuardarPieza";
        String sParametros="/"+sCodCompania+"/"+sCodAlmacen+"/"+pieza+"/"+secuencia+"/"+pcname+"/"+sZOna+"/"+sRuma+"/"+sNivel;

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

    public class SpinAdapter extends ArrayAdapter<Item>{

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
