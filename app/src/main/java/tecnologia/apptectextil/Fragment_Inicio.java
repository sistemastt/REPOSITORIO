package tecnologia.apptectextil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import tecnologia.apptectextil.Modelo.Modelo;


public class Fragment_Inicio extends Fragment {


    MainActivity mMainPrincipal;
    GraphView graph;
    Spinner mySpinnerCompania,mySpinnerAlmacen;

    public Fragment_Inicio() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mMainPrincipal= ((MainActivity)getActivity());

        View view = inflater.inflate(R.layout.fragment_fragment__inicio, container, false);
        // Inflate the layout for this fragment

        this.mySpinnerCompania = (Spinner) view.findViewById(R.id.spnCompania);
        this.mySpinnerAlmacen = (Spinner) view.findViewById(R.id.spnAlmacen);
        this.graph = (GraphView) view.findViewById(R.id.graph);

        this.mySpinnerCompania.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String  sCodCompania = ((Item)mySpinnerCompania.getSelectedItem())._id;
                String  sCodAlmacen = ((Item)mySpinnerAlmacen.getSelectedItem())._id;

                mObtenerEstadistico(sCodCompania,sCodAlmacen);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.mySpinnerAlmacen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String  sCodCompania = ((Item)mySpinnerCompania.getSelectedItem())._id;
                String  sCodAlmacen = ((Item)mySpinnerAlmacen.getSelectedItem())._id;

                mObtenerEstadistico(sCodCompania,sCodAlmacen);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        view.findViewById(R.id.cardHelloWorldGraph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openFullscreen(FullscreenExample.HELLO_WORLD);
            }
        });

        mUI();

        return view;
    }






    private void  mUI()
    {
        this.mObtenerInfoSistema();

        if(mySpinnerCompania.getCount()>0)
        {
            String  sCodCompania = ((Item)mySpinnerCompania.getSelectedItem())._id;
            String  sCodAlmacen = ((Item)mySpinnerAlmacen.getSelectedItem())._id;

            this.mObtenerEstadistico(sCodCompania,sCodAlmacen);
        }
    }


    public void mGraficarEstadistico(GraphView graph) {
        // first series is a line
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setDrawBackground(true);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setTitle("prueba");
        series.setColor(getIntFromColor(218,00,00));

        graph.addSeries(series);




        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {

                new DataPoint(3, 5)
        });
        series2.setDataWidth(1d);
        series2.setSpacing(50);
        series2.setAnimated(true);//---
        series2.setDrawValuesOnTop(true);
        series2.setTitle("prueba1");
        series2.setColor(Color.argb(255, 60, 200, 128));

        graph.addSeries(series2);

        series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 4)
        });
        series2.setDataWidth(1d);
        series2.setSpacing(50);
        series2.setAnimated(true);
        series2.setDrawValuesOnTop(true);
        series2.setTitle("prueba2");
        series.setColor(getIntFromColor(87,135,199));

        graph.addSeries(series2);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }


    private void mGraficarBarra(int iPosicion,int iDatoEstadistico,String sTabla_NomEstadistico,int color)
    {
        BarGraphSeries<DataPoint>    Data = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(iPosicion, iDatoEstadistico)

        });

        Data.setDataWidth(2d);
        Data.setSpacing(1);
        Data.setAnimated(true);
        Data.setDrawValuesOnTop(true);
        Data.setTitle(sTabla_NomEstadistico+":"+iDatoEstadistico);
        Data.setColor(color);

        graph.addSeries(Data);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }




    // Custom Spinner adapter (ArrayAdapter<User>)
    // You can define as a private to use it in the all class
    // This is the object that is going to do the "magic"
    private SpinAdapter adapter;


    public int getIntFromColor(int Red, int Green, int Blue) {
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }


    public void mObtenerInfoSistema() {

        try {
            ProgressDialog progressDialog = new ProgressDialog(this.mMainPrincipal);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Obteniendo Informacion, por favor espere...");
            // progressDialog.setProgressStyle(android.R.attr.progressBarStyleLarge);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = new ProgressBar(this.mMainPrincipal).getIndeterminateDrawable().mutate();
                drawable.setColorFilter(ContextCompat.getColor(this.mMainPrincipal, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                progressDialog.setIndeterminateDrawable(drawable);
            }
            new MyTask_mObtenerInfoSistemax(progressDialog, this.mMainPrincipal).execute();
        }
        catch (Exception e)
        {
            this.mMsg(e.toString(), 2);
        }
    }


    public void mObtenerEstadistico(String sCodCompania,String sCodAlmacen) {

        try {
            ProgressDialog progressDialog = new ProgressDialog( this.mMainPrincipal);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Obteniendo Informacion, por favor espere...");
            // progressDialog.setProgressStyle(android.R.attr.progressBarStyleLarge);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = new ProgressBar(this.mMainPrincipal).getIndeterminateDrawable().mutate();
                drawable.setColorFilter(ContextCompat.getColor(this.mMainPrincipal, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                progressDialog.setIndeterminateDrawable(drawable);
            }
            new MyTask_mObtenerEstadistico(progressDialog, this.mMainPrincipal,sCodCompania,sCodAlmacen).execute();
        }
        catch (Exception e)
        {
            this.mMainPrincipal.mMsg(e.toString(), 2);
        }
    }

    private void mCargarGraficoEstadistico(JSONArray sdata)
    {
        // String selectedVal = getResources().getStringArray(R.array.values)[cboAlmacen.getSelectedItemPosition()];

        int iVal = sdata.length();

        Object objJson=null,objJson_=null;

        try
        {
            if(iVal>0)
            {
                graph.removeAllSeries();
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 0)
                });
                series.setTitle("Detalle:");
                graph.addSeries(series);

                int iPosicionEst=1;
                for (int i=0; i < iVal; i++) {
                    objJson_ = sdata.get(i);
                    JSONArray sdata_ = (JSONArray)objJson_;

                    int iValdetalle = sdata_.length();

                    for (int j=0; j < iValdetalle; j++) {
                        objJson = sdata_.get(j);
                        JSONObject mainObject = new JSONObject(objJson.toString());

                        String sTABLA= mainObject.getString("TABLA");
                        String sCANTIDAD= mainObject.getString("CANTIDAD");
                        int iCantidad = Integer.parseInt(sCANTIDAD);
                        String sCODALM= mainObject.getString("CODALM");
                        int iCODALM = Integer.parseInt(sCODALM);

                        int iColor=0;
                        if(i==0)
                        iColor = this.getIntFromColor(87,135,199);
                        else  iColor = this.getIntFromColor(89,199,90);

                        this.mGraficarBarra(iPosicionEst,iCantidad,"Alm: "+iCODALM,iColor);
                        iPosicionEst=iPosicionEst+1;
                    }
                }

                this.graph.getLegendRenderer().setVisible(true);
                this.graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            }
        }
        catch (Exception ex)
        {
            String ss= ex.getMessage();
            this.mMainPrincipal.mMsg("Error: "+ss,1);
        }
    }


    private void mCargar_Compania(JSONArray sdata)
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
                items[i].setId(mainObject.getString("CODCIA"));
                items[i].setName(mainObject.getString("DESCIA"));
            }

            adapter = new SpinAdapter(this.mMainPrincipal,    android.R.layout.simple_spinner_item,   items);

            mySpinnerCompania.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            String ss= ex.getMessage();
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

            adapter = new SpinAdapter(this.mMainPrincipal,    android.R.layout.simple_spinner_item,   items);

            mySpinnerAlmacen.setAdapter(adapter);
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
   // String baseUrl = "http://192.168.166.61:8092/Api/AppTecnologiaTextil/";  // This is the API base URL (GitHub API)
    String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.

   // HttpURLConnection urlConnection;



    public class MyTask_mObtenerInfoSistemax extends AsyncTask<Void, Void, Void> {

        Modelo modAlmacen = new Modelo();
        Modelo modCompania = new Modelo();
        Modelo modTipoExistencia = new Modelo();

        ProgressDialog progress;
        MainActivity act;

        String sRpta="";

        public MyTask_mObtenerInfoSistemax(ProgressDialog progress, MainActivity act) {
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

        }
    }

    private  Object mObtenerCompania()
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mEstadistico_mObtenerCompania";
        String sParametros="";

        try {

            WS_Connexion_ConfigMobil ws = new WS_Connexion_ConfigMobil(this.mMainPrincipal);
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

        String sFuncionMetodo="mEstadistico_mObtenerAlmacen";
        String sParametros="";

        try {

            WS_Connexion_ConfigMobil ws = new WS_Connexion_ConfigMobil(this.mMainPrincipal);
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










    public class MyTask_mObtenerEstadistico extends AsyncTask<Void, Void, Void> {

        Modelo modRpta = new Modelo();
        Modelo modCompania = new Modelo();

        ProgressDialog progress;
        MainActivity act;

        String sRpta="",sComCompania="",sComAlmacen="";

        public MyTask_mObtenerEstadistico(ProgressDialog progress, MainActivity act,
                                          String sComCompania,String sComAlmacen) {
            this.progress = progress;
            this.act = act;
            this.sComCompania=sComCompania;
            this.sComAlmacen=sComAlmacen;
        }

        public void onPreExecute() {
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Object obj = new Object();
                obj =  mEstadistico(sComCompania,sComAlmacen);
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
                    mCargarGraficoEstadistico(sdata);
                }
                else
                {}//    mMsg("No se pudo obtener Informacion del sistema!..", 2);
            }
            else
            {}//   mMsg("Error!. "+modAlmacen.sRpta +sRpta, 1);
        }
    }


    private  Object mEstadistico(String sCompania,String sCodAlmacen)
    {
        Object objRsult = null;
        Modelo mod = new Modelo();

        WS_Connexion_ConfigMobil ws = new WS_Connexion_ConfigMobil(this.mMainPrincipal);
        String sBaseUrlServicio = ws.mObtenerCadenaConexionWS();

        WS wsConsumo = new WS();

        String sFuncionMetodo="mEstadistico_Obtener_Inventario";
        String sParametros="/"+sCompania+"/"+sCodAlmacen;

        try {
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








    public void mMsg(String sMsg, int i) {
        AlertDialog alert = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this.mMainPrincipal);

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
