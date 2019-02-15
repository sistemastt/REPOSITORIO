package tecnologia.apptectextil;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tecnologia.apptectextil.Modelo.FAB085_GuardarRegistro_Model;
import tecnologia.apptectextil.Modelo.Modelo;



public class WS {


    String sUrl="";

    HttpURLConnection urlConnection;


    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    public  Object mProcesarWS_PorParametros(String sUrlServicio,String sFuncionMetodo,String sParametros)
    {
       // "http://192.168.166.61:8092/Api/AppTecnologiaTextil/";

        Object objRsult = null;

        Modelo mod = new Modelo();

       this.sUrl = sUrlServicio + sFuncionMetodo+sParametros;
        StringBuilder result = new StringBuilder();
        //"https://api.github.com/users/dmnugent80/repos"
        try {
            URL url = new URL(this.sUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000); //set timeout to 5 seconds
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
            JSONArray obj = mainObject.getJSONArray("obj");

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



    public   String mProcesarWS_Post(String sUrlServicio,String sFuncionMetodo,FAB085_GuardarRegistro_Model mod)
    {
        String sRpta="";
try
{
    BufferedReader reader=null;

    this.sUrl = sUrlServicio + sFuncionMetodo;


    JSONObject jsonObject = new JSONObject();
    JSONObject envio = new JSONObject();
    envio.put("sCodCompania", mod.sCodCompania);
    envio.put("sCodAlmacen",mod.sCodAlmacen);
    envio.put("sEtiqueta", mod.sEtiqueta);
    envio.put("sZona", mod.sZona);
    envio.put("sAndamio", mod.sAndamio);
    envio.put("sCoordenada", mod.sCoordenada);
    envio.put("sKilo", mod.sKilo);
    envio.put("sConos", mod.sConos);
    envio.put("sUsuario", mod.sUsuario);

    jsonObject.put("param", envio);

    URL url = new URL( this.sUrl); //Constantes.URL+"RestTerminado/LeerRollo"
    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
    httpURLConnection.setDoOutput(true);
    httpURLConnection.setDoInput(true);
    httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
    httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
    httpURLConnection.setReadTimeout(READ_TIMEOUT);
    httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
    httpURLConnection.connect();


    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());

    wr.writeBytes(jsonObject.toString());
    wr.flush();
    InputStream inputStream;

    int status = httpURLConnection.getResponseCode();

    if (status != HttpURLConnection.HTTP_OK)
        inputStream = httpURLConnection.getErrorStream();
    else
        inputStream = httpURLConnection.getInputStream();
    //**************************
    reader = new BufferedReader(new InputStreamReader(inputStream ));
    StringBuilder sb = new StringBuilder();
    String line = null;

}
catch (Exception ee)
{}

return   sRpta;
    }

    public String mProcesarWS_Post_X(String sUrlServicio,String sFuncionMetodo,FAB085_GuardarRegistro_Model mod){

        String sRpta="";


        this.sUrl = sUrlServicio + sFuncionMetodo;

        HttpPut put = new HttpPut(this.sUrl);
        put.setHeader("content-type", "application/json");

        try
        {




            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(this.sUrl);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("sCodCompania", mod.sCodCompania));
            params.add(new BasicNameValuePair("sCodAlmacen",mod.sCodAlmacen));
            params.add(new BasicNameValuePair("sEtiqueta", mod.sEtiqueta));
            params.add(new BasicNameValuePair("sZona", mod.sZona));
            params.add(new BasicNameValuePair("sAndamio", mod.sAndamio));
            params.add(new BasicNameValuePair("sCoordenada", mod.sCoordenada));
            params.add(new BasicNameValuePair("sKilo", mod.sKilo));
            params.add(new BasicNameValuePair("sConos", mod.sConos));
            params.add(new BasicNameValuePair("sUsuario", mod.sUsuario));


            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
            String text = EntityUtils.toString(ent);


            sRpta="";
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
            sRpta = ex.toString();
        }

        return sRpta;
    }

}
