package vivian.smartshoppers.AccessFacade;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Vincent on 2/19/2016.
 */
public class UserConnection implements DataConnection {
    String encoding = "UTF-8";
    public String getData(HashMap<String, String> accessBundle) {
        try{
            //String data = this.writeData(accessBundle);
            String email = accessBundle.get("email");
            String password = accessBundle.get("password");
            String link="http://23.253.150.129/SmartShoppersPHP/auth.php";
            String data  = URLEncoder.encode("tag", encoding) + "=" + URLEncoder.encode("login", encoding);
            data += "&" + URLEncoder.encode("email", encoding) + "=" + URLEncoder.encode(email, encoding);
            data += "&" + URLEncoder.encode("password", encoding) + "=" + URLEncoder.encode(password, encoding);

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
            return js.toString();
            //JSONObject read = new JSONObject(js.toString());
            //int success = read.getInt("success");
            //Log.d("Login", js.toString());
        }
        catch(MalformedURLException mue){
            //Textbox: Error Connecting to Server
        }
        catch(UnsupportedEncodingException uee) {
            //Catch with isValidTests!!! Log string error somewhere, contains uncaught character.
        }
        catch(Exception e){
            //do things
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
