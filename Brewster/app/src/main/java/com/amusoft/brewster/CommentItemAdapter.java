package com.amusoft.brewster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by irving on 8/7/15.
 */
public class CommentItemAdapter extends ArrayAdapter<CommentItem> {

    private ArrayList<CommentItem> objects;
    Context context;

    public CommentItemAdapter(Context context, int textViewResourceId,
                           ArrayList<CommentItem> objects) {
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
            v = inflater.inflate(R.layout.activity_listview, null);
        }

        CommentItem i = objects.get(position);
        if (i != null) {

            TextView tvType = (TextView) v.findViewById(R.id.comment);
            TextView tvDescription = (TextView) v.findViewById(R.id.commentor);


            if (tvType != null) {
                tvType.setText(i.getsComment());
            }

            if (tvDescription != null) {
                tvDescription.setText(i.getsCommentor());
            }






        }
        return v;
    }
}
