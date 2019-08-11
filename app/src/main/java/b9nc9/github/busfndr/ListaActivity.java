package b9nc9.github.busfndr;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class ListaActivity extends AppCompatActivity {

    RecyclerView BuszRecyclerView;
    BuszAdapter BuszAdapter;
    List<Busz> bData;

    ArrayList<Volan> volan = new ArrayList<Volan>();
    ArrayList<Busz> db = new ArrayList<Busz>();

    void hozzaad(ArrayList<Volan> adat, ArrayList<Busz> db, int i){
        db.add(new Busz(adat.get(i).indul, adat.get(i).cel,
                adat.get(i).indulIdo, adat.get(i).erkezIdo,
                adat.get(i).kozlekedik));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        BuszRecyclerView = findViewById(R.id.rv);
        bData = new ArrayList<>();

        Date most = new Date();
        SimpleDateFormat hourformat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteformat = new SimpleDateFormat("mm");
        int ora = Integer.parseInt(hourformat.format(most));
        int perc = Integer.parseInt(minuteformat.format(most));
        int jobuszok = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(most);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayofMonth = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String Indul;
        String Cel;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Indul= null;
                Cel = null;

            } else {
                Indul= extras.getString("Indul");
                Cel= extras.getString("Cel");
                year = extras.getInt("ev");
                month = extras.getInt("honap");
                dayofMonth = extras.getInt("nap");
                volan= (ArrayList<Volan>) getIntent().getSerializableExtra("volan");
            }
        } else {
            Indul= (String) savedInstanceState.getSerializable("Indul");
            Cel= (String) savedInstanceState.getSerializable("Cel");
            year = savedInstanceState.getInt("ev");
            month = savedInstanceState.getInt("honap");
            dayofMonth = savedInstanceState.getInt("nap");
            volan= (ArrayList<Volan>) savedInstanceState.getSerializable("volan");
        }

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayofMonth);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        for(int i = 0; i < volan.size(); i++) {
            String indul = volan.get(i).indul;
            String cel = volan.get(i).cel;
            String kozlekedik = volan.get(i).kozlekedik;
            Boolean egyezes = Indul.equals(indul) && Cel.equals(cel);

            if (egyezes == true) {
                // ezt a részt valószínűleg le lehet egyszerűsíteni
                if (kozlekedik.equals("naponta")) {
                    hozzaad(volan, db, i);
                } else if ((kozlekedik.equals("munkanapokon")) && (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY)) {
                    hozzaad(volan, db, i);
                } else if ((kozlekedik.equals("munkaszüneti napok kivételével naponta")) && (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.SATURDAY)) {
                    hozzaad(volan, db, i);
                } else if ((kozlekedik.equals("szabadnap kivételével naponta")) && (dayOfWeek != Calendar.SUNDAY)) {
                    hozzaad(volan, db, i);
                } else if ((kozlekedik.equals("szabad- és munkaszüneti napokon")) && (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
                    hozzaad(volan, db, i);
                } else if (kozlekedik.equals("szabadnapokon") && (dayOfWeek == Calendar.SATURDAY)) {
                    hozzaad(volan, db, i);
                } else if (kozlekedik.equals("iskolai előadási napokon")) {
                    if (month == Calendar.JUNE && dayofMonth <= 15 && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                        hozzaad(volan, db, i);
                    } else if (month != Calendar.JUNE && month != Calendar.JULY && month != Calendar.AUGUST && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                        hozzaad(volan, db, i);
                    }
                }
            }
        }


        for(int i = 0; i < db.size(); i++){
            int[] menet = {Integer.parseInt(db.get(i).indulIdo.split(":")[0]), Integer.parseInt(db.get(i).indulIdo.split(":")[1])};
            if(ora < menet[0] || (ora == menet[0] && perc < menet[1])){
                jobuszok++;
            }
        }

        if(db.isEmpty()){
            ImageView xicon = findViewById(R.id.xicon);
            TextView nt = findViewById(R.id.nt);
            xicon.setVisibility(View.VISIBLE);
            nt.setVisibility(View.VISIBLE);
        }

        BuszAdapter = new BuszAdapter(this, db);
        BuszRecyclerView.setHasFixedSize(true);
        BuszRecyclerView.setAdapter(BuszAdapter);
        BuszRecyclerView.setNestedScrollingEnabled(false);
        BuszRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BuszRecyclerView.scrollToPosition(BuszRecyclerView.getAdapter().getItemCount() - jobuszok);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slideup1, R.anim.slidedown);
    }


}
