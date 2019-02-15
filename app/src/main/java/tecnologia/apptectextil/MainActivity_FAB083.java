package tecnologia.apptectextil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_FAB083 extends AppCompatActivity {

    adapter_menu adapter_menu_;
    ListView listaEventosOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__fab083);
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
        });
        */


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



    private  void mUI()
    {

        this.listaEventosOS = (ListView) findViewById(R.id.lvleventosOS);

        this.mListasEventos();

    }


    private void mListasEventos() {
        List<List<String>> listRpta = new ArrayList<List<String>>();


        List<String> listado = new ArrayList<String>();

        listado = new ArrayList<String>();
        listado.add("Alta");
        listado.add("Descripcion 1");
        listado.add("0");
        listRpta.add(0, listado);
        listado = new ArrayList<String>();
        listado.add("Baja");
        listado.add("Descripcion 2");
        listado.add("1");
        listRpta.add(1, listado);
        listado = new ArrayList<String>();
        listado.add("Anulacion");
        listado.add("Descripcion 3");
        listado.add("2");
        listRpta.add(1, listado);

        this.mLlenarlistadoOS(listRpta);

    }



    private void mLlenarlistadoOS(List<List<String>> listDatos) {
        int iColumnas = listDatos.get(0).size();
        int iFilas = listDatos.size();

        int[] imagenes = new int[iFilas];
        String[] evento = new String[iFilas];
        String[] evento_det = new String[iFilas];
        String[] evento_id = new String[iFilas];

        int a, b;

        for (a = 0; a < iColumnas; a++) {
            for (b = 0; b < iFilas; b++) {

                if (a == 0) {
                    if (b == 0)
                        imagenes[b] = R.mipmap.alta1;
                    if (b == 1)
                        imagenes[b] = R.mipmap.control;
                    if (b == 2)
                        imagenes[b] = R.mipmap.anulacion;

                    evento[b] = listDatos.get(b).get(a).toString();
                }
                if (a == 1) {
                    evento_det[b] = listDatos.get(b).get(a).toString();
                }
                if (a == 2) {
                    evento_id[b] = listDatos.get(b).get(a).toString();
                }
            }
        }

        this.adapter_menu_ = new adapter_menu(this, imagenes, evento, evento_det,evento_id);

        this.listaEventosOS.setAdapter(this.adapter_menu_ );
    }



}
