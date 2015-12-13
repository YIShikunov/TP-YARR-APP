package archon.tp_yarr_app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import archon.tp_yarr_app.RedditController;

public class LoginActivity extends AppCompatActivity {

    public boolean loginError = false;
    private boolean setUp = false;

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

        Credentials creds = OAuth.getCredentials(this);
        setUp = true;
        try {
           new UserChallengeTask(creds, this).execute(req);
        } catch (IllegalStateException | IllegalArgumentException a) {
            loginError = true;
        }

    }

    private void redirect() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginError)
            Toast.makeText(this, "Login not finished.", Toast.LENGTH_LONG).show();
        if (loginError || setUp) {
            redirect();
        }
    }

    private static final class UserChallengeTask extends AsyncTask<String, Void, OAuthData> {
        private Credentials creds;
        private Context context;

        public UserChallengeTask(Credentials creds, Context context) {
            this.creds = creds;
            this.context = context;
        }

        @Override
        protected OAuthData doInBackground(String... params) {
            try {
                HttpRequest request = HttpRequest.from("irrelevant", JrawUtils.newUrl(params[0]));
                Map<String, String> query = JrawUtils.parseUrlEncoded(request.getUrl().getQuery());
                if (!query.containsKey("state"))
                    throw new IllegalArgumentException("Final redirect URI did not contain the 'state' query parameter");
                if (query.containsKey("error"))
                    throw new OAuthException(query.get("error"));
                if (!query.containsKey("code"))
                    throw new IllegalArgumentException("Final redirect URI did not contain the 'code' parameter");

                String code = query.get("code");
                RedditClient reddit = new RedditClient(OAuth.getUserAgent(context));
                HttpRequest.Builder r = reddit.request()
                        .https(true)
                        .host(RedditClient.HOST_SPECIAL)
                        .path("/api/v1/access_token")
                        .expected(MediaType.ANY_TYPE)
                        .post(JrawUtils.mapOf(
                                "grant_type", "authorization_code",
                                "code", code,
                                "redirect_uri", context.getString(R.string.RED_URI)
                        )).basicAuth(new BasicAuthData(creds.getClientId(), creds.getClientSecret()));
                RestResponse response = reddit.execute(r.build());
                OAuthData data = new OAuthData(creds.getAuthenticationMethod(), response.getJson());
                recordAuthData(data.getRefreshToken());
            } catch (NetworkException | OAuthException | IllegalArgumentException e) {
                return null;
            }
            return null;
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
