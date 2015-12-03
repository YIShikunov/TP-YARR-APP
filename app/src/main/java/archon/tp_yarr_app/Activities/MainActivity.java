package archon.tp_yarr_app.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import archon.tp_yarr_app.OAuth;
import archon.tp_yarr_app.R;
import archon.tp_yarr_app.RedditController;
import archon.tp_yarr_app.RedditService;

public class MainActivity extends NavigationDrawerActivity implements SubredditFragment.OnFragmentInteractionListener {

    protected String mode;

    protected static final String MODE_SUBREDDITS = "SUBREDDITS";
    protected static final String MODE_THREADS = "THREADS";
    protected static final String MODE_COMMENTS = "COMMENTS";

    protected SubredditFragment subredditFragment;
    protected ThreadsFragment threadsFragment;
    protected CommentsFragment commentsFragment;

    protected String[] subreddits;
    protected String[] threads;
    // comments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.setUpDrawer();

        if (savedInstanceState == null) {
            switchToMain();
        }
    }

    protected void switchToMain() {
        subredditFragment = new SubredditFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_container, subredditFragment);
        transaction.commit();
        mode = MODE_SUBREDDITS;
        Intent intent = new Intent(this, RedditController.class);
        intent.putExtra(RedditController.TYPE, RedditController.HELP_OPENED_SUBREDDITS);
        startService(intent);
    }

    protected void switchToThreads() {
        threadsFragment = new ThreadsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("subs");
        transaction.replace(R.id.list_container, threadsFragment, "subs");
        transaction.commit();
        mode = MODE_THREADS;
        Intent intent = new Intent(this, RedditController.class);
        intent.putExtra(RedditController.TYPE, RedditController.HELP_OPENED_THREAD);
        startService(intent);
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

    @Override
    protected void openMainScreen() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        switchToMain();
    }

    public void onFragmentInteraction(int id) {
        if (mode.equals(MODE_SUBREDDITS)) {
            Intent intent = new Intent(this, RedditController.class);
            intent.putExtra(RedditController.TYPE, RedditController.HELP_CLICKED_SUBREDDIT);
            intent.putExtra(RedditController.ID, id);
            startService(intent);
            switchToThreads();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            switch (intent.getStringExtra(RedditController.TYPE)) {
                case RedditController.SET_SUBREDDITS:
                    setSubreddits(bundle);
                    break;
                case RedditController.SET_THREADS:
                    setThreads(bundle);
                    break;
                default:
                    break;
            }
        }
    };

    private void setSubreddits(Bundle bundle) {
        subreddits = bundle.getStringArray(RedditController.RESULT);
        subredditFragment.setList(bundle.getStringArray(RedditController.RESULT));
    }

    private void setThreads(Bundle bundle) {
        threads = bundle.getStringArray(RedditController.RESULT);
        //threadsFragment.setList(bundle.getStringArray(RedditController.RESULT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(RedditController.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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

    protected void updateFragments() {
        switch (mode) {
            case MODE_SUBREDDITS:
                subredditFragment.setList(subreddits);
                break;
            case MODE_THREADS:
                //threadsFragment.setList(threads);
                break;
        }
    }
}
