package vivian.smartshoppers.AccessFacade;

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

/**
 * Created by Vincent on 2/22/2016.
 */
public class GroceryStoreConnection implements DataConnection {
    String encoding = "UTF-8";
    public String getData(HashMap<String,String> accessBundle){
        try{
            String mlat = accessBundle.get("lat");
            String mlng = accessBundle.get("lng");
            String link="http://23.253.150.129/SmartShoppersPHP/groceryStores.php";
            String data  = URLEncoder.encode("tag", encoding) + "=" + URLEncoder.encode("map_stores", encoding);
            data += "&" + URLEncoder.encode("lat", encoding) + "=" + URLEncoder.encode(mlat, encoding);
            data += "&" + URLEncoder.encode("lng", encoding) + "=" + URLEncoder.encode(mlng, encoding);

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
            //Format sb!!! return result
            //dummy result
            return js.toString();
        }
        catch(MalformedURLException mue){
            //Textbox: Error Connecting to Server
        }
        catch(UnsupportedEncodingException uee) {
            //Textbox: Error in Email/Password Conficguratino
        }
        catch(IOException ioe){
            //do things
        }
        return "";
    }
}
