package tecnologia.apptectextil;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter_lista_muestras extends BaseAdapter {

    Context context;
    int[] iImagenes;
    LayoutInflater inflater;
    String[] UCOMIN,DESCOL,PROD,UFOMIN,UFIMIN;
int iTipo;

    public adapter_lista_muestras(Context contx,int[] Imagenes, String[] UCOMIN, String[] UFIMIN, String[] UFOMIN, String[] PROD, String[] DESCOL,int iTipo) {
        context=contx;
        iImagenes=Imagenes;
        this.UCOMIN=UCOMIN;
        this.UFIMIN=UFIMIN;
        this.UFOMIN=UFOMIN;
        this.PROD=PROD;
        this.DESCOL=DESCOL;
this.iTipo=iTipo;
    }
    @Override
    public int getCount() {
        return PROD.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imgImg;
        TextView uilbl_Columna,uilbl_Fila,uilbl_Fondo,uilbl_Articulo,uilbl_Color,uilbl_Columna_Nombre,uilbl_Fila_Nombre,uilbl_Fondo_nombre;
        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lista_resultado, parent, false);

        imgImg = (ImageView) itemView.findViewById(R.id.icono);
        uilbl_Columna = (TextView) itemView.findViewById(R.id.lbl_Columna);
        uilbl_Fila = (TextView) itemView.findViewById(R.id.lbl_Fila);
        uilbl_Fondo= (TextView) itemView.findViewById(R.id.lbl_Fondo);
        uilbl_Articulo = (TextView) itemView.findViewById(R.id.lbl_Articulo);
        uilbl_Color = (TextView) itemView.findViewById(R.id.lbl_Color);

        uilbl_Columna_Nombre = (TextView) itemView.findViewById(R.id.lbl_Columna_Nombre);
        uilbl_Fila_Nombre = (TextView) itemView.findViewById(R.id.lbl_Fila_Nombre);
        uilbl_Fondo_nombre = (TextView) itemView.findViewById(R.id.lbl_Fondo_nombre);

       // Capture position and set to the TextViews
        imgImg.setImageResource(this.iImagenes[position]);
        uilbl_Columna.setText(this.UCOMIN[position]);
        uilbl_Fila.setText(this.UFIMIN[position]);
        uilbl_Fondo.setText(this.UFOMIN[position]);
        uilbl_Articulo.setText(this.PROD[position]);
        uilbl_Color.setText(this.DESCOL[position]);

        if(this.iTipo==1)//SI ES CRUDO?
        {
            uilbl_Columna_Nombre.setText("ZONA");
            uilbl_Fila_Nombre.setText("RUMA");
            uilbl_Fondo_nombre.setText("NIVEL");
            uilbl_Color.setText("NULL");
        }

        return itemView;
    }

}
