package archon.tp_yarr_app.Database;

import android.content.Context;
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
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<SubredditItem> getUserSubreddits() {
        return new ArrayList<SubredditItem>();
    }
}
