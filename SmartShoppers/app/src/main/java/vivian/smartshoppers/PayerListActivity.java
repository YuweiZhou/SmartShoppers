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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PayerListActivity extends ListActivity implements AdapterView.OnItemClickListener{

    public ArrayList<String> PayerList = new ArrayList<String>();
    public String ChosenPayer;
    public String helperName;
    public String payerName;
    public String payer_store_ID;
    public String helper_store_ID;
    public int payerPosition;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer_list);

        sp = getSharedPreferences(getString(R.string.SPName), 0);
        edit = sp.edit();

        //Should get all store IDs and corresponding helper names. Check for
        //The payer store ID and display the helpers under that ID list
        helper_store_ID = sp.getString("helper_store_id", "");
        Log.i("Helper List", "The helper store ID is " + helper_store_ID);
        payer_store_ID = sp.getString("payer_store_id", "");
        Log.i("Helper List", "The payer store ID is " + payer_store_ID);

        //Dummy payers set here
        //PayerList.add("Carmen D. Burton");
        //PayerList.add("Rolanda R. Brown");

        HashMap<String, String> accessBundle = new HashMap<>();
        HashMap<String,String> helperBundle = new HashMap<>();
        accessBundle.put("request_help_id", "");
        accessBundle.put("helper_id", sp.getString(getString(R.string.user_id), "1"));
        accessBundle.put("tag", "requests_select");
        try {
            String json = new AsyncConnection("HelperRequest").execute(accessBundle).get();
            Log.d("js", json);
            JSONArray object = new JSONArray(json);
            for(int i=0;i<object.length();i++){
                String payer_id = object.getJSONObject(i).getString("request_help_id");
                helperBundle.put("user_id", payer_id);
                json = new AsyncConnection("Username").execute(helperBundle).get();
                JSONArray fin = new JSONArray(json);
                for(int j=0;j<fin.length();j++) {
                    JSONObject index = fin.getJSONObject(j);
                    payerName = index.getString("first_name") + " " + index.getString("last_name");
                    /*
                    if(!PayerList.contains(payerName)) {
                        PayerList.add(payerName);
                    }
                    */
                    Log.d(Integer.toString(i), payerName);
                    PayerList.add(payerName);
                    edit.putString(payerName, payer_id);
                    edit.commit();
                }
                Log.i("PayerList", "pre-payer list: "+PayerList);
                helperBundle.clear();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Log.i("PayerList", "payer list: "+PayerList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PayerList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    //@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChosenPayer = (PayerList.get(position));
        payerPosition = position;
        //dummy shopping cart

        Intent intentBundle = new Intent(this, Payer_Info.class);
        Bundle bundle = new Bundle();
        bundle.putString("payerName", ChosenPayer);
        bundle.putString("payerAddress", "3400 Lancaster Ave");
        bundle.putInt("payerPosition", payerPosition);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);
    }
    public void homePage(View view) {
        Intent intentBundle = new Intent(this, MapsActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt("store_id", 1);
            intentBundle.putExtras(bundle);

        startActivity(intentBundle);
    }
    public void logOut(View view) {
        Intent intent = new Intent(this, ConfirmPageActivity.class);
        startActivity(intent);
    }
    public void refreshPage(View view) {
        Intent intentBundle = new Intent(this, PayerListActivity.class);
        startActivity(intentBundle);
    }
}
