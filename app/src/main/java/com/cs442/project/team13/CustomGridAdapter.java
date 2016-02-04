package com.cs442.project.team13;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Priyanka on 12/10/2015.
 */
public class CustomGridAdapter extends BaseAdapter {
    private Context context;
    private final String[] gridValues;

    //Constructor to initialize values
    public CustomGridAdapter(Context context, String[ ] gridValues) {

        this.context        = context;
        this.gridValues     = gridValues;
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return gridValues.length;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        // LayoutInflator to call external grid_item.xml file

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from grid_item.xml ( Defined Below )

            gridView = inflater.inflate( R.layout.grid_item , null);

            // set value into textview

            TextView textView;
            textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);

            textView.setText(gridValues[position]);


            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);


                imageView.setImageResource(mThumbIds[position]);


        } else {

            gridView = (View) convertView;
        }

        return gridView;
    }
    private Integer[] mThumbIds = {
            R.drawable.categories1, R.drawable.sell,
            R.drawable.wishlist, R.drawable.profile,
            R.drawable.settings, R.drawable.logout,
    };
}
