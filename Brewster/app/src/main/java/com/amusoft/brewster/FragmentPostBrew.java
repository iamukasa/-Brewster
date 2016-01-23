package com.amusoft.brewster;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by irving on 8/6/15.
 */
public class FragmentPostBrew extends Fragment {

    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    EditText bTitle, bDescription, bYoutube;
    Button bPost;
    ImageButton photo;
    static final int AVATAR_DIALOG_ID = 2;
    static final int TAKE_AVATAR_CAMERA_REQUEST = 1;
    static final int TAKE_AVATAR_GALLERY_REQUEST = 2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View postbrewfrag = inflater.inflate(R.layout.fragment_post_brew, container, false);
        mBrewsterprefferences = getActivity().getSharedPreferences(Constants.BREWSTER_PREFERENCES,
                Context.MODE_PRIVATE);
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");
        bTitle=(EditText)postbrewfrag.findViewById(R.id.brewTitle);
        bDescription=(EditText)postbrewfrag.findViewById(R.id.brewDescription);
        bYoutube=(EditText)postbrewfrag.findViewById(R.id.brewvideo);
        bPost=(Button)postbrewfrag.findViewById(R.id.postBrew);
        photo=(ImageButton)postbrewfrag.findViewById(R.id.postImage);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, TAKE_AVATAR_GALLERY_REQUEST);


            }
        });

        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> postBrew= new  HashMap<String,Object>();
                postBrew.put(Constants.FIREBASE_BREW_TITLE,
                        bTitle.getText().toString());
                postBrew.put(Constants.FIREBASE_BREW_DESCRIPTION,
                        bDescription.getText().toString());
                postBrew.put(Constants.FIREBASE_BREW_VIDEOS,
                        bYoutube.getText().toString());
                postBrew.put(Constants.FIREBASE_BREW_PHOTOS,
                        mBrewsterprefferences.getString(Constants.CURRENT_IMAGE, null));
                postBrew.put(Constants.FIREBASE_BREW_POSTER,
                        mBrewsterprefferences.getString(Constants.CURRENT_LOGGED_IN, null));
                myFirebaseRef.child(Constants.FIREBASE_BREW).push().setValue(postBrew);
                bTitle.setText("");
   bTitle.setText("");
   bDescription.setText("");
   bYoutube.setText("");
                photo.setImageDrawable(null);
                photo.setImageBitmap(null);
                photo.setImageURI(null);
                photo.setImageDrawable(getResources().getDrawable(R.drawable.index));
                Toast.makeText(getActivity().getApplicationContext(),
                        "Successfully Submitted your brew", Toast.LENGTH_SHORT).show();





            }
        });




        return postbrewfrag;
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
                        Bitmap galleryPic = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
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

    private void saveAvatar(Bitmap avatar) {
// TODO: Save the Bitmap as a local file called avatar.jpg
        String strAvatarFilename = "avatar.jpg";
        try {
            avatar.compress(Bitmap.CompressFormat.JPEG,
                    100, getActivity().openFileOutput(strAvatarFilename, getActivity().MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

// TODO: Determine the Uri to the local avatar.jpg file

// TODO: Save the Uri path as a String preference
        Uri imageUri = Uri.fromFile(new File(getActivity().getFilesDir(), strAvatarFilename));
// TODO: Update the ImageButton with the new image
        photo.setImageBitmap(null);
        photo.setImageDrawable(null);
        photo.setImageBitmap(avatar);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        SharedPreferences.Editor editorwa = mBrewsterprefferences.edit();
        editorwa.putString(Constants.CURRENT_IMAGE, image);
        editorwa.commit();


    }
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case AVATAR_DIALOG_ID:
                LayoutInflater inflater2 =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout2 =
                        inflater2.inflate(R.layout.avatar_picker,
                                (ViewGroup)getActivity().findViewById(R.id.chooseAva));
                final Button btnCamera = (Button) layout2.findViewById(R.id.camera);
                final Button btnGallery = (Button) layout2.findViewById(R.id.gallery);
                btnCamera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent pictureIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(pictureIntent, TAKE_AVATAR_CAMERA_REQUEST);
                        getActivity()
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
                        getActivity()
                                .removeDialog(AVATAR_DIALOG_ID);

                    }
                });

                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setView(layout2);
                builder2.setTitle("pick photo");
                builder2.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity()
                                        .removeDialog(AVATAR_DIALOG_ID);

                            }
                        });

                builder2.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getActivity()
                                        .removeDialog(AVATAR_DIALOG_ID);
                            }
                        });

                AlertDialog chooseDialog = builder2.create();
                return chooseDialog;

        }


        return null;
    }

}
