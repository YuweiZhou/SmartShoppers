package vivian.smartshoppers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by John on 2/12/2016.
 */
public class UserLoginTask extends AsyncTask<Void, Void, String> {

    Map<String, String> vals;
    String link;
    String encoding;
    Intent dest;
    Activity act;
    String json;

    public UserLoginTask(Intent dest, Activity act, ArrayList<String> in)
    {
        this.link = act.getString(R.string.link);
        this.encoding = act.getString(R.string.encoding);
        this.dest = dest;
        this.act = act;
        vals = new LinkedHashMap<>();
        for (String s:in) {
            vals.put(s.split("=")[0], s.split("=")[1]);
        }
    }
    public UserLoginTask(Intent dest, Activity act, String... params)
    {
        this.link = act.getString(R.string.link);
        this.encoding = act.getString(R.string.encoding);
        this.dest = dest;
        this.act = act;
        vals = new LinkedHashMap<>();
        for(String s:params)
        {
            Log.d("String s", s);
            vals.put(s.split("=")[0], s.split("=")[1]);
        }
    }
    @Override
    protected String doInBackground(Void... params){

        Log.d("Background", "1");
        try{
            String data = this.getData();
            Log.d("Background", link);
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            Log.d("Background", "3");
            this.writeData(data, conn);
            Log.d("Background", "4");
            return this.readData(conn);
        }
        catch(Exception e) {
            Log.d("Err", "There was an exception");
            return "";
        }
    }
    protected String getData() throws UnsupportedEncodingException{
        String data = "";
        for(String key:vals.keySet())
        {
            data += "&" + URLEncoder.encode(key, encoding) + "=" + URLEncoder.encode(vals.get(key), encoding);
        }
        data += "&" + URLEncoder.encode("user_type", encoding) + "=" + URLEncoder.encode("payer", encoding);
        data = data.replaceFirst("&", "");
        return data;
    }
    public void writeData(String data, URLConnection conn) throws IOException{
        conn.setDoOutput(true);
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        wr.write(data);
        wr.close();
    }
    public String readData(URLConnection conn) throws IOException
    {
        //this needs to be chagned to read in to jsonobj
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder js = new StringBuilder();
        String line = null;
        // Read Server Response
        while((line = reader.readLine()) != null) {
            js.append(line);
        }
        reader.close();
        json = js.toString();
        return json;
    }
    public boolean getSuccess(String js){
        //this should be changed, will need to read json
        return js.contains("success");
    }
    @Override
    protected void onPostExecute(String result){
        Log.d("Background", "5");
        Bundle bundle = new Bundle();
        bundle.putString("userName", "");
        dest.putExtras(bundle);
        if(result.contains("success")) act.startActivity(dest);
        else {
            Toast.makeText(act.getApplicationContext(), "The entered credentials were incorrect.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}