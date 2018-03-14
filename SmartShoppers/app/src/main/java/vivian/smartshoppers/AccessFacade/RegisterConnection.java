package vivian.smartshoppers.AccessFacade;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by John on 3/4/2016.
 */
public class RegisterConnection implements DataConnection {
    String encoding = "UTF-8";
    public String getData(HashMap<String, String> accessBundle) {
        try{
            //String data = this.writeData(accessBundle);
            ArrayList<String> values = new ArrayList<String>();
            for(String key:accessBundle.keySet()){
                values.add(accessBundle.get(key));
            }
            accessBundle.put("tag", "register");
            accessBundle.put("user_type", "payer");
            String link="http://23.253.150.129/SmartShoppersPHP/auth.php";
            String data = writeData(accessBundle);

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            wr.write(data);

            wr.close();


            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder js = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null) {
                js.append(line);
            }
            reader.close();
            Log.d("json", js.toString());
            return js.toString();

            //JSONObject read = new JSONObject(js.toString());
            //int success = read.getInt("success");
            //Log.d("Login", js.toString());
        }
        catch(MalformedURLException mue){
            //Textbox: Error Connecting to Server
            mue.printStackTrace();;
        }
        catch(UnsupportedEncodingException uee) {
            //Catch with isValidTests!!! Log string error somewhere, contains uncaught character.
            uee.printStackTrace();
        }
        catch(Exception e){
            //do things
            e.printStackTrace();
        }

        return "";
    }
    public String writeData(HashMap<String, String> accessBundle) throws UnsupportedEncodingException{
        String data = "";
        for(String key:accessBundle.keySet()){
            data += "&" + URLEncoder.encode(key, encoding) + "=" + URLEncoder.encode(accessBundle.get(key), encoding);
        }
        data = data.replaceFirst("&", "");
        return data;
    }

}
