package archon.tp_yarr_app.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.dean.jraw.JrawUtils;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.HttpRequest;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.io.BufferedReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import archon.tp_yarr_app.Database.RedditDAO;
import archon.tp_yarr_app.Database.RedditSQLiteHelper;
import archon.tp_yarr_app.OAuth;
import archon.tp_yarr_app.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Intent intent = getIntent();

        String action = intent.getAction();
        if (!action.equals(Intent.ACTION_VIEW)) {
            throw new RuntimeException("Should not happen");
        }

        String req = intent.getData().toString().replaceFirst("tpyarr", "http");
        req = req.replace("#", "?");
        RedditClient client = new RedditClient(UserAgent.of(getString(R.string.user_agent)));
        OAuthHelper helper = client.getOAuthHelper();
        Credentials creds = OAuth.getCredentials(this);
        String url = helper.getAuthorizationUrl(creds, true).toString();

        Pattern pattern = Pattern.compile("state=(.*)&");
        Log.d("1", url);
        Matcher matcher = pattern.matcher(url);
        String state = matcher.group();
        req = req.replaceAll("TP_YARR_TOKEN", state);


        OAuthData data;
        try {
            data = helper.onUserChallenge(req, creds);
        } catch (OAuthException | IllegalStateException a) {
            redirect();
            return;
        }

        RedditDAO dao = new RedditDAO(getApplication());
        try {
            dao.open();
            dao.updateRefreshToken(data.getRefreshToken());
        } catch (SQLException s) {
            redirect();
            return;
        }
        dao.close();
        redirect();
    }

    private void redirect() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
