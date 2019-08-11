package b9nc9.github.busfndr;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.badoualy.datepicker.DatePickerTimeline;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity{


     //EGYEDI SPINNER ADAPTER
    class SpinnerAdapter extends BaseAdapter {
        private ArrayList<String> lista;
        private Activity parentActivity;
        private LayoutInflater inflater;

        public SpinnerAdapter(Activity parent, ArrayList<String> l) {
            parentActivity = parent;
            lista = l;
            inflater = (LayoutInflater) parentActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public Object getItem(int position) {
            return lista.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             View view = convertView;
             if (convertView == null)
                 view = inflater.inflate(R.layout.spinnertext, null);

             TextView text = (TextView) view.findViewById(R.id.spinnertext);
             String myObj = lista.get(position);
             text.setText(myObj);
             text.setSelected(true);
             return view;
         }
    }

    ArrayList<String> indulAllomasok = new ArrayList<String>();
    ArrayList<String> erkezAllomasok = new ArrayList<String>();
    ArrayList<Volan> volan = new ArrayList<Volan>();
    Calendar c = Calendar.getInstance();

    // BEOLVAS AZ ADATBÁZISBÓL
    public void Beolvas(ArrayList<Volan> volan) throws IOException {
        SharedPreferences pref = getSharedPreferences("mypref", MODE_PRIVATE);
        Uri uri = Uri.parse(pref.getString("file", "none"));
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String sor = "";
        while ((sor = reader.readLine()) != null) {
            if(sor != null){
                volan.add(new Volan(sor));
            }
        }
    }

    // ELLENŐRZI, HOGY ELŐSZÖR FUT-E AZ APP
    public void Elso(){
        SharedPreferences pref = getSharedPreferences("mypref", MODE_PRIVATE);
        if(pref.getBoolean("firststart", true)){
            startActivity(new Intent(MainActivity.this, HelloActivity.class));
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firststart", false);
            editor.commit();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Elso();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // BEOLVASÁS
        try {
            Beolvas(volan);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //AZ ADATBÁZISBAN SZEREPLŐ VÁROSOK BETÖLTÉSE
        for(int i = 0; i < volan.size(); i++){
            indulAllomasok.add(volan.get(i).indul);
        }

        for(int i = 0; i < volan.size(); i++){
            erkezAllomasok.add(volan.get(i).cel);
        }

        indulAllomasok =  new ArrayList(new HashSet(indulAllomasok));
        erkezAllomasok =  new ArrayList(new HashSet(erkezAllomasok));

        // ÁLLOMÁSOK BEÁLLÍTÁSA

        SpinnerAdapter indulAdapter = new SpinnerAdapter(this, indulAllomasok);
        SpinnerAdapter erkezAdapter = new SpinnerAdapter(this, indulAllomasok);

        final Spinner indulAllomas = (Spinner) findViewById(R.id.honnan);
        indulAllomas.setAdapter(indulAdapter);
        final Spinner erkezAllomas = (Spinner) findViewById(R.id.hova);
        erkezAllomas.setAdapter(erkezAdapter);

        indulAllomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String clickedObj = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        erkezAllomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String clickedObj = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});


        //CSERE
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(1000);


        final ImageButton csere = (ImageButton) findViewById(R.id.csere);
        csere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indulPos = indulAllomas.getSelectedItemPosition();
                int celPos = erkezAllomas.getSelectedItemPosition();
                csere.animate().rotationBy(180).setDuration(350);
                indulAllomas.startAnimation(out);
                erkezAllomas.startAnimation(out);
                indulAllomas.setSelection(celPos);
                erkezAllomas.setSelection(indulPos);
                indulAllomas.startAnimation(in);
                erkezAllomas.startAnimation(in);
            }
        });


        //DÁTUM KIVÁLASZTÁSA
        Date most = new Date();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        c.setTime(most);

        DatePickerTimeline timeline = findViewById(R.id.timeline);
        timeline.setFirstVisibleDate(c.get(Calendar.YEAR)-1, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        timeline.setLastVisibleDate(c.get(Calendar.YEAR)+1, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        timeline.setSelectedDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
            }
        });


        //KERESÉS INDÍTÁSA
        Button keres = (Button) findViewById(R.id.keres);
        keres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buszKereses = new Intent(MainActivity.this, ListaActivity.class);

                Spinner indulspinner = (Spinner) findViewById(R.id.honnan);
                Spinner celspinner = (Spinner) findViewById(R.id.hova);
                String Indul = indulspinner.getSelectedItem().toString();
                String Cel = celspinner.getSelectedItem().toString();

                int ev = c.get(Calendar.YEAR);
                int honap = c.get(Calendar.MONTH);
                int nap = c.get(Calendar.DAY_OF_MONTH);

                buszKereses.putExtra("Indul", Indul);
                buszKereses.putExtra("Cel", Cel);
                buszKereses.putExtra("volan", volan);
                buszKereses.putExtra("ev", ev);
                buszKereses.putExtra("honap", honap);
                buszKereses.putExtra("nap", nap);

                startActivity(buszKereses);
                overridePendingTransition(R.anim.slidedown1, R.anim.slideup);
            }
        });

        //BEÁLLÍTÁSOK
        ImageButton settings = (ImageButton) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                TextView logo = (TextView) findViewById(R.id.logo);
                ActivityOptions ops = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, logo, "logo"
                );
                startActivity(settings, ops.toBundle());
                overridePendingTransition(R.anim.slidedown1, R.anim.slideup);
            }
        });

    }
}