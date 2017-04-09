package com.socializent.application.socializent;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.socializent.application.socializent.Fragments.NavigationDrawerFirst;
import com.socializent.application.socializent.Fragments.BottomBarMap;
import com.socializent.application.socializent.Fragments.BottomBarChat;
import com.socializent.application.socializent.Fragments.BottomBarRecommend;
import com.socializent.application.socializent.Fragments.BottomBarNotifications;
import com.socializent.application.socializent.Fragments.NavigationDrawerSecond;
import com.socializent.application.socializent.Fragments.NavigationDrawerThird;
import com.socializent.application.socializent.Fragments.NavigationDrawerFourth;

/**
 * Created by Toshıba on 3/11/2017.
 */

public class Template extends AppCompatActivity {
    private String[] mOptionMenu;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRelativeLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment openFragment = null;
    private CharSequence mTitleSection;
    private Fragment mFragment = null;

    //profil fotosu için
    private NavigationView navigationView;
    private View navHeader;
    public ImageView imgNavHeaderBg, imgProfile;
    public TextView txtName, txtWebsite;


    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template);

        mOptionMenu = new String[] { getString(R.string.first_fragment),
                getString(R.string.second_fragment),
                getString(R.string.third_fragment),
                getString(R.string.fourth_fragment)};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.list_view_drawer);

        //profil fotoğrafı için
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        //loadNavHeader();

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
                        mFragment = new NavigationDrawerFirst();
                        break;
                    case 1:
                        mFragment = new NavigationDrawerSecond();
                        break;
                    case 2:
                        mFragment = new NavigationDrawerThird();
                        break;
                    case 3:
                        mFragment = new NavigationDrawerFourth();
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
                ActivityCompat.invalidateOptionsMenu(Template.this);
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                ActivityCompat.invalidateOptionsMenu(Template.this);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    //*******
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            //zülal: map buttonuna e basınca frame burda oluşturuluyor
                            //fragmentsin içindeki BottomBarMap senini için :D
                            case R.id.action_item1:
                                selectedFragment = BottomBarMap.newInstance();
                                getSupportActionBar().setTitle("Event Map");
                                break;
                            case R.id.action_item2:
                                selectedFragment = BottomBarRecommend.newInstance();
                                getSupportActionBar().setTitle("Recommended For You");
                                break;
                            case R.id.action_item3:
                                selectedFragment = BottomBarChat.newInstance();
                                getSupportActionBar().setTitle("Chat");
                                break;
                            case R.id.action_item4:
                                selectedFragment = BottomBarNotifications.newInstance();
                                getSupportActionBar().setTitle("Notifications");
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_frame, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, BottomBarMap.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

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

        //mTitleSection =getString(R.string.first_fragment);
        getSupportActionBar().setTitle("Socializent");
        //mFragment = new NavigationDrawerFirst();
        openFragment = new BottomBarMap();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, openFragment).commit(); //eskiden mFragment vardı
    }

}
