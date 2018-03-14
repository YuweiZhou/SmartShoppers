package vivian.smartshoppers;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Payer_Info extends ListActivity implements AdapterView.OnItemClickListener {

    public static String [] Groceries={};
    public static Double[] Prices={};
    public String payerName;
    public String payerAddress;
    public double totalPay;
    public ArrayList<Integer> Position = new ArrayList<Integer>();
    public ArrayList<String> selected_stuff = new ArrayList<String>();
    TextView payerNameTV;
    TextView payerAddressTV;
    TextView totalPayTV;

    public int payerPosition;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer__info);

        sp = getSharedPreferences(getString(R.string.SPName), 0);

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if (!extrasBundle.isEmpty()) {
            boolean hasPayerName = extrasBundle.containsKey("payerName");
            boolean hasPayerAddress = extrasBundle.containsKey("payerAddress");
            boolean hasPayerShoppingCart = extrasBundle.containsKey("payerShoppingCart");
            boolean hasPayerPosition = extrasBundle.containsKey("payerPosition");
            if (hasPayerName) {
                payerName = extrasBundle.getString("payerName");
                Log.i("Helper List", "The payer name is " + payerName);
            }
            if (hasPayerAddress) {
                payerAddress = extrasBundle.getString("payerAddress");
            }
            if (hasPayerShoppingCart) {
                Position = extrasBundle.getIntegerArrayList("payerShoppingCart");
            }
            if(hasPayerPosition){
                payerPosition = extrasBundle.getInt("payerPosition");
            }
        }

            //for (int i = 0; i<Position.size(); i++){
            //    selected_stuff.add(Groceries[Position.get(i)]);
            //}
        String json = "l";
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("tag", "user_grocery_list_select");
        accessBundle.put("user_id", sp.getString(payerName, ""));
        accessBundle.put("position_array", "");
        try{
            json = new AsyncConnection("UserGroceries").execute(accessBundle).get();
            Position = getSelectedArray(json);
            Log.d("PayerInfoJson", json);
        }
        catch(Exception e){e.printStackTrace();}


        try{
            accessBundle.clear();
            accessBundle.put("store_id", sp.getString(getString(R.string.store_id), ""));
            json = new AsyncConnection("Groceries").execute(accessBundle).get();
            Groceries = getGroceryNameArray(json);
            Prices = getPriceArray(json);
        }
        catch(Exception e){e.printStackTrace();}

        for(int i=0; i<Position.size(); i++){
            int m = Position.get(i);
            selected_stuff.add(Groceries[m]);
        }



        for (int i=0; i<Position.size(); i++ ){
            totalPay = totalPay+Prices[Position.get(i)];
            Log.i("PayerInfo", "Prices list is: "+Prices);
            Log.i("PayerInfo","totalPay is: "+totalPay);
        }

        String s = String.format("%.2f", totalPay);

        payerNameTV = (TextView) findViewById(R.id.payerName);
        payerAddressTV = (TextView) findViewById(R.id.payerAddress);
        totalPayTV = (TextView)findViewById(R.id.totalPayment);
        totalPayTV.setText("Total payment: $"+s);
        payerNameTV.setText(payerName);
        payerAddressTV.setText(payerAddress);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selected_stuff);
            setListAdapter(adapter);
            getListView().setOnItemClickListener(this);



    }

    //@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private static Double[] getPriceArray(String json){
        JSONArray groceries = null;
        List<Double> priceDoubleList = new ArrayList<Double>();
        try {
            groceries = new JSONArray(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Double[] result = null;
        for(int i=0;i<groceries.length();i++)
        {
            JSONObject index;
            try {
                index = groceries.getJSONObject(i);

                double priceDouble = index.getDouble("price");
                priceDoubleList.add(priceDouble);
                result = new Double[priceDoubleList.size()];
                result = priceDoubleList.toArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return result;
    }
    private static String[] getGroceryNameArray(String json){
        JSONArray groceries = null;
        List<String> nameList = new ArrayList<String>();

        try {
            groceries = new JSONArray(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] result = null;
        for(int i=0;i<groceries.length();i++)
        {
            JSONObject index;
            try {
                index = groceries.getJSONObject(i);
                String name = index.getString("name");
                nameList.add(name);
                result = new String[nameList.size()];
                result = nameList.toArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return result;
    }


    public ArrayList<Integer> getSelectedArray(String json) throws Exception{
        //user_grocery_list_add
        //remove
        //select
        ArrayList<Integer> positions = new ArrayList<>();
        JSONObject obj = new JSONObject(json);
        String list = obj.getString("position_array");
        for(String in:list.split(", ")){
            int blah = Integer.parseInt(in);
            positions.add(blah);
        }
        return positions;
    }
    public void removeHelper(){
        String payer_id = sp.getString(payerName, "");
        String helper_id = sp.getString(getString(R.string.user_id), "");
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("tag", "help_request_remove");
        accessBundle.put("helper_id", helper_id);
        accessBundle.put("request_help_id", payer_id);
        new AsyncConnection("HelperRequest").execute(accessBundle);
    }
    public void removeGroceries(){
        String user_id = sp.getString(payerName, "");
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("user_id", user_id);
        accessBundle.put("position_array", "");
        accessBundle.put("tag", "user_grocery_list_remove");
        new AsyncConnection("UserGroceries").execute(accessBundle);
    }

    public void completeOrder(View view) {
        removeHelper();
        removeGroceries();
        Intent intentBundle = new Intent(this, PayerListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("payerName", payerName);
        bundle.putInt("alreadySelectedPayer", payerPosition);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);
    }
}