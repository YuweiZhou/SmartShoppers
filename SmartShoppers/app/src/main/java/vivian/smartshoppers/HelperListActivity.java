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

public class HelperListActivity extends ListActivity implements AdapterView.OnItemClickListener{

    public boolean chooseHelper = false;
    public ArrayList<String> HelperList = new ArrayList<String>();
    public String helperName;
    public String payerName;
    public String helper_id;
    public String store_ID;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_list);

        sp = getSharedPreferences(getString(R.string.SPName), 0);
        edit = sp.edit();

        Log.i("Helper List", "The helper name is " + helperName);
        store_ID = sp.getString(getString(R.string.store_id), "");
        HashMap<String, String> accessBundle = new HashMap();
        HashMap<String,String> helperBundle = new HashMap();
        accessBundle.put("store_id", store_ID);
        accessBundle.put("user_id", "");
        Log.d("UserID", sp.getString("UserID", "-1"));
        accessBundle.put("tag", "users_select");
        try {
            String json = new AsyncConnection("UserAvailable").execute(accessBundle).get();
            JSONArray object = new JSONArray(json);
            for(int i=0;i<object.length();i++){
                helper_id = object.getJSONObject(i).getString("user_id");
                Log.d(Integer.toString(i), helper_id);
                helperBundle.put("user_id", helper_id);
                json = new AsyncConnection("Username").execute(helperBundle).get();
                JSONArray fin = new JSONArray(json);
                for(int j=0;j<fin.length();j++) {
                    JSONObject index = fin.getJSONObject(j);
                    helperName = index.getString("first_name") + " " + index.getString("last_name");
                    /*
                    if(!HelperList.contains(helperName)){
                        HelperList.add(helperName);
                        edit.putString(helperName, helper_id);
                        edit.apply();
                    }
                    */
                    HelperList.add(helperName);
                    Log.d(helperName, helper_id);
                    edit.putString(helperName, helper_id);
                    edit.commit();
                }
                helperBundle.clear();
            }
        }
        catch(Exception e){
            Log.d("lll", "STILL WRONG");
        }
        Log.d("END", "HERE");

        //Add dummy helpers for now
        //HelperList.add("Anne D. Ward");
        //HelperList.add("Ophelia R. Williams");
        //HelperList.add("Dorothy M. Miller\n");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, HelperList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    //@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        chooseHelper = true;
        String choseHelperName = HelperList.get(position);
        Log.d("helper chose", choseHelperName);
        Toast.makeText(this, "You selected: " + choseHelperName
                , Toast.LENGTH_SHORT).show();
        edit.putString("helperName", choseHelperName);
        edit.commit();
        helperName =choseHelperName;
        Log.d("helperName", choseHelperName);
    }


    public void GroceryListPage(View view) {

        if(chooseHelper == true){
            Intent intentBundle = new Intent(this, GroceryListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("payer_store_id", store_ID);
            bundle.putString("helperName", helperName );
            intentBundle.putExtras(bundle);
            startActivity(intentBundle);
        }
        else{
            Toast.makeText(this, "You haven't choose any helper yet"
                    , Toast.LENGTH_SHORT).show();
        }

    }

    public void refreshPage(View view) {
        Intent intentBundle = new Intent(this, HelperListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("helperName", helperName);
        bundle.putString("payerName", payerName);
        bundle.putString("helper_store_id",store_ID);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);
    }
}
