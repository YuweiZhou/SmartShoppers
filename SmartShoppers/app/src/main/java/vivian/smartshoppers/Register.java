package vivian.smartshoppers;

import android.content.Intent;
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
    String[] str = {"address", "first_name", "last_name", "email", "password", "user_type", "city", "state", "zip", "card_num"};
    UserLoginTask mAuthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                attemptReg();
            }
        });
        texts = new ArrayList<>();
        texts.add((EditText) findViewById(R.id.address));
        texts.add((EditText) findViewById(R.id.first_name));
        texts.add((EditText) findViewById(R.id.last_name));
        texts.add((EditText) findViewById(R.id.email_address));
        texts.add((EditText) findViewById(R.id.Password));
        texts.add((EditText) findViewById(R.id.user_type));
        texts.add((EditText) findViewById(R.id.city));
        texts.add((EditText) findViewById(R.id.state));
        texts.add((EditText) findViewById(R.id.zip));
        texts.add((EditText) findViewById(R.id.card_num));
    }
    public void attemptReg() {
        ArrayList<String> values = new ArrayList<>();
        values.add("tag=register");
        for(int i=0;i<str.length;i++)
        {
            Log.d("BS", values.get(i));
            values.add(str[i] + "=" + texts.get(i).getText().toString());
        }
        Log.d("task", "made");
        mAuthTask = new UserLoginTask(new Intent(this, MapsActivity.class), this, values);
        mAuthTask.execute((Void) null);
    }
    /*public boolean isValid(EditText in, int i){
        switch(i){
            case(0):
                //checking address through google is strange: http://stackoverflow.com/questions/682093/address-validation-using-google-maps-api
                break;
            case(1):
                break;
            case(2):
                break;
            case(3):
                break;
            case(4):
                break;
            case(5):
                break;
            case(6):
                break;
            case(7):
                break;
            case(8):
                break;
            case(9):
                break;
            default:
                break;
        }
        return true;
    }*/
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
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void Register_Page() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
