package vivian.smartshoppers.AccessFacade;
import java.util.HashMap;
/**
 * Created by Vincent on 2/19/2016.
 */
public class ServerConnect {
    private DataConnection dataConnection;
    /*
                HashMap accessBundle = new HashMap<String, String>();
                accessBundle.put("email", mEmail);

                ServerConnect user = new ServerConnect();
                String js = user.getUserData(accessBundle);
     */
    public String getUserData(HashMap<String, String> accessBundle){
        DataConnection userConnect = new UserConnection();
        String result =  userConnect.getData(accessBundle);
        return result;
    }
    public String getGroceryData(HashMap<String, String> accessBundle){
        DataConnection groceriesConnect = new GroceriesConnection();
        String result =  groceriesConnect.getData(accessBundle);
        return result;
    }
    public String getGroceryStoreData(HashMap<String, String> accessBundle){
        DataConnection storeConnect = new GroceryStoreConnection();
        String result =  storeConnect.getData(accessBundle);
        return result;
    }
    public String updateUsersAvailableData(HashMap<String, String> accessBundle){
        DataConnection usersAvailableConnect = new UsersAvailableConnection();
        String result =  usersAvailableConnect.getData(accessBundle);
        return result;
    }
    public String getRegisterData(HashMap<String, String> accessBundle){
        DataConnection registerConnect = new RegisterConnection();
        String result =  registerConnect.getData(accessBundle);
        return result;
    }
    public String getUsernameData(HashMap<String, String> accessBundle){
        DataConnection usernameConnect = new UsernameConnection();
        String result = usernameConnect.getData(accessBundle);
        return result;
    }
    public String getHelpRequestData(HashMap<String, String> accessBundle){
        DataConnection helperRequestConnect = new HelpRequestsConnection();
        String result = helperRequestConnect.getData(accessBundle);
        return result;
    }
    public String getUserGroceryData(HashMap<String, String> accessBundle){
        DataConnection userGroceryData = new UserGroceriesConnection();
        String result = userGroceryData.getData(accessBundle);
        return result;
    }
}

