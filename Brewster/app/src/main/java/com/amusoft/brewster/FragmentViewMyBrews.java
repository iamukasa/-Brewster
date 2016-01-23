package com.amusoft.brewster;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by irving on 8/6/15.
 */
public class FragmentViewMyBrews extends Fragment {
    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    ArrayList<BrewItem> allMyBrews;
    ArrayList<BrewOnlineItem>allMyBrewOnline;
    ListView listAllMyBrews;
    BrewItemAdapter adapters;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mybrewsfrag= inflater.inflate(R.layout.fragment_my_brews, container, false);

        mBrewsterprefferences = getActivity().getSharedPreferences(Constants.BREWSTER_PREFERENCES,
                Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");
        allMyBrews=new ArrayList<BrewItem>();
        allMyBrewOnline=new ArrayList<BrewOnlineItem>();

        listAllMyBrews=(ListView)mybrewsfrag.findViewById(R.id.listallMybrews);
      adapters = new BrewItemAdapter(getActivity().getApplicationContext(),
                R.layout.brew_item, allMyBrews);


        Query queryRef = myFirebaseRef.child(Constants.FIREBASE_BREW).
                orderByChild(Constants.FIREBASE_BREW_POSTER).equalTo(
                mBrewsterprefferences.getString(Constants.CURRENT_LOGGED_IN,null));
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {

                    allMyBrews.add(new BrewItem(newPost.get(Constants.FIREBASE_BREW_TITLE).toString(),
                            newPost.get(Constants.FIREBASE_BREW_DESCRIPTION).toString(),
                            newPost.get(Constants.FIREBASE_BREW_PHOTOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_VIDEOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_POSTER).toString()));

                    allMyBrewOnline.add(new BrewOnlineItem(new BrewItem(newPost.get(Constants.FIREBASE_BREW_TITLE).toString(),
                            newPost.get(Constants.FIREBASE_BREW_DESCRIPTION).toString(),
                            newPost.get(Constants.FIREBASE_BREW_PHOTOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_VIDEOS).toString(),
                            newPost.get(Constants.FIREBASE_BREW_POSTER).toString()), snapshot.getKey()));



                    listAllMyBrews.setAdapter(adapters);

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

        listAllMyBrews.setAdapter(adapters);

        return mybrewsfrag;
    }
}
