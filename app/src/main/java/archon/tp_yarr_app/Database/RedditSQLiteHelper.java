package archon.tp_yarr_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RedditSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reddit.db";

    // Every time you change something in the schema, increment this
    private static final int DATABASE_VERSION = 3;

    private static final String CREATE_SUBREDDITS =
            "CREATE TABLE subreddits (" +
                    "_id integer primary key autoincrement," +
                    "sname text not null," +
                    "ID36 text not null," +
                    "position integer)";

    private final static String CREATE_OAUTH =
            "CREATE TABLE oauth (" +
                    "_id integer primary key autoincrement," +
                    "access_token text not null," +
                    "token_type text not null,"+
                    "expires_in integer,"+
                    "scope text not null);";

    private final static String CREATE_REFRESH_TOKEN =
            "CREATE TABLE token (_id integer primary key autoincrement, refresh_token text not null);";

    public RedditSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_SUBREDDITS);
        database.execSQL(CREATE_OAUTH);
        database.execSQL(CREATE_REFRESH_TOKEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS subreddits");
        db.execSQL("DROP TABLE IF EXISTS oauth");
        db.execSQL("DROP TABLE IF EXISTS token");
        onCreate(db);
    }

}