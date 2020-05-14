package com.example.cinesaragon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cinesaragon.R;
import com.example.cinesaragon.model.Ticket;

import java.util.List;

public class MisEntradasAdapter extends ArrayAdapter<Ticket> {

    private Context context;

    public MisEntradasAdapter(Context context, List<Ticket> objects){

        super(context, R.layout.ticket_design,objects);
        this.context=context;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.ticket_design,null);
        }


        TextView type_text = v.findViewById(R.id.type_ticket);
        TextView cinema_text = v.findViewById(R.id.cinema_ticket);
        TextView movie_text = v.findViewById(R.id.movie_ticket);
        TextView date_text = v.findViewById(R.id.date_ticket);

        Ticket t = getItem(position);

        String type_string = "Entrada de " + t.getTipo();
        String movie_string = t.getPelicula();
        String cinema_string = "" + t.getCine();
        String date_string = t.getFecha();

        type_text.setText(type_string);
        movie_text.setText(movie_string);
        cinema_text.setText(cinema_string);
        date_text.setText(date_string);

        return v;
    }
}
