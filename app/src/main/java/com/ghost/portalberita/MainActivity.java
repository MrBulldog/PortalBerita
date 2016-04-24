package com.ghost.portalberita;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {


    LazyAdapter adapter;
    JSONParser jsonParser = new JSONParser() ;
    ListView listView;
    ProgressDialog progressDialog;
    JSONArray jsonArray;
    ArrayList<HashMap<String, String>> databerita = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databerita = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
        listView = (ListView)findViewById(R.id.listView);

    }




    private class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Holy Crap");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_BERITA, "GET", param);

            try {
                jsonArray = json.getJSONArray("berita");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    String id_berita = c.getString(Consatant.TAG_ID);
                    String judul = c.getString(Consatant.TAG_JUDUL);
                    String link_gambar = Config.BASE_IMG + c.getString(Consatant.TAG_GAMBAR);

                    HashMap<String, String> map = new HashMap<>();
                    map.put(Consatant.TAG_ID, id_berita);
                    map.put(Consatant.TAG_JUDUL, judul);
                    map.put(Consatant.TAG_GAMBAR, link_gambar);
                    databerita.add(map);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SetListAdapter(databerita);
                }

            });
        }
    }

        private void SetListAdapter(ArrayList<HashMap<String, String>> databerita) {
            listView = (ListView) findViewById(R.id.listView);
            adapter = new LazyAdapter(this, databerita);
            listView.setAdapter(adapter);


    }


}

