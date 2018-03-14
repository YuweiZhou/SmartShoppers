package vivian.smartshoppers;

/**
 * Created by vivia on 2/8/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListAdapter extends BaseAdapter {

    String [] result;
    String [] result2;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;

    public CustomListAdapter(Activity mainActivity, String[] Groceries,String[] price, int[] prgmImages) {

        result = Groceries;
        result2 = price;
        context = mainActivity;
        imageId = prgmImages;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView tv2;
        ImageView img;
    }

    public View getView(final int position,View convertView,ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_fragment, null);
        holder.tv=(TextView) rowView.findViewById(R.id.grocery_title);
        holder.tv2=(TextView)rowView.findViewById(R.id.price);
        holder.img=(ImageView) rowView.findViewById(R.id.icon);
        holder.tv.setText(result[position]);
        holder.tv2.setText(result2[position]);
        holder.img.setImageResource(imageId[position]);
        /*
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, result[position] + " is added to your cart", Toast.LENGTH_LONG).show();
            }
        });
        */
        return rowView;

    };
}

