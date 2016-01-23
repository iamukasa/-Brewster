package com.amusoft.brewster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SplashScreen extends ActionBarActivity {
    static final int CHOOSE_REG=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                // After 5 seconds redirect to another intent
                SplashScreen.this.showDialog(CHOOSE_REG);


            }
        }, 3 * 1000); // wait for 3 seconds
    }
    @Override
    protected Dialog onCreateDialog(final int id){
        switch(id){
            case CHOOSE_REG:
                final LayoutInflater inflater3 =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutswrong =
                        inflater3.inflate(R.layout.choose_use,
                                (ViewGroup) findViewById(R.id.chooseuse));
                final Button wrn=(Button)layoutswrong.findViewById(R.id.chooseCitizen);
                final Button wrn1=(Button)layoutswrong.findViewById(R.id.chooseKanjo);

                wrn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(getApplicationContext(),BrewerLogIn.class);
                        startActivity(i);
                        finish();

                    }
                });

                wrn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(getApplicationContext(),PublicViewBrews.class);
                        startActivity(i);
                        finish();


                    }
                });



                final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(layoutswrong);
                builder3.setTitle("Use Tell Brewster as?");


                final AlertDialog lDialog11 = builder3.create();
                return lDialog11;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
