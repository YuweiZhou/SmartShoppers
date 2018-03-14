package vivian.smartshoppers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.Toast;



public class GroceryListFragment extends ListFragment implements AdapterView.OnItemClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Groceries, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    //@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] GroceryList = getResources().getStringArray(R.array.Groceries);
        String Selecteditem= GroceryList[+position];
        Toast.makeText(getActivity(), Selecteditem + " is added to your cart!"
                , Toast.LENGTH_SHORT).show();
        }


}
