package archon.tp_yarr_app.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
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
import archon.tp_yarr_app.Fragments.StateFragment;
import archon.tp_yarr_app.Fragments.SubredditFragment;
import archon.tp_yarr_app.Fragments.ThreadsFragment;
import archon.tp_yarr_app.OAuth;
import archon.tp_yarr_app.R;
import archon.tp_yarr_app.RedditController;
import archon.tp_yarr_app.RedditService;

public class MainActivity extends NavigationDrawerActivity implements SubredditFragment.OnFragmentInteractionListener {

    public StateFragment stateFragment;

    public String mode;

    protected static final String MODE_SUBREDDITS = "SUBREDDITS";
    protected static final String MODE_THREADS = "THREADS";
    protected static final String MODE_COMMENTS = "COMMENTS";

    public SubredditFragment subredditFragment;
    public ThreadsFragment threadsFragment;
    public CommentsFragment commentsFragment;

    protected String[] subreddits;
    protected String[] threads;
    // comments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.setUpDrawer();

        FragmentManager manager = getFragmentManager();
        stateFragment = (StateFragment) manager.findFragmentByTag("state");
        if (stateFragment == null) {
            stateFragment = new StateFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(stateFragment, "state");
            transaction.commit();
        }


        if (savedInstanceState == null) {
            switchToMain();
        }
    }

    protected void switchToMain() {
        stateFragment.subredditFragment = new SubredditFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_container, stateFragment.subredditFragment);
        transaction.commit();
        stateFragment.mode = StateFragment.MODE_SUBREDDITS;
        Intent intent = new Intent(this, RedditController.class);
        intent.putExtra(RedditController.TYPE, RedditController.HELP_OPENED_SUBREDDITS);
        startService(intent);
    }

    protected void switchToThreads() {
        stateFragment.threadsFragment = new ThreadsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("subs");
        transaction.replace(R.id.list_container, stateFragment.threadsFragment, "subs");
        transaction.commit();
        stateFragment.mode = StateFragment.MODE_THREADS;
        Intent intent = new Intent(this, RedditController.class);
        intent.putExtra(RedditController.TYPE, RedditController.HELP_OPENED_THREAD);
        startService(intent);
    }

    protected void switchToComments(String thread) {
        /*if (commentsFragment == null)
            commentsFragment = new CommentsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("thread");
        transaction.replace(R.id.list_container, commentsFragment, "thread");
        transaction.commit();
        mode = MODE_COMMENTS;*/
    }

    @Override
    protected void openMainScreen() {
        // For the navigation drawer button
        super.openMainScreen();
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        switchToMain();
    }

    public void onFragmentInteraction(int id) {
        if (stateFragment.mode.equals(StateFragment.MODE_SUBREDDITS)) {
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
        stateFragment.subreddits = bundle.getStringArray(RedditController.RESULT);
        stateFragment.subredditFragment.setList(bundle.getStringArray(RedditController.RESULT));
    }

    private void setThreads(Bundle bundle) {
        stateFragment.threads = bundle.getStringArray(RedditController.RESULT);
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
            if (stateFragment.mode.equals(StateFragment.MODE_COMMENTS))
                stateFragment.mode = StateFragment.MODE_THREADS;
            else
                stateFragment.mode = StateFragment.MODE_SUBREDDITS;
        } else {
            super.onBackPressed();
        }
    }

    protected void updateFragments() {
        switch (stateFragment.mode) {
            case StateFragment.MODE_SUBREDDITS:
                stateFragment.subredditFragment.setList(subreddits);
                break;
            case StateFragment.MODE_THREADS:
                //threadsFragment.setList(threads);
                break;
        }
    }
}
