package b9nc9.github.busfndr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HelloActivity extends AppCompatActivity{

    private static final int READ_REQUEST_CODE = 42;

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    private void savePreferences(String db) {
        SharedPreferences prefs = this.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("db", db);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello);
        Button kuld = (Button)findViewById(R.id.talloz);
        kuld.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                performFileSearch();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == HelloActivity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                getContentResolver().takePersistableUriPermission(uri,  Intent.FLAG_GRANT_READ_URI_PERMISSION);
                savePreferences("file", uri);

            }
        }
    }

    private void savePreferences(String pref, Uri ertek) {
        SharedPreferences prefs = this.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pref, ertek.toString());
        editor.commit();
        startActivity(new Intent(HelloActivity.this, MainActivity.class));
    }
}