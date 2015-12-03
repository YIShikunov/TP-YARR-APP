package archon.tp_yarr_app;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.io.File;
import java.util.Random;

public class RedditService extends IntentService {

    public static final String NOTIFICATION = "com.archon.android.service.receiver";
    public static final String RESULT = "RESULT";
    public static final String CODE = "CODE";
    public static final String TYPE = "TYPE";
    public static final String FRONT_PAGE = "FRONT_PAGE";
    public static final String THREADS = "THREADS";
    public static final String COMMENTS = "COMMENTS";
    public static final String TARGET = "TARGET";

    public RedditService() {
        super("RedditService");
    }

    public int result_code;
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle result = new Bundle();
        String type = intent.getStringExtra(TYPE);
        switch (type) {
            case FRONT_PAGE:
                String[] subreddits = loadFrontPage();
                result.putStringArray(RESULT, subreddits);
                break;
            case THREADS:
                result_code = Activity.RESULT_CANCELED;
                break;
            case COMMENTS:
                result_code = Activity.RESULT_CANCELED;
                break;
            default:
                result_code = Activity.RESULT_CANCELED;
                break;
        }
        publishResults(result, result_code);
    }

    private void publishResults(Bundle result, int code) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtras(result);
        intent.putExtra(CODE, code);
        sendBroadcast(intent);
    }

    private String[] loadFrontPage() {
        return RedditAPI.loadSubreddits();
    }

}
