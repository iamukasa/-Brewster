package com.amusoft.brewster;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class EditBrewerProfile extends ActionBarActivity {
    static final int AVATAR_DIALOG_ID = 2;
    static final int TAKE_AVATAR_CAMERA_REQUEST = 1;
    static final int TAKE_AVATAR_GALLERY_REQUEST = 2;
    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    ImageButton avatar;
    Button updateprofile;
    EditText profileUsername, profilename, profileID, profileschool, profilebio, profileskills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brewer_profile);
        setToolBar();
        mBrewsterprefferences = getSharedPreferences(Constants.BREWSTER_PREFERENCES,
                Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");

        String user = mBrewsterprefferences.getString(Constants.CURRENT_LOGGED_IN, null);
        profileUsername = (EditText) findViewById(R.id.brewerprofileusername);
        profilename = (EditText) findViewById(R.id.brewerprofilename);
        profileID = (EditText) findViewById(R.id.brewerprofileid);
        profileschool = (EditText) findViewById(R.id.brewerprofileschool);
        profilebio = (EditText) findViewById(R.id.brewerprofilebio);
        profileskills = (EditText) findViewById(R.id.brewerprofileskills);
        updateprofile = (Button) findViewById(R.id.btnbrewerprofileupdate);
        avatar = (ImageButton) findViewById(R.id.brewerprofileavatar);


        Query queryRef = myFirebaseRef.child(Constants.FIREBASE_BREWER).
                orderByChild(Constants.FIREBASE_BREWER_EMAIL)
                .equalTo(user).limitToFirst(1);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 String keyupdate = dataSnapshot.getKey().toString();
                SharedPreferences.Editor editorkey = mBrewsterprefferences.edit();
                editorkey.putString(Constants.CURRENT_PROFILE_KEY, keyupdate);
                editorkey.commit();

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
                    if (newPost.get(Constants.FIREBASE_BREWER_BIO) != null) {
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
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBrewerProfile.this.showDialog(AVATAR_DIALOG_ID);
            }
        });
        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> profile=new HashMap<String, Object>();
                profile.put(Constants.FIREBASE_BREWER_AVATAR,
                        mBrewsterprefferences.getString(Constants.CURRENT_AVATAR, null));
                profile.put(Constants.FIREBASE_BREWER_EMAIL,
                        mBrewsterprefferences.getString(Constants.CURRENT_LOGGED_IN,null));
                profile.put(Constants.FIREBASE_BREWER_ID,profileID.getText().toString());
                profile.put(Constants.FIREBASE_BREWER_NAME,profilename.getText().toString());
                profile.put(Constants.FIREBASE_BREWER_USERNAME,profileUsername.getText().toString());
                profile.put(Constants.FIREBASE_BREWER_SCHOOL,profileschool.getText().toString());
                profile.put(Constants.FIREBASE_BREWER_SKILL,profileskills.getText().toString());
                profile.put(Constants.FIREBASE_BREWER_BIO,profilebio.getText().toString());
                Map<String,Object> brewer=new HashMap<String, Object>();
                brewer.put(mBrewsterprefferences.getString(Constants.CURRENT_PROFILE_KEY,null)
                        ,profile);

                myFirebaseRef.child(Constants.FIREBASE_BREWER).updateChildren(brewer);
                Toast.makeText(getApplicationContext(),"Sucessfully updated your profle",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEditProfile);
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_AVATAR_CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) {
                    // Avatar camera mode was canceled.
                } else if (resultCode == Activity.RESULT_OK) {
                    // TODO: HANDLE PHOTO TAKEN
                    Bitmap cameraPic = (Bitmap) data.getExtras().get("data");
                    saveAvatar(cameraPic);
                }
                break;
            case TAKE_AVATAR_GALLERY_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) {
                    // Avatar gallery request mode was canceled.
                } else if (resultCode == Activity.RESULT_OK) {
                    // TODO: HANDLE IMAGE CHOSEN
                    Uri photoUri = data.getData();
                    try {
                        Bitmap galleryPic = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                        saveAvatar(galleryPic);
                        saveAvatar(galleryPic);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void saveAvatar(Bitmap bitavatar) {
// TODO: Save the Bitmap as a local file called avatar.jpg
        String strAvatarFilename = "avatar.jpg";
        try {
            bitavatar.compress(Bitmap.CompressFormat.JPEG,
                    100, openFileOutput(strAvatarFilename, MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

// TODO: Determine the Uri to the local avatar.jpg file

// TODO: Save the Uri path as a String preference
        Uri imageUri = Uri.fromFile(new File(getFilesDir(), strAvatarFilename));
// TODO: Update the ImageButton with the new image
        avatar.setImageBitmap(null);
        avatar.setImageDrawable(null);
        avatar.setImageBitmap(bitavatar);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitavatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        SharedPreferences.Editor editorwa = mBrewsterprefferences.edit();
        editorwa.putString(Constants.CURRENT_AVATAR, image);
        editorwa.commit();


    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case AVATAR_DIALOG_ID:
                LayoutInflater inflater2 =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout2 =
                        inflater2.inflate(R.layout.avatar_picker,
                                (ViewGroup) findViewById(R.id.chooseAva));
                final Button btnCamera = (Button) layout2.findViewById(R.id.camera);
                final Button btnGallery = (Button) layout2.findViewById(R.id.gallery);
                btnCamera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent pictureIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(pictureIntent, TAKE_AVATAR_CAMERA_REQUEST);
                        EditBrewerProfile.this
                                .removeDialog(AVATAR_DIALOG_ID);


                    }
                });
                btnGallery.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                        pickPhoto.setType("image/*");
                        startActivityForResult(pickPhoto, TAKE_AVATAR_GALLERY_REQUEST);
                        EditBrewerProfile.this
                                .removeDialog(AVATAR_DIALOG_ID);

                    }
                });

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(layout2);
                builder2.setTitle("pick photo");
                builder2.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                EditBrewerProfile.this
                                        .removeDialog(AVATAR_DIALOG_ID);

                            }
                        });

                builder2.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditBrewerProfile.this
                                        .removeDialog(AVATAR_DIALOG_ID);
                            }
                        });

                AlertDialog chooseDialog = builder2.create();
                return chooseDialog;

        }


        return null;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_brewer_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent upIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(upIntent);
            finish();

        }


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
