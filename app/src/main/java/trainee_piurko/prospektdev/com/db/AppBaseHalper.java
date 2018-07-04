package trainee_piurko.prospektdev.com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppBaseHalper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "photoBase.db";

    public AppBaseHalper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ AppDBSchema.PhotoTable.NAME+"(" +
                " _id integer primary key autoincrement, " +
                AppDBSchema.PhotoTable.Cols.ID + ", " +
                AppDBSchema.PhotoTable.Cols.TITLE + ", " +
                AppDBSchema.PhotoTable.Cols.URL+ ", " +
                AppDBSchema.PhotoTable.Cols.DIR_URL+ ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
