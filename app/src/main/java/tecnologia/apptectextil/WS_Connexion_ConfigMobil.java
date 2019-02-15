package tecnologia.apptectextil;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WS_Connexion_ConfigMobil {

    public SQLLiteAdmin adminBDin;
    public SQLiteDatabase bdMovil;

    public  WS_Connexion_ConfigMobil(Activity mMainActitvty)
    {
        //INICIAR BASE DATOS LOCAL - SQLLITE
        //**********************************************************************************************************
        adminBDin = new SQLLiteAdmin(mMainActitvty.getApplicationContext(), "administracion", null, 1);
        //*********************************************************************************************************

    }


    public  String mObtenerCadenaConexionWS()
    {
        String sURL_WS="";
        try{
            bdMovil = adminBDin.getReadableDatabase();
            Cursor c = bdMovil.rawQuery("SELECT URL_WS, IP, PUERTO, NOMBRESERVICIO "+
                    "FROM TB_CONFIGURACION ", null);

            if(c.getCount()>0)
            {
                if (c.moveToFirst()){
                    do {
                        // Passing values
                        sURL_WS = c.getString(0);//URL_WS

                        break;
                        // Do something Here with values
                    } while(c.moveToNext());
                }
                bdMovil.close();
            }
        }
        catch (Exception  exc )
        {
          //  mMsg("error: mObtenerParametrosGuardados: "+exc.toString(),2);

          //  sURL_WS="error: mObtenerParametrosGuardados: "+exc.toString()
        }

        return sURL_WS;
    }
}
