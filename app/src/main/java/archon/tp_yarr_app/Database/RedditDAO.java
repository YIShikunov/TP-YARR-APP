package archon.tp_yarr_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class RedditDAO {

    private SQLiteDatabase database;
    private RedditSQLiteHelper dbHelper;

    public RedditDAO(Context context) {
        dbHelper = new RedditSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        SubredditItem testItem = new SubredditItem();
        /*testItem.setSname("testt");
        testItem.setID36("2222222");
        testItem.setPosition(0);
        addSubreddit(testItem);*/
    }

    public void close() {
        dbHelper.close();
    }

    public void addSubreddit(SubredditItem subreddit) {
        ContentValues values = new ContentValues();
        values.put("sname", subreddit.getSname());
        values.put("ID36", subreddit.getID36());
        values.put("position", subreddit.getPosition());
        database.insert("subreddits", null,
                values);
    }

    public ArrayList<SubredditItem> getUserSubreddits() {
        ArrayList<SubredditItem> subreddits = new ArrayList<SubredditItem>();
        Cursor cursor = database.rawQuery("SELECT * FROM subreddits;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SubredditItem comment = parseCursor(cursor);
            subreddits.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return subreddits;
    }

    public void updateRefreshToken(String token) {
        removeRefreshToken();
        ContentValues n = new ContentValues();
        n.put("refresh_token", token);
        database.insert("token", null, n);
        Log.e("QWE", getRefreshToken());
    }

    public String getRefreshToken() {
        Cursor cursor = database.rawQuery("SELECT * FROM token;", null);
        if (!cursor.moveToFirst())
            return null;
        return cursor.getString(1);
    }

    public void removeRefreshToken() {
        database.delete("token", "true", null);
    }

    public boolean isLoggedIn() {
        Cursor cursor = database.rawQuery("SELECT * FROM token;", null);
        return cursor.moveToFirst();
    }

    private SubredditItem parseCursor(Cursor cursor) {
        SubredditItem result = new SubredditItem();
        result.setSname(cursor.getString(1));
        result.setID36(cursor.getString(2));
        result.setPosition(cursor.getInt(3));
        return result;
    }

    public Cursor executeQuery(String query) {
        // mostly for testing purposes
        return database.rawQuery(query, null);

    }
}
