package tecnologia.apptectextil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_PrepPartidas_CTC015 extends AppCompatActivity {

    adapter_lista_menu adapter_lista_menu_;
    ListView listaEventosOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__prep_partidas__ctc015);
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


        this.listaEventosOS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                view.setSelected(true);

                ImageView imgImg;
                TextView uilblEVENTO_ID;
                uilblEVENTO_ID = (TextView) view.findViewById(R.id.lblEVENTO_ID);
                String sEventoOS_ID = uilblEVENTO_ID.getText().toString();
                mLlamarEventos(sEventoOS_ID);

            }
        });
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

        this.listaEventosOS = (ListView) findViewById(R.id.lvleventos);

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
        listado.add("Cambio");
        listado.add("Descripcion 3");
        listado.add("2");
        listRpta.add(2, listado);

        mLlenarlistadoOS(listRpta);

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
                        imagenes[b] = R.mipmap.anulacion;
                    if (b == 2)
                        imagenes[b] = R.mipmap.cambio;

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

        this.adapter_lista_menu_ = new adapter_lista_menu(this, imagenes, evento, evento_det,evento_id);

        this.listaEventosOS.setAdapter(this.adapter_lista_menu_);
    }

    private  void  mLlamarEventos(String sIdEvento)
    {
        Intent intent;

     /*   if (sIdEvento.equals("0")) {

            intent = new Intent(this, MainActivity_Tejeduria_Cortes_Proceso.class);
            // intent.putExtra("Usuario",  sUsuarioAll);
            startActivity(intent);
        }
        if (sIdEvento.equals("1")) {
            intent = new Intent(this, MainActivity_Tejeduria_Cortes_Consultas.class);
            //intent.putExtra("Usuario",  sUsuarioAll);
            startActivity(intent);
        }*/
    }

}
