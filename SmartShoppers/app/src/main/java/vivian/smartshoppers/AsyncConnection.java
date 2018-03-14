package vivian.smartshoppers;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import vivian.smartshoppers.AccessFacade.GroceryStoreConnection;
import vivian.smartshoppers.AccessFacade.ServerConnect;

/**
 * Created by John on 2/24/2016.
 */
public class AsyncConnection extends AsyncTask<HashMap<String, String>, String, String> {
    String type;
    public final String groceryType = "Groceries";
    public final String groceryStoreType = "GroceryStore";
    public final String userType = "User";
    public final String userAvailType = "UserAvailable";
    public final String usernameType = "Username";
    public final String registerType = "Register";
    public final String helpReqType = "HelperRequest";
    public final String userGroceryType = "UserGroceries";

    public AsyncConnection(String type) {
        this.type = type;
    }

    public String doInBackground(HashMap<String, String>... Params) {
        Log.d("Aync", "HERE");
        HashMap<String, String> accessBundle = Params[0];
        if (type.equals(groceryStoreType))
            return new ServerConnect().getGroceryStoreData(accessBundle);
        if (type.equals(groceryType))
            return new ServerConnect().getGroceryData(accessBundle);
        if (type.equals(userType))
            return new ServerConnect().getUserData(accessBundle);
        if(type.equals(userAvailType))
            return new ServerConnect().updateUsersAvailableData(accessBundle);
        if(type.equals(usernameType))
            return new ServerConnect().getUsernameData(accessBundle);
        if(type.equals(registerType))
            return new ServerConnect().getRegisterData(accessBundle);
        if(type.equals(helpReqType))
            return new ServerConnect().getHelpRequestData(accessBundle);
        if(type.equals(userGroceryType))
            return new ServerConnect().getUserGroceryData(accessBundle);
        return "BAD INPUT TYPE";
    }
    public void onPostExecute(String result){
    }
}