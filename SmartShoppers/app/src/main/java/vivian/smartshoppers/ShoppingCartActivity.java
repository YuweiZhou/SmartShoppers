package vivian.smartshoppers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {


    public static String [] Groceries={"Apple","Orange","Watermelon","Peach","Kiwi","Mango","Grape","Strawberry","Pear"};
    public static String [] price = {"$0.99/Lb.","$1.22/Lb.", "$4.00/ea", "$0.70/ea"
            , "$3.5/Lb.", "$1.20/ea", "$2.59/Lb.","$2.00/Lb.", "$1.50/Lb."};
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static Double[] PriceNum = {0.99, 1.22, 4.0, 0.7, 3.5, 1.2, 2.95, 2.0, 1.5};
    public ArrayList<Integer> selectedStuff = new ArrayList<Integer>();

    public ArrayList<String> ItemList = new ArrayList<String>();
    public ArrayList<String> PreItemList = new ArrayList<String>();

    public ArrayList<String> PriceList = new ArrayList<String>();
    public int totalNum;
    public double totalPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        sp = getSharedPreferences(getString(R.string.SPName), 0);
        edit = sp.edit();

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(!extrasBundle.isEmpty()){
            boolean hasSelectedGrocery = extrasBundle.containsKey("selected_stuff");
            boolean hasTotalPay = extrasBundle.containsKey("totalPay");

            if(hasSelectedGrocery){
                selectedStuff = extrasBundle.getIntegerArrayList("selected_stuff");
                Log.i("Shopping cart", "The selected grocery is " + selectedStuff);
            }
            if(hasTotalPay){
                totalPay = extrasBundle.getDouble("totalPay");
            }
        }
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("store_id", sp.getString(getString(R.string.store_id), ""));
        try {
            String json = new AsyncConnection("Groceries").execute(accessBundle).get();
            Groceries = getGroceryNameArray(json);
            PriceNum = getPriceArray(json);
            price = getPriceStringArray(json);
        }
        catch(Exception e){e.printStackTrace();}

        for(int i = 0; i<selectedStuff.size();i++){
            int position = selectedStuff.get(i);
            ItemList.add(Groceries[position]);
            PriceList.add(price[position]);

        }

        PreItemList = ItemList;
        //instantiate custom adapter
        ShoppingCartAdapter adapter = new ShoppingCartAdapter(this, ItemList, PriceList);
        adapter.notifyDataSetChanged();

        if(PreItemList != ItemList){
            this.onCreate(null);
        }
        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.SelectedGroceryView);
        lView.setAdapter(adapter);


    }

    public void Return_Grocery(View view) {

        selectedStuff.clear();
        for(int i = 0; i<ItemList.size(); i++){
            for(int j = 0; j< Groceries.length; j++){
                if(ItemList.get(i) == Groceries[j]){
                    selectedStuff.add(j);
                }
            }
        }

        totalNum = ItemList.size();
        totalPay = 0;
        for(int i = 0; i<selectedStuff.size();i++){
            int position = selectedStuff.get(i);
            totalPay += PriceNum[position];
        }


        Intent intentBundle = new Intent(this, GroceryListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("totalNum", totalNum);
        bundle.putDouble("alreadyTotalPay", totalPay);
        bundle.putIntegerArrayList("AlreadySelectedStuff", selectedStuff);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);
        Log.d("shopping cart: ", "selected stuff: " + selectedStuff);
        Log.i("Shopping cart", "totalNum is: " + totalNum);
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
                nameList.add("\"" + name + "\"");
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
