package archon.tp_yarr_app.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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


import archon.tp_yarr_app.Fragments.CommentsFragment;
import archon.tp_yarr_app.Fragments.SubredditFragment;
import archon.tp_yarr_app.Fragments.ThreadsFragment;
import archon.tp_yarr_app.R;
import archon.tp_yarr_app.RedditService;

public class MainActivity extends AppCompatActivity implements SubredditFragment.OnFragmentInteractionListener {

    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected NavigationView mNavigationView;

    protected String mode;

    protected static final String MODE_SUBREDDITS = "SUBREDDITS";
    protected static final String MODE_THREADS = "THREADS";
    protected static final String MODE_COMMENTS = "COMMENTS";

    protected SubredditFragment subredditFragment;
    protected ThreadsFragment threadsFragment;
    protected CommentsFragment commentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDrawer();

        if (savedInstanceState == null) {
            switchToMain();
        }
    }

    protected void switchToMain() {
        subredditFragment = new SubredditFragment();
        subredditFragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.list_container, subredditFragment).commit();
    }

    protected void switchToThreads(String subreddit) {

    }

    protected void switchToComments(String thread) {

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

    private void loadSubreddits() {

        Intent intent = new Intent(this, RedditService.class);
        startService(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (subredditFragment != null)
                subredditFragment.setList(bundle.getStringArray("subs"));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(RedditService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
