package com.amusoft.brewster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.Map;


public class PublicProfileBrewer extends ActionBarActivity {
    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    ImageButton avatar;

    EditText profileUsername, profilename, profileID, profileschool, profilebio, profileskills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile_brewer);

        setToolBar();
        mBrewsterprefferences = getSharedPreferences(Constants.BREWSTER_PREFERENCES,
                Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");


        profileUsername = (EditText) findViewById(R.id.brewerpublicprofileusername);
        profilename = (EditText) findViewById(R.id.brewerpublicprofilename);
        profileID = (EditText) findViewById(R.id.brewerpublicprofileid);
        profileschool = (EditText) findViewById(R.id.brewerpublicprofileschool);
        profilebio = (EditText) findViewById(R.id.brewerpublicprofilebio);
        profileskills = (EditText) findViewById(R.id.brewerpublicprofileskills);
        profileUsername.setEnabled(false);
        profilename.setEnabled(false);
        profileID.setEnabled(false);
        profileschool.setEnabled(false);
        profilebio.setEnabled(false);
        profileskills.setEnabled(false);

        avatar = (ImageButton) findViewById(R.id.brewerpublicprofileavatar);
        Intent inte=getIntent();

        String user = inte.getExtras().getString("userpublic");


        Query queryRef = myFirebaseRef.child(Constants.FIREBASE_BREWER).orderByChild(Constants.FIREBASE_BREWER_EMAIL)
                .equalTo(user).limitToFirst(1);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                if (newPost != null) {
                    profileID.setText(newPost.get(Constants.FIREBASE_BREWER_ID).toString());

                    if (newPost.get(Constants.FIREBASE_BREWER_USERNAME) != null) {
                        profileUsername.setText(newPost.get(Constants.FIREBASE_BREWER_USERNAME).toString());


                    }
                    if (newPost.get(Constants.FIREBASE_BREWER_NAME) != null) {
                        profilename.setText(newPost.get(Constants.FIREBASE_BREWER_NAME).toString());

                    }
                    if (newPost.get(Constants.FIREBASE_BREWER_SCHOOL) != null) {
                        profileschool.setText(newPost.get(Constants.FIREBASE_BREWER_SCHOOL).toString());

                    }
                    if (newPost.get(Constants.FIREBASE_BREWER_SKILL) != null) {
                        profileskills.setText(newPost.get(Constants.FIREBASE_BREWER_SKILL).toString());

                    }
                    if (newPost.get(Constants.FIREBASE_BREWER_BIO) != null & avatar!=null) {
                        profilebio.setText(newPost.get(Constants.FIREBASE_BREWER_BIO).toString());

                    }
                    if (newPost.get(Constants.FIREBASE_BREWER_AVATAR) != null) {
                        byte[] imageAsBytes = Base64.decode(newPost.get(Constants.FIREBASE_BREWER_AVATAR).
                                        toString().getBytes(),
                                Base64.DEFAULT);
                        avatar.setImageURI(null);
                        avatar.setImageBitmap(null);
                        avatar.setImageBitmap(
                                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                        );

                    }

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

    }
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPublicProfile);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            setUpActionbar();

            toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white_pure));


        }

    }
    private void setUpActionbar() {
        if (getSupportActionBar() != null) {
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
        getMenuInflater().inflate(R.menu.menu_public_profile_brewer, menu);
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
        if (id == android.R.id.home) {
            Intent upIntent = new Intent(getApplicationContext(), PublicViewBrews.class);
            startActivity(upIntent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
