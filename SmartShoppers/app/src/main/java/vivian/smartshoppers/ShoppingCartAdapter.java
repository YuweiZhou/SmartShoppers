package vivian.smartshoppers;

/**
 * Created by vivia on 2/8/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShoppingCartAdapter extends BaseAdapter {

    public ArrayList<String> result = new ArrayList<String>();
    public ArrayList<String> result2 = new ArrayList<String>();
    Context context;
    public static String [] Groceries={"Apple","Orange","Watermelon","Peach","Kiwi","Mango","Grape","Strawberry","Pear"};
    public static String [] price = {"$0.99/Lb.","$1.22/Lb.", "$4.00/ea", "$0.70/ea"
            , "$3.5/Lb.", "$1.20/ea", "$2.59/Lb.","$2.00/Lb.", "$1.50/Lb."};

    public static double[] PriceNum = {0.99, 1.22, 4.0, 0.7, 3.5, 1.2, 2.95, 2.0, 1.5};
    public ArrayList<Integer> selectedStuff = new ArrayList<Integer>();
    public int totalNum;
    public double totalPay = 0;

    private static LayoutInflater inflater=null;

    public ShoppingCartAdapter(Context thiscontext, ArrayList<String> SelectedGroceries,ArrayList<String> SelectedPrice) {

        result = SelectedGroceries;
        result2 = SelectedPrice;
        context =thiscontext ;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView selectedGrocery;
        TextView selectedPrice;
        ImageButton deleteBtn;

    }

    public View getView(final int position,View convertView,ViewGroup parent) {
        View view;
        Holder holder=new Holder();
        view = inflater.inflate(R.layout.custom_fragment_cart,null);

        holder.selectedGrocery = (TextView)view.findViewById(R.id.selected_grocery);
        holder.selectedGrocery.setText(result.get(position));

        holder.selectedPrice = (TextView)view.findViewById(R.id.selected_price);
        holder.selectedPrice.setText(result2.get(position));


        Log.d("Cart Addapter", "the clicked position is: " + position);

        holder.deleteBtn = (ImageButton)view.findViewById(R.id.delete_icon);


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                result.remove(position);
                result2.remove(position);//or some other task
                notifyDataSetChanged();
                Log.d("shopping cart Adapter", "ItemList: " + result);

            }
        });


        return view;

    }



}

