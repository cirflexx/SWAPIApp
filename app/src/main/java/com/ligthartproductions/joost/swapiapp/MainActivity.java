package com.ligthartproductions.joost.swapiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ligthartproductions.joost.swapiapp.service.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String url = "http://swapi.co/api/people/";
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    public Boolean sortAge = false;
    ArrayList<HashMap<String, String>> characterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vind de buttons en zet er een onclicklistener aan vast
        Button btnFavorites = (Button) findViewById(R.id.btn_favorites);
        btnFavorites.setOnClickListener(this);

        Button btnSortAge = (Button) findViewById(R.id.btn_sortAge);
        btnSortAge.setOnClickListener(this);

        // Maakt een nieuwe ArrayList aan.
        characterList = new ArrayList<>();

        // Vind de ListView en zet deze in een variabele
        lv = (ListView) findViewById(R.id.list_ResultList);

        new GetCharacters().execute();

    }


    public void onClick(View v) {
        switch (v.getId()) {
            // Als de Favorites button wordt ingedrukt dan gaat de gebruiker naar de FavoritesActivity
            case R.id.btn_favorites:
                Intent intent = new Intent(this, FavoritesActivity.class);
                startActivity(intent);
                break;

            // Als de sortAge button wordt ingedrukt wordt sortAge true als hij false is en false als hij true is.
            case R.id.btn_sortAge:
                sortAge = !sortAge;

                // Herhaald de GetCharacters.
                new GetCharacters().execute();
                break;

            default:
                break;
        }
    }


    private class GetCharacters extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Laat een pop-up zien waarin aangegeven wordt dat de gegevens opgehaald worden.
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading character information...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
                HttpRequest httpReq = new HttpRequest();
                // Maakt een request naar de URL, en stopt de response in een string.
                String jsonStr = httpReq.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);


                        // Haalt de JSON array op waar de karakters in zitten
                        JSONArray characters = jsonObj.getJSONArray("results");
                        // Loopt door alle karakter heen.
                        for (int i = 0; i < characters.length(); i++) {
                            JSONObject c = characters.getJSONObject(i);

                            //Haalt de naam en geboortenaam uit de Json en zet deze in een variabele
                            String name = c.getString("name");
                            String birth_year = c.getString("birth_year");

                            // Er wordt een nieuwe hashmap aangemaakt genaamd character.
                            HashMap<String, String> character = new HashMap<>();

                            //Voegt de informatie in de HashMap
                            character.put("name", name);
                            character.put("birth_year", birth_year);

                            // Voegt de karakter toe aan de lijst
                            characterList.add(character);

                            // Er wordt gekeken of sortAge true of false is.
                            if(!sortAge) {
                                // Als sortAge false is dan wordt de lijst op naam gesorteerd (dit is standaard als de app wordt opgestart).
                                Collections.sort(characterList, new Comparator<HashMap<String, String>>() {
                                    @Override
                                    public int compare(HashMap<String, String> lhs,
                                                       HashMap<String, String> rhs) {
                                        // Do your comparison logic here and retrn accordingly.
                                        return lhs.get("name").compareTo(rhs.get("name"));
                                    }
                                });
                            } else {
                                // Als sortAge true is wordt de lijst gesorteerd naar geboortejaar
                                Collections.sort(characterList, new Comparator<HashMap<String, String>>() {
                                    @Override
                                    public int compare(HashMap<String, String> lhs,
                                                       HashMap<String, String> rhs) {
                                        // Do your comparison logic here and retrn accordingly.
                                        return lhs.get("birth_year").compareTo(rhs.get("birth_year"));
                                    }
                                });
                            }
                        }

                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "No results are found.");
                }

                return null;
            }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Stopt het laad pop-up
            if (pDialog.isShowing())
                pDialog.dismiss();


            // JSON wordt in de ListView gezet
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, characterList,
                    R.layout.listview, new String[]{"name", "birth_year"},
                    new int[]{R.id.name, R.id.byear});

            lv.setAdapter(adapter);
        }
    }

}
