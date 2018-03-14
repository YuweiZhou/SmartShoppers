package vivian.smartshoppers.AccessFacade;

import android.util.Log;

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
 * Created by Vincent on 2/26/2016.
 */
public class HelpRequestsConnection implements DataConnection{
    String encoding = "UTF-8";
    public String getData(HashMap<String,String> accessBundle){
        try{
            String helper_id = accessBundle.get("helper_id");
            String request_help_id = accessBundle.get("request_help_id");
            Log.d("AsyncHelpID", helper_id);
            Log.d("AsyncRequestID", request_help_id);

            String tag = accessBundle.get("tag");

            String link="http://23.253.150.129/SmartShoppersPHP/help_requests.php";

            String data  = URLEncoder.encode("tag", encoding) + "=" + URLEncoder.encode(tag, encoding);
            data += "&" + URLEncoder.encode("helper_id", encoding) + "=" + URLEncoder.encode(helper_id, encoding);
            data += "&" + URLEncoder.encode("request_help_id", encoding) + "=" + URLEncoder.encode(request_help_id, encoding);

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
            Log.d("js", js.toString());
            return js.toString();
        }
        catch(MalformedURLException mue){
            //Textbox: Error Connecting to Server
            Log.d("URL", "STILL WRONG");
        }
        catch(UnsupportedEncodingException uee) {
            //Textbox: Error in Email/Password Conficguratino
            Log.d("ENC", "STILL WRONG");

        }
        catch(IOException ioe){
            //do things
            Log.d("ioe", "STILL WRONG");
        }
        return "";
    }
}
