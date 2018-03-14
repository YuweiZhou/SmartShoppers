package vivian.smartshoppers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroceryListActivity extends Activity {
    public int totalNum = 0;
    TextView tView;
    ListView lv;
    Context context;

    public static int[] prgmImages = {R.drawable.apple,R.drawable.banana,R.drawable.broccoli,
            R.drawable.milk,R.drawable.esparagus, R.drawable.butter};
    public static String [] Groceries={"Apple","Orange","Watermelon","Peach","Kiwi","Mango","Grape","Strawberry","Pear"};
    public static String [] price = {"$0.99/Lb.","$1.22/Lb.", "$4.00/ea", "$0.70/ea"
            , "$3.5/Lb.", "$1.20/ea", "$2.59/Lb.","$2.00/Lb.", "$1.50/Lb."};
    public static Double[] PriceNum = {0.99, 1.22, 4.0, 0.7, 3.5, 1.2, 2.95, 2.0, 1.5};
    public double totalPay = 0;
    public Intent  intentBundle;
    public Bundle bundle = new Bundle();
    public ArrayList<Integer> PositionList = new ArrayList<Integer>();
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        sp = getSharedPreferences(getString(R.string.SPName), 0);
        edit = sp.edit();

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(!extrasBundle.isEmpty()){
            boolean hasTotalNum = extrasBundle.containsKey("totalNum");
            boolean hasAlreadySelectedStuff= extrasBundle.containsKey("AlreadySelectedStuff");
            boolean hasAlreadyTotalPay = extrasBundle.containsKey("alreadyTotalPay");
            boolean hasCheckTotalPay = extrasBundle.containsKey("CheckTotalPay");
            boolean hasCheckSelected_stuff = extrasBundle.containsKey("CheckSelected_stuff");
            boolean hasCheckTotalNum = extrasBundle.containsKey("CheckTotalNum");

            if(hasTotalNum){
                totalNum = extrasBundle.getInt("totalNum");
            }
            else if(hasCheckTotalNum){
                totalNum = extrasBundle.getInt("CheckTotalNum");
            }


            if(hasAlreadySelectedStuff){
                PositionList = extrasBundle.getIntegerArrayList("AlreadySelectedStuff");
            }
            else if(hasCheckSelected_stuff){
                PositionList = extrasBundle.getIntegerArrayList("CheckSelected_stuff");
            }


            if(hasAlreadyTotalPay){
                totalPay = extrasBundle.getDouble("alreadyTotalPay");
            }
            else if(hasCheckTotalPay){
                totalPay = extrasBundle.getDouble("CheckTotalPay");
            }

        }

        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("store_id", sp.getString(getString(R.string.store_id), ""));
        try {
            String json = new AsyncConnection("Groceries").execute(accessBundle).get();
            Log.d("Grocery Json", json);
            Groceries = getGroceryNameArray(json);
            PriceNum = getPriceArray(json);
            price = getPriceStringArray(json);
        }
        catch(Exception e){e.printStackTrace();}

        tView = (TextView)findViewById(R.id.item_amount);
        tView.setText(Integer.toString(totalNum));
        context = this;
        lv = (ListView) findViewById(R.id.GroceryListView);
        lv.setAdapter(new CustomListAdapter(this, Groceries, price, prgmImages));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("Grocery List", "I clicked an item");
                Toast.makeText(context, Groceries[position] + " is added to your cart", Toast.LENGTH_LONG).show();

                totalNum++;
                tView.setText(Integer.toString(totalNum));
                Log.i("Grocery List", "Selected grocery is: " + position);
                PositionList.add(position);

                totalPay += PriceNum[position];
                Log.i("Grocery List", "Pre-totalPay is: " + totalPay);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_grocery_list, menu);
        return true;
    }

    public void confirm_page(View view) {
        intentBundle = new Intent(view.getContext(), PaymentConfirmActivity.class);
        bundle.putDouble("totalPay", totalPay);
        bundle.putIntegerArrayList("selected_stuff", PositionList);
        bundle.putInt("totalNum", totalNum);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);

    }
    public void shopping_cart_page(View view) {
        intentBundle = new Intent(view.getContext(), ShoppingCartActivity.class);
        bundle.putIntegerArrayList("selected_stuff", PositionList);
        bundle.putDouble("totalPay", totalPay);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);
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
    private static String[] getPriceStringArray(String json){
        JSONArray groceries = null;
        List<String> priceStringList = new ArrayList<String>();
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

                String priceString = index.getString("price");
                priceStringList.add("\"" + "$" +  priceString + "/lb" + "\"");
                result = new String[priceStringList.size()];
                result = priceStringList.toArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return result;
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



}
