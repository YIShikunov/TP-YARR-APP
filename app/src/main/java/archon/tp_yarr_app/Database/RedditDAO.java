package archon.tp_yarr_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

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
        testItem.setSname("testt");
        testItem.setID36("2222222");
        testItem.setPosition(0);
        addSubreddit(testItem);
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

    private SubredditItem parseCursor(Cursor cursor) {
        SubredditItem result = new SubredditItem();
        result.setSname(cursor.getString(1));
        result.setID36(cursor.getString(2));
        result.setPosition(cursor.getInt(3));
        return result;
    }
}
