package com.amusoft.brewster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by irving on 8/6/15.
 */
public class BrewItemAdapter extends ArrayAdapter<BrewItem> {
    private ArrayList<BrewItem> objects;
    Context context;

    public BrewItemAdapter(Context context, int textViewResourceId,
                            ArrayList<BrewItem> objects) {
        super(context, textViewResourceId,textViewResourceId,objects);
        this.objects = objects;
        this.context = context;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.brew_item, null);
        }

        BrewItem i = objects.get(position);
        if (i != null) {

            TextView tvType = (TextView) v.findViewById(R.id.memType);
            TextView tvDescription = (TextView) v.findViewById(R.id.memDescription);
            TextView tvSender = (TextView) v.findViewById(R.id.memSender);
            ImageView imPhoto=(ImageView)v.findViewById(R.id.reportImage);

            if (tvType != null) {
                tvType.setText(i.getBTitle());
            }

            if (tvDescription != null) {
                tvDescription.setText(i.getBDescription());
            }
            if (tvSender!= null) {
                tvSender.setText("By :"+i.getBSender());
            }
            if(imPhoto!=null){
                if(i.getBPhoto() !=null){
                    byte[] imageAsBytes = Base64.decode(i.getBPhoto().getBytes(), Base64.DEFAULT);
                    imPhoto.setImageURI(null);
                    imPhoto.setImageBitmap(null);
                    imPhoto.setImageBitmap(
                            BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                    );

                }else if("-Jw7R2M88Bo-6AziZxB4".contentEquals(i.getBPhoto())){
                    imPhoto.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_launcher));

                }
                else {
                    imPhoto.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_launcher));
                }


            }

        }
        return v;
    }
}
