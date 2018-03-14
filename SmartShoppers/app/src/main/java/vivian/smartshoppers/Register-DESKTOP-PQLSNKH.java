package vivian.smartshoppers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    ArrayList<EditText> texts;
    String[] str = {"address", "first_name", "last_name", "email", "password", "city", "state", "zip", "card_num"};
    AsyncConnection mAuthTask;
    public String name;
    public String user_id;
    View focusView;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = getSharedPreferences(getString(R.string.SPName), 0);
        edit = sp.edit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        Button mRegisterReadyButton = (Button) findViewById(R.id.register_ready_button);
        mRegisterReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cont = true;

                for(int i=0;i<texts.size();i++){
                    if(!isValid(texts.get(i), i)){
                        cont=false;
                        focusView = texts.get(i);
                        break;
                    }
                }
                if (cont) {
                    attemptReg();
                }else{
                    focusView.requestFocus();
                }

            }
        });
        texts = new ArrayList<>();
        texts.add((EditText) findViewById(R.id.address));
        texts.add((EditText) findViewById(R.id.first_name));
        texts.add((EditText) findViewById(R.id.last_name));
        texts.add((EditText) findViewById(R.id.email_address));
        texts.add((EditText) findViewById(R.id.Password));
        texts.add((EditText) findViewById(R.id.city));
        texts.add((EditText) findViewById(R.id.state));
        texts.add((EditText) findViewById(R.id.zip));
        texts.add((EditText) findViewById(R.id.card_num));


    }
    public void attemptReg() {
        HashMap<String, String> accessBundle = new HashMap<>();
        for(int i=0;i<str.length;i++)
        {
            accessBundle.put(str[i], texts.get(i).getText().toString());
        }
        String js = "Fail";
        mAuthTask = new AsyncConnection("Register");
        try {
            js = mAuthTask.execute(accessBundle).get();
            Log.d("js", js);
            if(js.contains("success")) {
                JSONObject json = new JSONObject(js);
                name = json.getJSONObject("user").getString("first_name");
                name += " " + json.getJSONObject("user").getString("last_name");
                user_id = json.getJSONObject("user").getString("user_id");
                edit.putString(getString(R.string.username), name);
                edit.putString(getString(R.string.user_id), user_id);
                edit.commit();
                Home_Screen();
            }
            else{
                Toast.makeText(getApplicationContext(), "The entered credentials could not be registered.",
                        Toast.LENGTH_SHORT).show();
                }
            }
        catch (Exception e){e.printStackTrace();}
    }

    public boolean isValid(EditText in, int i){
        String put = in.getText().toString();
        switch(i){
            case(3):
                if(!ValidityChecker.isEmailValid(put)){
                    in.setError("The email is invalid.");
                    return false;
                }
                break;
            case(4):
                //if(!ValidityChecker.isPasswordValid(put)){
                //    in.setError("");
                //    return false;
                //}
                break;
            case(9):
                if(!ValidityChecker.isCardNumValid(put)){
                    in.setError("The credit card number needs to be 16 digits.");
                    return false;
                }
                break;
            default:
                break;
        }
        /*if(!ValidityChecker.isStringValid(put)){
            in.setError("");
            return true;
        }*/
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void Home_Screen(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void Home_Screen(View view) {
        Intent intent = new Intent(view.getContext(), MapsActivity.class);
        startActivity(intent);
    }

}