package archon.tp_yarr_app.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import archon.tp_yarr_app.Fragments.SubredditFragment;
import archon.tp_yarr_app.Fragments.ThreadsFragment;
import archon.tp_yarr_app.R;

public class MainActivity extends AppCompatActivity implements SubredditFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDrawer();

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            SubredditFragment details = new SubredditFragment();
            details.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.list_container, details).commit();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setUpDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_main:
                        openMainScreen();
                        return true;
                    case R.id.menu_item_login:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.reddit.com"));
                        startActivity(browserIntent);
                        return true;
                    case R.id.menu_item_settings:
                        openSettings();
                        return true;
                    case R.id.menu_item_help:
                        openHelp();
                        return true;
                    default:
                        return true;
                }
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void openMainScreen() {
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        //finish();
    }

    protected void openHelp() {
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
    }

    protected void openSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    private void openSubreddit() {
        Fragment newFragment = new ThreadsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.list_container, newFragment);

        transaction.commit();
    }

    public void onFragmentInteraction(String id) {
        Toast.makeText(this, "Open sub", Toast.LENGTH_SHORT).show();
        openSubreddit();
    }
}
