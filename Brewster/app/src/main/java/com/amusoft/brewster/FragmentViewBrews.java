package com.amusoft.brewster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by irving on 8/6/15.
 */
public class FragmentViewBrews extends Fragment {
    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    ArrayList<BrewItem> allBrews;
    ArrayList<BrewOnlineItem>allBrewOnline;
    ListView listAllBrews;
    BrewItemAdapter adapters;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View allbrewfrag= inflater.inflate(R.layout.fragment_view_all_brews, container, false);
        mBrewsterprefferences = getActivity().getSharedPreferences(Constants.BREWSTER_PREFERENCES,
                Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");
        allBrews=new ArrayList<BrewItem>();
        allBrewOnline=new ArrayList<BrewOnlineItem>();
        listAllBrews=(ListView)allbrewfrag.findViewById(R.id.listallbrews); adapters = new BrewItemAdapter(getActivity().getApplicationContext(),
                R.layout.brew_item,allBrews);
    adapters = new BrewItemAdapter(getActivity().getApplicationContext(),
                R.layout.brew_item,allBrews);

        myFirebaseRef.child(Constants.FIREBASE_BREW).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {

                    allBrews.add(new BrewItem(newPost.get(Constants.FIREBASE_BREW_TITLE).toString(),
                            newPost.get(Constants.FIREBASE_BREW_DESCRIPTION).toString(),
                            newPost.get(Constants.FIREBASE_BREW_PHOTOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_VIDEOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_POSTER).toString()));

                    allBrewOnline.add(new BrewOnlineItem(new BrewItem(newPost.get(Constants.FIREBASE_BREW_TITLE).toString(),
                            newPost.get(Constants.FIREBASE_BREW_DESCRIPTION).toString(),
                            newPost.get(Constants.FIREBASE_BREW_PHOTOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_VIDEOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_POSTER).toString()), snapshot.getKey()));

                    listAllBrews.setAdapter(adapters);


                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });
        listAllBrews.setAdapter(adapters);




        listAllBrews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent xbrew=new Intent(getActivity().getApplicationContext(),ViewSingleBrew.class);
                xbrew.putExtra(Constants.BREW_ITEM_CLICKED,
                        allBrewOnline.get(position).getBrewItemKey().toString());
                startActivity(xbrew);
            }
        });
        return allbrewfrag;
    }
}
