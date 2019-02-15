package tecnologia.apptectextil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter_lista_menu  extends BaseAdapter {

    Context context;
    int[] iImagenes;
    LayoutInflater inflater;
    String[] Evento,Evento_detalle,Evento_id;

    public adapter_lista_menu(Context contx,int[] Imagenes, String[] Evento, String[] Evento_detalle, String[] Evento_id) {
        context=contx;
        iImagenes=Imagenes;
        this.Evento=Evento;
        this.Evento_detalle=Evento_detalle;
        this.Evento_id=Evento_id;
    }
    @Override
    public int getCount() {
        return Evento.length;
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
        TextView uilblEVENTO_OS,uilblEVENTO_OS_det,uilblEVENTO_ID;
        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lista_menus, parent, false);

        imgImg = (ImageView) itemView.findViewById(R.id.icono);
        uilblEVENTO_OS = (TextView) itemView.findViewById(R.id.lblEVENTO_OS);
        uilblEVENTO_OS_det = (TextView) itemView.findViewById(R.id.lblEVENTO_OS_det);
        uilblEVENTO_ID = (TextView) itemView.findViewById(R.id.lblEVENTO_ID);


        // Capture position and set to the TextViews
        imgImg.setImageResource(this.iImagenes[position]);
        uilblEVENTO_OS.setText(this.Evento[position]);
        uilblEVENTO_OS_det.setText(this.Evento_detalle[position]);
        uilblEVENTO_ID.setText(this.Evento_id[position]);



        return itemView;
    }


}
