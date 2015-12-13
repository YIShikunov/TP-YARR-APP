package archon.tp_yarr_app.Activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.net.URL;

import archon.tp_yarr_app.R;

public class OAuthActivity extends AppCompatActivity {
    private static final String REDIRECT_URL = "http://auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        // Create our RedditClient
        RedditClient reddit = new RedditClient(UserAgent.of("android:archon.tp_yarr_app:v0.0.1 (by /u/thetimujin)"));
        final OAuthHelper helper = reddit.getOAuthHelper();
        // This is Android, so our OAuth app should be an installed app.
        final Credentials credentials = Credentials.installedApp("Ogd-nN_lj6TUGQ", REDIRECT_URL);

        // If this is true, then you will be able to refresh to access token
        boolean permanent = true;
        // OAuth2 scopes to request. See https://www.reddit.com/dev/api/oauth for a full list
        String[] scopes = {"identity", "read"};

        URL authorizationUrl = helper.getAuthorizationUrl(credentials, permanent, scopes);
        // Load the authorization URL into the browser
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(authorizationUrl.toExternalForm());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("code=")) {
                    // We've detected the redirect URL
                    new UserChallengeTask(helper, credentials).execute(url);
                }
            }
        });
    }

    private static final class UserChallengeTask extends AsyncTask<String, Void, OAuthData> {
        private OAuthHelper helper;
        private Credentials creds;
        public UserChallengeTask(OAuthHelper helper, Credentials creds) {
            // <assign fields to parameters>
        }

        @Override
        protected OAuthData doInBackground(String... params) {
            try {
                return helper.onUserChallenge(params[0], creds);
            } catch (NetworkException | OAuthException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(OAuthData oAuthData) {
            //reddit.authenticate(oAuthData);
        }
    }
}