package com.example.student.officehoursignin;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<String> listItems=new ArrayList<String>();
    ArrayList<String> ids=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        ListView ohlist = (ListView) findViewById(R.id.ohlist);
        ohlist.setAdapter(adapter);
        ohlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selected >= 0) {
                    View old = parent.getChildAt(selected);
                    old.setBackgroundColor(0xFFFFFFFF);
                }
                selected = position;
                Log.d("Click",""+selected);
                view.setBackgroundColor(0xFF90FF82);
            }
        });
    }

    public void submit(View view) throws IOException{
        if(selected==-1){
            return;
        }
        ListView ohlist = (ListView) findViewById(R.id.ohlist);
        ohlist.getChildAt(selected).setBackgroundColor(0xFFFFFFFF);
        String submitOh = ids.get(selected);
        listItems.clear();
        adapter.add("Loading");
        ids.clear();
        selected = -1;
        new handleSubmit().execute(new URL("http://igis.uvaguides.org/ohreceive.php?method=post&oh_id="+submitOh));
    }

    public void refresh(View view) throws IOException {
        if(selected!=-1){
            ListView ohlist = (ListView) findViewById(R.id.ohlist);
            ohlist.getChildAt(selected).setBackgroundColor(0xFFFFFFFF);
        }
        listItems.clear();
        adapter.add("Loading");
        ids.clear();
        selected = -1;
        new handleRefresh().execute(new URL("http://igis.uvaguides.org/ohpublish.php"));
    }

    private class handleRefresh extends AsyncTask<URL, Integer, String> {

        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection igis = null;
            String result = "";
            try {
                igis = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(igis.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                result += reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                igis.disconnect();
            }
            return result;
        }


        protected void onPostExecute(String result) {
            listItems.clear();
            ids.clear();
            if(!result.contains(";")){
                adapter.add("No office hours right now. Check back later!");
                ids.add("0");
                return;
            }else {
                String[] ohs = result.split(";");
                for (String oh : ohs) {
                    String name = oh.split(",")[0];
                    String id = oh.split(",")[1];
                    adapter.add(name);
                    ids.add(id);
                }
            }
        }
    }

    private class handleSubmit extends AsyncTask<URL, Integer, String> {

        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection igis = null;
            String result = "";
            try {
                igis = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(igis.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                result += reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                igis.disconnect();
            }
            return result;
        }


        protected void onPostExecute(String result) {
            listItems.clear();
            ids.clear();
            if(!result.contains(";")){
                adapter.add("No office hours right now. Check back later!");
                ids.add("0");
                return;
            }else {
                String[] ohs = result.split(";");
                for (String oh : ohs) {
                    String name = oh.split(",")[0];
                    String id = oh.split(",")[1];
                    adapter.add(name);
                    ids.add(id);
                }
            }
        }
    }


}

