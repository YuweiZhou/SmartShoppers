package vivian.smartshoppers;
import junit.framework.*;
import java.util.HashMap;

import vivian.smartshoppers.AccessFacade.ServerConnect;

/**
 * Created by John on 2/26/2016.
 */
public class ServerConnectionTest {
    HashMap<String, String> accessBundle = new HashMap<>();
    ServerConnect conn = new ServerConnect();
    public void testUserConnection(){
        accessBundle.clear();
        accessBundle.put("email", "vivian199664@gmail.com");
        accessBundle.put("password", "12345");
        String json = conn.getGroceryStoreData(accessBundle);
        Assert.assertTrue(json.equals("{\"success\":1,\"user\":{\"user_id\":\"14\",\"email\":\"vivian199664@gmail.com\",\"user_type\":\"helper\"}}"));
    }
    public void testGroceryStoreConnection(){
        accessBundle.clear();
        accessBundle.put("lat", "39.956581");
        accessBundle.put("lng", "-75.187723");
        String json = conn.getGroceryStoreData(accessBundle);
        Assert.assertTrue(json.equals("[{\"store_id\":\"3\",\"name\":\"Wawa\",\"address\":\"3744 Spruce St\",\"city\":\"Philadelphia\",\"state\":\"PA\",\"lat\":\"39.9535686\",\"lng\":\"-75.1956537\"},{\"store_id\":\"2\",\"name\":\"Rodriguez Market\",\"address\":\"601 N 34th St\",\"city\":\"Philadelphia\",\"state\":\"PA\",\"lat\":\"39.9605836\",\"lng\":\"-75.1953104\"},{\"store_id\":\"1\",\"name\":\"7-Eleven\",\"address\":\"3440 Market S\",\"city\":\"Philadelphia\",\"state\":\"PA\",\"lat\":\"39.9558303\",\"lng\":\"-75.1993659\"},{\"store_id\":\"6\",\"name\":\"Martinez Grocery\",\"address\":\"401 N 40th St\",\"city\":\"Philadelphia\",\"state\":\"PA\",\"lat\":\"39.9596338\",\"lng\":\"-75.2019569\"},{\"store_id\":\"4\",\"name\":\"The Fresh Grocer\",\"address\":\"4001 Walnut St\",\"city\":\"Philadelphia\",\"state\":\"PA\",\"lat\":\"39.9544733\",\"lng\":\"-75.2024773\"},{\"store_id\":\"5\",\"name\":\"Supreme Shop n Bag\",\"address\":\"4301 Walnut St\",\"city\":\"Philadelphia\",\"state\":\"PA\",\"lat\":\"39.9562168\",\"lng\":\"-75.2070263\"}]"));
    }
    public void testGrocerConnectionTest(){
        accessBundle.clear();
    }
    public void testUserAvailableConnection(){
        accessBundle.clear();

    }
}
