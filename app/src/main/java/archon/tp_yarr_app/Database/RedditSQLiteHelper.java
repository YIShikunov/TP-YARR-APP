package archon.tp_yarr_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RedditSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reddit.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_SUBREDDITS =
            "CREATE TABLE subreddits (" +
                    "_id integer primary key autoincrement," +
                    "sname text not null," +
                    "ID36 text not null," +
                    "position integer)";

    public RedditSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_SUBREDDITS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS subreddits");
        onCreate(db);
    }

}