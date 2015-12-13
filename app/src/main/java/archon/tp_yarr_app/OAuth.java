package archon.tp_yarr_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

public class OAuth {

    public static void initiateLogin(Context context) {
        Intent browserIntent;
        browserIntent = new Intent(Intent.ACTION_VIEW, buildUri(context));
        context.startActivity(browserIntent);
    }

    public static Uri buildUri(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getString(R.string.URL));
        stringBuilder.append(context.getString(R.string.client_id));
        stringBuilder.append(context.getString(R.string.CLIENT_ID));
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.response_type));
        stringBuilder.append(context.getString(R.string.TYPE));
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.state));
        stringBuilder.append(generateToken());
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.redirect_uri));
        stringBuilder.append(context.getString(R.string.RED_URI));
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.duration));
        stringBuilder.append(context.getString(R.string.DURATION));
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.scope));
        stringBuilder.append(context.getString(R.string.SCOPE));
        return Uri.parse(stringBuilder.toString());
    }

    private static String generateToken() {
        return "TP_YARR_TOKEN";
    }

    public static Credentials getCredentials(Context context) {
        return Credentials.installedApp(context.getString(R.string.CLIENT_ID), context.getString(R.string.FAKE_URI));
    }

    public static Credentials getUserlessCredentials(Context context) {
        return Credentials.userlessApp(context.getString(R.string.CLIENT_ID), UUID.randomUUID());
    }
}
