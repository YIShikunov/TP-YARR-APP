package archon.tp_yarr_app;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import archon.tp_yarr_app.Database.RedditDAO;
import archon.tp_yarr_app.Database.SubredditItem;

public class RedditController extends IntentService {

    public static final String NOTIFICATION = "com.archon.android.controller.receiver";

    public RedditController() {
        super("RedditController");
    }

    public static final String TYPE = "TYPE";
    public static final String ID   = "ID";

    public static final String HELP_OPENED_SUBREDDITS = "q"; // no extra params
    public static final String HELP_CLICKED_SUBREDDIT = "w"; // int: position
    public static final String HELP_OPENED_THREAD     = "e"; // no extra params
    public static final String HELP_CLICKED_THREAD    = "r"; // int: position
    public static final String HELP_ASKED_FOR_REFRESH = "t"; // no extra params

    @Override
    protected void onHandleIntent(Intent intent) {
        String type = intent.getStringExtra(TYPE);
        switch (type) {
            case HELP_OPENED_SUBREDDITS:
                loadSubreddits();
                break;
            case HELP_CLICKED_SUBREDDIT:
                clickSubreddit(intent.getIntExtra(ID, 0));
                break;
            case HELP_OPENED_THREAD:
                loadCurrentThread();
                break;
            case HELP_CLICKED_THREAD:
                clickThread(intent.getIntExtra(ID, 0));
                break;
            case HELP_ASKED_FOR_REFRESH:
                refresh();
                break;
            default:
                raiseError();
                break;
        }
    }

    public static final String RESULT = "RESULT";
    public static final String SET_SUBREDDITS = "a";
    public static final String SET_THREADS = "s";

    private void loadSubreddits() {
        RedditDAO dao = new RedditDAO(getApplication());
        try {
            dao.open();
            ArrayList<SubredditItem> subreddits = dao.getUserSubreddits();
            ArrayList<String> values = new ArrayList<>();
            for (SubredditItem s : subreddits) {
                values.add(s.toString());
            }
            sendSubreddits(values);
        } catch (SQLException a) {
            Toast.makeText(getApplication(), "SQL database", Toast.LENGTH_SHORT).show();
        }
        dao.close();
        sendSubreddits(RedditAPI.loadSubreddits(getApplication()));
    }

    private void sendSubreddits(ArrayList<String> values) {
        if (values == null)
            return;
        Intent intent = new Intent(NOTIFICATION);
        Bundle bundle = new Bundle();
        bundle.putStringArray(RESULT, values.toArray(new String[values.size()]));
        intent.putExtras(bundle);
        intent.putExtra(TYPE, SET_SUBREDDITS);
        sendBroadcast(intent);
    }
    private void clickSubreddit(int id) {

    }
    private void loadCurrentThread() {

    }
    private void clickThread(int id) {

    }
    private void refresh() {

    }
    private void raiseError() {

    }
}
