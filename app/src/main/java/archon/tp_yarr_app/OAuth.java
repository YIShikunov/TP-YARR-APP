package archon.tp_yarr_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OAuth {

    public static void initiateLogin(Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, buildUri(context));
        context.startActivity(browserIntent);
    }

    private static Uri buildUri(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getString(R.string.URL));
        stringBuilder.append(context.getString(R.string.client_id));
        stringBuilder.append(context.getString(R.string.CLIENT_ID));
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.response_type));
        stringBuilder.append(context.getString(R.string.TYPE));
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.state));
        stringBuilder.append(generateRandomToken());
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.redirect_uri));
        stringBuilder.append(context.getString(R.string.RED_URI));
        stringBuilder.append("&");
        stringBuilder.append(context.getString(R.string.scope));
        stringBuilder.append(context.getString(R.string.SCOPE));
        return Uri.parse(stringBuilder.toString());
    }

    private static String generateRandomToken() {
        return "remove"; // Random token provided by watchout4snakes.com random word generator.
        // TODO: learn to make data persistent and make this really random
    }
}
