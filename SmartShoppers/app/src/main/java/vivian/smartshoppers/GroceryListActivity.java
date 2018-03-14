package vivian.smartshoppers;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GroceryListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);


    }

    public void confirm_page(View view) {
        Intent intent = new Intent(this, ConfirmPageActivity.class);
        startActivity(intent);
    }

}
