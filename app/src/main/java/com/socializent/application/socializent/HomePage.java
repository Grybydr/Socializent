package com.socializent.application.socializent;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.socializent.application.socializent.fragments.FirstFragment;
import com.socializent.application.socializent.fragments.SecondFragment;
import com.socializent.application.socializent.fragments.ThirdFragment;

/**
 * Created by Toshıba on 3/11/2017.
 */

public class HomePage extends AppCompatActivity {
    private String[] mOptionMenu;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelativeLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mTitleSection;
    private Fragment mFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.ustmenubutonsuz));

        mOptionMenu = new String[] { getString(R.string.first_fragment),
                getString(R.string.second_fragment),
                getString(R.string.third_fragment) };
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.list_view_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<>(getSupportActionBar()
                .getThemedContext(), android.R.layout.simple_list_item_1,
                mOptionMenu));
        initContentWithFirstFragment();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        mFragment = new FirstFragment();
                        break;
                    case 1:
                        mFragment = new SecondFragment();
                        break;
                    case 2:
                        mFragment = new ThirdFragment();
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mFragment).commit();

                mDrawerList.setItemChecked(position, true);

                mTitleSection = mOptionMenu[position];
                getSupportActionBar().setTitle(mTitleSection);

                mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
            }
        });
        mDrawerList.setItemChecked(0, true);
        mTitleSection = getString(R.string.first_fragment);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitleSection);
                ActivityCompat.invalidateOptionsMenu(HomePage.this);
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                ActivityCompat.invalidateOptionsMenu(HomePage.this);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, R.string.action_settings, Toast.LENGTH_SHORT)
                        .show();
                ;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void initContentWithFirstFragment(){

        mTitleSection =getString(R.string.first_fragment);
        getSupportActionBar().setTitle(mTitleSection);
        mFragment = new FirstFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment).commit();
    }


}
