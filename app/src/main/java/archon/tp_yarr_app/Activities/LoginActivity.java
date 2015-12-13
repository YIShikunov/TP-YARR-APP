package archon.tp_yarr_app.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.common.net.MediaType;

import net.dean.jraw.JrawUtils;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.BasicAuthData;
import net.dean.jraw.http.HttpRequest;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.RestResponse;
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

        Log.e("1", intent.getData().toString());

        String req = intent.getData().toString().replaceFirst("tpyarr", "http");
        //req = req.replace("#", "?");
        RedditClient client = new RedditClient(UserAgent.of(getString(R.string.user_agent)));
        OAuthHelper helper = client.getOAuthHelper();
        Credentials creds = OAuth.getCredentials(this);
        //String url = helper.getAuthorizationUrl(creds, true).toString();
/*
        Pattern pattern = Pattern.compile("^.*state=([a-zA-Z0-9]+)&.*$");
        Matcher matcher = pattern.matcher(url);
        Log.e("1", matcher.find()?"match":"no match");
        Log.e("1", String.valueOf(matcher.groupCount()));
        String state = matcher.group(1);
        Log.e("1", state);
        req = req.replaceAll("TP_YARR_TOKEN", state);
        Log.e("1", req);*/

        OAuthData data;
        try {
           new UserChallengeTask(helper, creds, getApplication()).execute(req);
        } catch (IllegalStateException a) {
            redirect();
            return;
        }
        redirect();
    }

    private void redirect() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private static final class UserChallengeTask extends AsyncTask<String, Void, OAuthData> {
        private OAuthHelper helper;
        private Credentials creds;
        private Context context;

        public UserChallengeTask(OAuthHelper helper, Credentials creds, Context context) {
            this.helper = helper;
            this.creds = creds;
            this.context = context;
        }

        @Override
        protected OAuthData doInBackground(String... params) {
            try {
                //helper.onUserChallenge(params[0], creds);
                HttpRequest request = HttpRequest.from("irrelevant", JrawUtils.newUrl(params[0]));
                Log.e("1", request.toString());
                Map<String, String> query = JrawUtils.parseUrlEncoded(request.getUrl().getQuery());
                Log.e("1", query.toString());
                if (!query.containsKey("state"))
                    throw new IllegalArgumentException("Final redirect URI did not contain the 'state' query parameter");
                if (query.containsKey("error"))
                    throw new OAuthException(query.get("error"));
                if (!query.containsKey("code"))
                    throw new IllegalArgumentException("Final redirect URI did not contain the 'code' parameter");

                String code = query.get("code");
                Log.e("1", code);
                RedditClient reddit = new RedditClient(UserAgent.of("android:archon.tp_yarr_app:v0.0.1 (by /u/thetimujin)"));
                HttpRequest.Builder r = reddit.request()
                        .https(true)
                        .host(RedditClient.HOST_SPECIAL)
                        .path("/api/v1/access_token")
                        .expected(MediaType.ANY_TYPE)
                        .post(JrawUtils.mapOf(
                                "grant_type", "authorization_code",
                                "code", code,
                                "redirect_uri", "tpyarr://auth"
                        )).header("Content-Type", "application/x-www-form-urlencoded").
                                basicAuth(new BasicAuthData(creds.getClientId(), creds.getClientSecret()));

                RestResponse response = reddit.execute(r.build());
                Log.e("1", response.getJson().toString());
                Log.e("2", response.getRaw());
                OAuthData data = new OAuthData(creds.getAuthenticationMethod(), response.getJson());
                Log.e("3", data.getRefreshToken());
                recordAuthData(data.getRefreshToken());
                return null;
            } catch (NetworkException | OAuthException e) {
                Log.e("2", "error");
                return null;
            }
        }

        public void recordAuthData(String token) {
            RedditDAO dao = new RedditDAO(context);
            try {
                dao.open();
                dao.updateRefreshToken(token);
            } catch (SQLException s) {
                return;
            }
            dao.close();
        }

        @Override
        protected void onPostExecute(OAuthData oAuthData) {

        }
    }
}
