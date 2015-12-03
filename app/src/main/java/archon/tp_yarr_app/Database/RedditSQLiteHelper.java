package archon.tp_yarr_app.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RedditSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reddit.db";
    private static final int DATABASE_VERSION = 1;

    public RedditSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


    ////////////


}
