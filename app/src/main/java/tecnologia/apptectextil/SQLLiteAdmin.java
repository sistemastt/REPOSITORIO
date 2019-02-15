package tecnologia.apptectextil;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by lescalante on 24/02/2017.
 */

public class SQLLiteAdmin extends SQLiteOpenHelper {

    public SQLLiteAdmin(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table TB_CONFIGURACION(URL_WS TEXT,IP TEXT,PUERTO TEXT,NOMBRESERVICIO TEXT)");

        db.execSQL("create table TB_LOGIN(USUARIO TEXT,PASSWORD TEXT,RECORDAR_LOGIN TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS TB_CONFIGURACION");

        db.execSQL("DROP TABLE IF EXISTS TB_LOGIN");

        onCreate(db);
    }
}
