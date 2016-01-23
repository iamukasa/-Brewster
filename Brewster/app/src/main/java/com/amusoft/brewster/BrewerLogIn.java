package com.amusoft.brewster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class BrewerLogIn extends ActionBarActivity {
    static final int CREATE_ACCOUNT = 0;
    Firebase myFirebaseRef;
    SharedPreferences mBrewsterprefferences;
    Button btnlogin;
    EditText txtentEmail, txtentPassword;
    TextView goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewer_log_in);
        mBrewsterprefferences = getSharedPreferences(Constants.BREWSTER_PREFERENCES, Context.MODE_PRIVATE);

        Firebase.setAndroidContext(getApplicationContext());
        myFirebaseRef = new Firebase("https://brewster.firebaseio.com/");

        setToolBar();
        btnlogin = (Button) findViewById(R.id.loginadmin);
        txtentEmail = (EditText) findViewById(R.id.entEmail);
        txtentPassword = (EditText) findViewById(R.id.entPassword);
        goToSignUp = (TextView) findViewById(R.id.txtsignup);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String anEmail = txtentEmail.getText().toString();
                String aPass = txtentPassword.getText().toString();

                myFirebaseRef.authWithPassword(anEmail, aPass,
                        new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {

                                SharedPreferences.Editor editort = mBrewsterprefferences.edit();
                                editort.putString(Constants.CURRENT_LOGGED_IN, anEmail);
                                editort.commit();

                                Intent iu = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(iu);


                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                Toast.makeText(getApplicationContext(), "There is an error please try again", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrewerLogIn.this.showDialog(CREATE_ACCOUNT);

            }
        });
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAdminLogIn);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            setUpActionbar();
            getOverflowMenu();
            toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white_pure));


        }

    }

    private void setUpActionbar() {
        if (getSupportActionBar() != null) {
            ActionBar bar = getSupportActionBar();
            bar.setTitle(getResources().getString(R.string.app_name));
            bar.setHomeButtonEnabled(false);
            bar.setDisplayShowHomeEnabled(false);
            bar.setDisplayHomeAsUpEnabled(false);
            bar.setDisplayShowTitleEnabled(true);


            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        }


    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);

            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {

                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
                menuKeyField.isSynthetic();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        switch (id) {
            case CREATE_ACCOUNT:
                final LayoutInflater inflater3 =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutswrong =
                        inflater3.inflate(R.layout.dialog_new_admin_account,
                                (ViewGroup) findViewById(R.id.createAdmin));
                final EditText entemaildia = (EditText) layoutswrong.findViewById(R.id.diaentEmail);
                final EditText entidno = (EditText) layoutswrong.findViewById(R.id.idno);
                final EditText entpassworddiza = (EditText) layoutswrong.findViewById(R.id.diaentPassword);
                final EditText entCoonfirmpassworddiza = (EditText) layoutswrong.findViewById(R.id.diaconfirmPassword);
                final Button wrn2 = (Button) layoutswrong.findViewById(R.id.diacreateadmin);
                final TextView error = (TextView) layoutswrong.findViewById(R.id.error);

                error.setVisibility(View.INVISIBLE);
                entCoonfirmpassworddiza.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String strPass1 = entpassworddiza.getText().toString();
                        String strPass2 = entCoonfirmpassworddiza.getText().toString();
                        if (strPass1.equals(strPass2)) {
                            error.setVisibility(View.VISIBLE);
                            error.setTextColor(getResources().getColor(R.color.green));
                            error.setText("Passwords match");
                            wrn2.setVisibility(View.VISIBLE);
                        } else {
                            error.setVisibility(View.VISIBLE);

                            error.setTextColor(getResources().getColor(R.color.red));
                            error.setText("Passwords do not match");
                            wrn2.setVisibility(View.INVISIBLE);
                        }

                    }
                });
                wrn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String theemail = entemaildia.getText().toString();
                        String thepassword = entpassworddiza.getText().toString();
                        final String idno = entidno.getText().toString();
                        myFirebaseRef.createUser(theemail, thepassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                Map<String, Object> e = new HashMap<String, Object>();
                                e.put(Constants.FIREBASE_BREWER_ID, idno);
                                e.put(Constants.FIREBASE_BREWER_EMAIL, theemail);
                                myFirebaseRef.child(Constants.FIREBASE_BREWER).push().setValue(e);
                                entemaildia.setText("");
                                entCoonfirmpassworddiza.setText("");
                                entpassworddiza.setText("");
                                Toast.makeText(getApplicationContext(), "Successfully created user,You can now log in", Toast.LENGTH_SHORT).show();
                                BrewerLogIn.this.removeDialog(CREATE_ACCOUNT);


                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Toast.makeText(getApplicationContext(), "There is an error please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


                final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(layoutswrong);
                builder3.setTitle("Create Account");
                final AlertDialog lDialog11 = builder3.create();
                return lDialog11;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_brewer_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStaBREWSTERnt
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
