package com.amusoft.brewster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;


public class MainActivity extends ActionBarActivity {
    MaterialViewPager mViewPager;
    static final int TAKE_AVATAR_CAMERA_REQUEST = 1;
    static final int TAKE_AVATAR_GALLERY_REQUEST = 2;
    static final int AVATAR_DIALOG_ID = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three

        setMaterialPager();


    }

    private void setMaterialPager() {

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        Toolbar toolbar = mViewPager.getToolbar();


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.inflateMenu(R.menu.menu_main);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();

                    //noinspection SimplifiableIfStatement

                    switch (id) {
                        case R.id.action_myprofile:
                            Intent i = new Intent(getApplicationContext(), EditBrewerProfile.class);
                            startActivity(i);
                            break;

                    }
                    return false;
                }
            });


            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);

        }
        ViewPager viewPager = mViewPager.getViewPager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            int oldPosition = 1;

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:

                        return new FragmentPostBrew();
                    case 1:

                        return new FragmentViewBrews();
                    case 2:

                        return new FragmentViewMyBrews();


                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "PostBrew";
                    case 1:
                        return "View all brews";
                    case 2:
                        return "My Brews";

                }
                return "";
            }

            //called when the current page has changed
            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);

                //only if position changed
                if (position == oldPosition)
                    return;
                oldPosition = position;

                int color = 0;

                switch (position) {
                    case 0:
                        color = getResources().getColor(R.color.blue);
                        break;
                    case 1:
                        color = getResources().getColor(R.color.blue);
                        break;
                    case 2:
                        color = getResources().getColor(R.color.blue);
                        break;
                    case 3:
                        color = getResources().getColor(R.color.blue);
                        break;
                }

                final int fadeDuration = 400;


                mViewPager.setColor(color, fadeDuration);

            }

        });
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
