package com.purpleorchestra.spinder.spindertec.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.purpleorchestra.spinder.spindertec.Templates.Deportes;

import java.util.ArrayList;

/**
 * Created by alichel_6288 on 30/04/16.
 */
public class SportsAdapter extends BaseAdapter{

    private ArrayList<Deportes> alDeportesByUser;
    private Context mContext;

    public SportsAdapter(Context c, ArrayList<Deportes> dep){
        mContext = c;
        alDeportesByUser = dep;
    }


    @Override
    public int getCount() {
        return alDeportesByUser.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        TextView txtView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            txtView = new TextView(mContext);
            txtView.setLayoutParams(new GridView.LayoutParams(85, 85));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            txtView.setPadding(8, 8, 8, 8);
        } else {
            txtView = (TextView) convertView;
        }

        txtView.setText(alDeportesByUser.get(i).name);

        return txtView;
    }
}
