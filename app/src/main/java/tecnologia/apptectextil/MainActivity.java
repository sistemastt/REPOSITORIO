package tecnologia.apptectextil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String sUsuarioAll="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        sUsuarioAll = (String) getIntent().getExtras().getSerializable("Usuario");

        //:::::::::::::::::::::INICIAR CON FRAGMENT INICIO:::::::::::::::::::::::::::::::::
        Fragment fragment = null;
        fragment = new Fragment_Inicio();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        // menuItem.setChecked(true);
        getSupportActionBar().setTitle("INICIO");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        switch (item.getItemId()) {
            case R.id.action_versionactual:
                break;

            case R.id.action_descargar_drive:
                Intent myIntent = new Intent(this, WebViewActivity.class);
                startActivity(myIntent);
                break;
            case R.id.action_configuracion:
                myIntent = new Intent(this, MainActivity_Configuracion.class);
                startActivity(myIntent);
                break;
        }
        return false;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        NavigationView  navigationView = (NavigationView) findViewById(R.id.nav_view);
        Intent intent;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        boolean fragmentTransaction = false;

        switch (id) {
            case R.id.nav_inicio:
               // fragmentTransaction = true;
                break;


            case R.id.nav_inventario_crudo:
                 intent = new Intent(this, MainActivity_Inventario_Crudo.class);
                intent.putExtra("Usuario",  sUsuarioAll);
                startActivity(intent);
                // this.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;

            case R.id.nav_inventario_prod_terminado:
                 intent = new Intent(this, MainActivity_Inventario_ProdTerminado.class);
                intent.putExtra("Usuario",  sUsuarioAll);
                startActivity(intent);
                //this.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;

            case R.id.nav_inventario_fab085:
                intent = new Intent(this, MainActivity_Inventario_FAB085.class);
                intent.putExtra("Usuario",  sUsuarioAll);
                startActivity(intent);
                //this.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;



            case R.id.nav_tejeduria_corte:
                intent = new Intent(this, MainActivity_Tejeduria_cortes.class);
                startActivity(intent);
                // this.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;

            case R.id.nav_preppartidas_ctc015:
                intent = new Intent(this, MainActivity_PrepPartidas_CTC015.class);
                startActivity(intent);
                // this.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;

            case R.id.nav_muestras:
                intent = new Intent(this, MainActivity_Muestras.class);
                startActivity(intent);
                // this.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;

            case R.id.nav_procesos_fab083:
                intent = new Intent(this, MainActivity_FAB083.class);
                startActivity(intent);
                //this.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                break;
        }


        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


}
