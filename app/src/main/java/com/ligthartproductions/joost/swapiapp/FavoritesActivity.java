package com.ligthartproductions.joost.swapiapp;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

public class FavoritesActivity extends AppCompatActivity {

    public ListView lv;
    ArrayList<HashMap<String, String>> favoritesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // De toolbar aan de bovenkant van het scherm wordt geladen en getoond.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // De ListView wordt gevonden en in een variabele gezet.
        lv = (ListView) findViewById(R.id.lv_favorites);

        // Er wordt een nieuwe array aangemaakt (hier moeten de favoriten in komen).
        favoritesList = new ArrayList<>();

        // Er wordt een terugknop link in de toolbar gezet.
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Als er op de terugknop wordt geklikt gaat hij naar het vorige scherm (MainActivity).
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
