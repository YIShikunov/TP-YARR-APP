package archon.tp_yarr_app;

import android.content.Context;
import android.database.Cursor;

import net.dean.jraw.RedditClient;

import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.paginators.Paginator;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.UserSubredditsPaginator;

import java.sql.SQLException;
import java.util.ArrayList;

import archon.tp_yarr_app.Database.RedditDAO;
import archon.tp_yarr_app.utils.OAuthDataWrapper;

public class RedditAPI {

    public static ArrayList<String> loadSubreddits(Context context) {

        UserAgent userAgent = OAuth.getUserAgent(context);
        RedditClient redditClient = new RedditClient(userAgent);
        OAuthHelper helper = redditClient.getOAuthHelper();
        RedditDAO dao = new RedditDAO(context);
        OAuthData authData;
        ArrayList<String> subreddits;

        try {
            dao.open();
            if (dao.isLoggedIn()) {
                helper.setRefreshToken(dao.getRefreshToken());
                authData = helper.refreshToken(OAuth.getCredentials(context));
                redditClient.authenticate(authData);
                subreddits = new ArrayList<String>();
                UserSubredditsPaginator paginator = new UserSubredditsPaginator(redditClient, "subscriber");
                Listing<Subreddit> submissions = paginator.next();
                for (Subreddit subreddit : submissions) {
                    subreddits.add(subreddit.getDisplayName());
                }
            } else {
                authData = helper.easyAuth(OAuth.getUserlessCredentials(context));
                redditClient.authenticate(authData);
                subreddits = (ArrayList<String>) redditClient.getTrendingSubreddits();
                Paginator<Subreddit> paginator = new Paginator<Subreddit>(redditClient, Subreddit.class) {
                    @Override
                    protected String getBaseUri() {
                        return "/subreddits/popular";
                    }
                };
                Listing<Subreddit> submissions = paginator.next();
                for (Subreddit subreddit : submissions) {
                    subreddits.add(subreddit.getDisplayName());
                }
            }
        } catch (SQLException | OAuthException s) {
            return null;
        }
        dao.close();
        return subreddits;
    }

    private static OAuthData retrieveOAuthData(Context context) {

        RedditDAO dao = new RedditDAO(context);
        OAuthDataWrapper result = new OAuthDataWrapper();
        try {
            dao.open();
            Cursor cursor = dao.executeQuery("SELECT * FROM oauth;");
            cursor.moveToFirst();
            result.access_token = cursor.getString(1);
            result.token_type = cursor.getString(2);
            result.expires_in = cursor.getInt(3);
            result.scope = cursor.getString(4);
        } catch (SQLException s) {

        }
        dao.close();
        return (OAuthData) result;
    }
}
