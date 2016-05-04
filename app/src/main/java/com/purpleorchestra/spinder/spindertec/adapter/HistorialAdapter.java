package com.purpleorchestra.spinder.spindertec.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.purpleorchestra.spinder.spindertec.R;
import com.purpleorchestra.spinder.spindertec.Templates.Reservation;

import java.util.ArrayList;

/**
 * Created by alichel_6288 on 02/05/16.
 */
public class HistorialAdapter extends BaseAdapter {

    private ArrayList<Reservation> alHistoryByUser; //Las reservaciones del usuario que se está consultado
    private static LayoutInflater inflater=null;
    private Activity activity;


    public HistorialAdapter(Activity c, ArrayList<Reservation> alHisty){
        //mContext = c;
        activity = c;
        alHistoryByUser = alHisty;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return alHistoryByUser.size();
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
        View vi = convertView;

        if (convertView==null){
            vi = inflater.inflate(R.layout.list_row, null);
            TextView opponent = (TextView)vi.findViewById(R.id.txtOpponent);
            TextView sport = (TextView)vi.findViewById(R.id.txtSport);
            TextView facility = (TextView)vi.findViewById(R.id.txtFacility);
            TextView scheduleDate = (TextView)vi.findViewById(R.id.txtDate);
            TextView scheduleTime = (TextView)vi.findViewById(R.id.txtTime);

            opponent.setText(alHistoryByUser.get(i).reservationOpponent);
            sport.setText(alHistoryByUser.get(i).reservationSport);
            facility.setText(alHistoryByUser.get(i).reservationFacility);
            scheduleDate.setText(alHistoryByUser.get(i).reservationScheduleDate);
            scheduleTime.setText(alHistoryByUser.get(i).reservationScheduleTime);
        }
        return vi;
    }
}
