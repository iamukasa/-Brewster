package com.amusoft.brewster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Map;


public class ViewSinglePublicBrew extends ActionBarActivity {
    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    TextView singlePoster,singleTitle,singleDescription;
    Button singleYoutube;
    ListView Comments;
    EditText addComent;
    ImageView imPhoto;
    ArrayList<CommentItem> arrayComments;
    CommentItemAdapter adapter;
    ListView listView;
    String youtube;
    String posterprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_public_brew);
        setToolBar();
        mBrewsterprefferences = getSharedPreferences(Constants.BREWSTER_PREFERENCES,
                Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");
        singlePoster=(TextView)findViewById(R.id.singlepublicPoster);
        singleTitle=(TextView)findViewById(R.id.singlepublicTitle);
        singleDescription=(TextView)findViewById(R.id.singlepublicDescription);
        imPhoto=(ImageView)findViewById(R.id.showSinglepublicBrewImage);
        singleYoutube=(Button)findViewById(R.id.singlepublicButtonYoutube);
        singleYoutube.setVisibility(View.INVISIBLE);
        arrayComments=new ArrayList<CommentItem>();




        Intent i=getIntent();
        final String key=i.getExtras().getString(Constants.BREW_ITEM_CLICKED);
        Comments = (ListView) findViewById(R.id.listView_comments);

        adapter =new CommentItemAdapter(getApplicationContext(),
                R.layout.activity_listview,arrayComments);






        listView= (ListView) findViewById(R.id.listView_publiccomments);
        listView.setAdapter(adapter);

        addComent = (EditText) findViewById(R.id.chat_publiceditText);
        addComent.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    final String question = addComent.getText().toString();
                    adapter.add(new CommentItem(question,
                            "anonymus"));
                    addComent.setText("");


                    return true;
                }

                return false;
            }
        });






        Query queryRef = myFirebaseRef.child(Constants.FIREBASE_BREW).orderByKey()
                .equalTo(key);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                if (newPost != null) {
                    singlePoster.setText("By : "+newPost.get(Constants.FIREBASE_BREW_POSTER).toString());
                    posterprofile=newPost.get(Constants.FIREBASE_BREW_POSTER).toString();
                    singleTitle.setText(newPost.get(Constants.FIREBASE_BREW_TITLE).toString());
                    singleDescription.setText(newPost.get(Constants.FIREBASE_BREW_DESCRIPTION).toString());
                    String photoBrew=newPost.get(Constants.FIREBASE_BREW_PHOTOS).toString();
                    if(photoBrew !=null){
                        byte[] imageAsBytes = Base64.decode(photoBrew.getBytes(), Base64.DEFAULT);
                        imPhoto.setImageURI(null);
                        imPhoto.setImageBitmap(null);
                        imPhoto.setImageBitmap(
                                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                        );
                    }
                 youtube=newPost.get(Constants.FIREBASE_BREW_VIDEOS).toString();

                    singleYoutube.setVisibility(View.VISIBLE);





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
        singlePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(getApplicationContext(), PublicProfileBrewer.class);
                inte.putExtra("userpublic", posterprofile);
                startActivity(inte);
            }
        });
        singleYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri e = Uri.parse(youtube);
                Intent videoClient = new Intent(Intent.ACTION_VIEW, e);


                startActivity(videoClient);
            }
        });

    }
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewSinglePublicBrew);
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
        getMenuInflater().inflate(R.menu.menu_view_single_public_brew, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
