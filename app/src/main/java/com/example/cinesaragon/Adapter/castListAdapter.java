package com.example.cinesaragon.Adapter;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cinesaragon.R;
import com.example.cinesaragon.model.cast;

import java.util.List;



public class castListAdapter extends ArrayAdapter<cast> {

    private Context context;

    public castListAdapter(Context context, List<cast> objects){
        super(context, R.layout.castcrewlistitemdesign,objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v= convertView;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        if(v == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.castcrewlistitemdesign,null);
        }

        TextView name=(TextView) v.findViewById(R.id.actorName);
        TextView roleAs =(TextView) v.findViewById(R.id.actorAs);
        ImageView image=(ImageView) v.findViewById(R.id.actorImg);

        cast person = getItem(position);

        name.setText(person.getName());
        roleAs.setText(person.getCharacter());

        String imgPath= "https://image.tmdb.org/t/p/w92"+person.getImagePath();
        //Drawable drawable = LoadImageFromWebOperations(imgPath.toString());
        //image.setImageDrawable(drawable);

        if(person.getImagePath().equals("null")){
            Glide.with(context)
                    .load("http://mariabiju.com/wp-content/themes/bazar/core/assets/images/no-featured-175.jpg")
                    .into(image);
        }
        else{
            Glide.with(context)
                    .load(imgPath)
                    .into(image);
        }

        return v;
    }

    /*private Drawable LoadImageFromWebOperations(String url) {

        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }

    }*/

}

