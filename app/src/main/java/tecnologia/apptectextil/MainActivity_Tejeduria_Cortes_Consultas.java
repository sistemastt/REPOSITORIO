package tecnologia.apptectextil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;

import java.util.Calendar;

public class MainActivity_Tejeduria_Cortes_Consultas extends AppCompatActivity {

    TabHost uitabhost1;
    EditText uitxtFECHADESDE,uitxtFECHAHASTA;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__tejeduria__cortes__consultas);
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

        //tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);

        this.uitabhost1.setup();
        Object dd=  this.uitabhost1.newTabSpec("Criterios");
        ((TabHost.TabSpec) dd).setContent(R.id.tab1);
        ((TabHost.TabSpec) dd).setIndicator("Criterios");
        this.uitabhost1.addTab(((TabHost.TabSpec) dd));

        dd=  this.uitabhost1.newTabSpec("Cortes Tejeduria");
        ((TabHost.TabSpec) dd).setContent(R.id.tab2);
        ((TabHost.TabSpec) dd).setIndicator("Cortes Tejeduria");
        this.uitabhost1.addTab(((TabHost.TabSpec) dd));


        this.uitxtFECHADESDE.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // calender class's instance and get current date , month and year from calender
                String sFH= uitxtFECHADESDE.getText().toString();

                String sDia= sFH.substring(0,2);
                String sMes= sFH.substring(3,5);
                String sAnio= sFH.substring(6);

                int mYear = Integer.parseInt(sAnio); // current year
                int mMonth = Integer.parseInt(sMes); // current month
                int mDay = Integer.parseInt(sDia); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity_Tejeduria_Cortes_Consultas.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                // monthOfYear=monthOfYear+1;
                                String sDia= String.format("%02d", dayOfMonth);
                                String sMes= String.format("%02d", (monthOfYear+1));

                                uitxtFECHADESDE.setText(sDia + "/" + sMes + "/" + year);
                            }
                        }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });


        this.uitxtFECHAHASTA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // calender class's instance and get current date , month and year from calender
                String sFH= uitxtFECHAHASTA.getText().toString();

                String sDia= sFH.substring(0,2);
                String sMes= sFH.substring(3,5);
                String sAnio= sFH.substring(6);

                int mYear = Integer.parseInt(sAnio); // current year
                int mMonth = Integer.parseInt(sMes); // current month
                int mDay = Integer.parseInt(sDia); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity_Tejeduria_Cortes_Consultas.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                // monthOfYear=monthOfYear+1;
                                String sDia= String.format("%02d", dayOfMonth);
                                String sMes= String.format("%02d", (monthOfYear+1));

                                uitxtFECHAHASTA.setText(sDia + "/" + sMes + "/" + year);
                            }
                        }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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



    private  void mUI() {

        this.uitabhost1 = (TabHost) findViewById(R.id.tabhost1);


        this.uitxtFECHADESDE = (EditText) findViewById(R.id.txtfecini);
        this.uitxtFECHAHASTA = (EditText) findViewById(R.id.txtfecfin);


        Calendar c = Calendar.getInstance();
        int iYear = c.get(Calendar.YEAR); // current year
        int iMonth = c.get(Calendar.MONTH)+1; // current month
        int iDay = c.get(Calendar.DAY_OF_MONTH); // current day

        String sDia= String.format("%02d", iDay);
        String sMes= String.format("%02d", iMonth);

        this.uitxtFECHADESDE.setText(sDia + "/" + sMes + "/" + iYear);

        this.uitxtFECHAHASTA.setText(sDia + "/" + sMes + "/" + iYear);

    }


}
