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
        if (subredditFragment == null)
            subredditFragment = new SubredditFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_container, subredditFragment);
        transaction.commit();
        mode = MODE_SUBREDDITS;
        getSubreddits();
    }

    protected void switchToThreads(String subreddit) {
        if (threadsFragment == null)
            threadsFragment = new ThreadsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("subs");
        transaction.replace(R.id.list_container, threadsFragment, "subs");
        transaction.commit();
        mode = MODE_THREADS;
    }

    protected void switchToComments(String thread) {
        if (commentsFragment == null)
            commentsFragment = new CommentsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("thread");
        transaction.replace(R.id.list_container, commentsFragment, "thread");
        transaction.commit();
        mode = MODE_COMMENTS;
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
        switchToMain();
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

    public void onFragmentInteraction(String item) {
        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
        if (mode.equals(MODE_SUBREDDITS)) {
            switchToThreads(item);
        }
    }

    private void getSubreddits() {
        Intent intent = new Intent(this, RedditService.class);
        intent.putExtra(RedditService.TYPE, RedditService.FRONT_PAGE);
        startService(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (subredditFragment != null && mode == MODE_SUBREDDITS)
                subredditFragment.setList(bundle.getStringArray(RedditService.RESULT));
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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
            if (mode.equals(MODE_COMMENTS))
                mode = MODE_THREADS;
            else
                mode = MODE_SUBREDDITS;
        } else {
            super.onBackPressed();
        }
    }
}
