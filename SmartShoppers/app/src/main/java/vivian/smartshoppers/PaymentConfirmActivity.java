package vivian.smartshoppers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentConfirmActivity extends AppCompatActivity {

    public double totalPay = 0;
    TextView tView;
    public int totalNum;
    public ArrayList<Integer> PositionList = new ArrayList<Integer>();
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences(getString(R.string.SPName), 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(!extrasBundle.isEmpty()){
            boolean hasTotalPay = extrasBundle.containsKey("totalPay");
            boolean hasSelectedStuff = extrasBundle.containsKey("selected_stuff");
            boolean hasTotalNum = extrasBundle.containsKey("totalNum");
            if(hasTotalPay){
                totalPay = extrasBundle.getDouble("totalPay");
                Log.i("Payment confirm", "The totalPay is " + totalPay);
            }
            if(hasTotalNum){
                totalNum = extrasBundle.getInt("totalNum");
            }
            if(hasSelectedStuff){
                PositionList = extrasBundle.getIntegerArrayList("selected_stuff");
            }
        }


        tView = (TextView)findViewById(R.id.confirmPayMoney);
        String s = String.format("%.2f", totalPay);
        tView.setText("$"+s);

    }
    public String getStringArray(ArrayList<Integer> selectedStuff){
        String list = "";
        for(int i=0;i<selectedStuff.size();i++){
            list += ", " + Integer.toString(selectedStuff.get(i));
        }
        list = list.replaceFirst(", ", "");
        return list;
    }
    public void addList(String list){
        String json ="l";
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("tag", "user_grocery_list_add");
        accessBundle.put("user_id", sp.getString(getString(R.string.user_id), ""));
        accessBundle.put("position_array", list);
        Log.d("list", list);
        try{
            json = new AsyncConnection("UserGroceries").execute(accessBundle).get();
            Log.d("list json", json);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public void addHelper(){
        String helperName = sp.getString("helperName", "");
        String helper_id = sp.getString(helperName, "");
        String user_id = sp.getString(getString(R.string.user_id), "");
        Log.d("helperName", helperName);
        Log.d("userid", user_id);
        Log.d("helperid", helper_id);
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("helper_id", helper_id);
        accessBundle.put("request_help_id", user_id);
        accessBundle.put("tag", "help_request_add");
        new AsyncConnection("HelperRequest").execute(accessBundle);
    }

    public void Comfirm_page(View view) {
        addHelper();
        addList(getStringArray(PositionList));
        Intent intent = new Intent(this, ConfirmPageActivity.class);
        startActivity(intent);
    }

    public void Back_shopping(View view) {
        Intent intentBundle = new Intent(view.getContext(), GroceryListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("CheckTotalPay", totalPay);
        bundle.putIntegerArrayList("CheckSelected_stuff", PositionList);
        bundle.putInt("CheckTotalNum", totalNum);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);
    }

}
