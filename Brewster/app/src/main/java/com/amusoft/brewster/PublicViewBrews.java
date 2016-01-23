package com.amusoft.brewster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Map;


public class PublicViewBrews extends ActionBarActivity {
    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    ArrayList<BrewItem> allBrews;
    ArrayList<BrewOnlineItem>allBrewOnline;
    ListView listAllBrews;
    BrewItemAdapter adapters;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_view_brews);
        setToolBar();

        mBrewsterprefferences =getSharedPreferences(Constants.BREWSTER_PREFERENCES,
                Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");
        allBrews=new ArrayList<BrewItem>();
        allBrewOnline=new ArrayList<BrewOnlineItem>();
        listAllBrews=(ListView)findViewById(R.id.listallpublicbrews);
        adapters = new BrewItemAdapter(getApplicationContext(),
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



        listAllBrews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent xbrew = new Intent(getApplicationContext(), ViewSinglePublicBrew.class);
                xbrew.putExtra(Constants.BREW_ITEM_CLICKED,
                        allBrewOnline.get(position).getBrewItemKey().toString());
                startActivity(xbrew);
            }
        });

    }
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPubliceBrew);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            setUpActionbar();

            toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white_pure));






        }

    }

    private void setUpActionbar() {
        if(getSupportActionBar()!=null){
            ActionBar bar = getSupportActionBar();
            bar.setTitle(getResources().getString(R.string.app_name));
            bar.setHomeButtonEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(true);



            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_public_view_brews, menu);
        mSearchAction = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.searchbar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch(v);
                        return true;
                    }
                    return false;
                }
            });

            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));

            isSearchOpened = true;
        }
    }




    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
     super.onBackPressed();
    }

    private void doSearch(TextView v) {
        String type=v.getText().toString();
        Query queryRef = myFirebaseRef.child(Constants.FIREBASE_BREW).
                orderByChild(Constants.FIREBASE_BREW_TITLE).
                equalTo(type).limitToFirst(1);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {
                    allBrews.clear();
                    allBrewOnline.clear();


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

//
    }
}
