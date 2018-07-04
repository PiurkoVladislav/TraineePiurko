package trainee_piurko.prospektdev.com.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import trainee_piurko.prospektdev.com.AppItem;

public class AppCursorWrapper extends CursorWrapper {

    public AppCursorWrapper(Cursor cursor) {
        super(cursor);
    }

        public AppItem getItem(){
        String id = getString(getColumnIndex(AppDBSchema.PhotoTable.Cols.ID));
        String title = getString(getColumnIndex(AppDBSchema.PhotoTable.Cols.TITLE));
        String url = getString(getColumnIndex(AppDBSchema.PhotoTable.Cols.URL));
        String dirUrl = getString(getColumnIndex(AppDBSchema.PhotoTable.Cols.DIR_URL));


        AppItem appItem = new AppItem();

        appItem.setUrl(url);
        appItem.setId(id);
        appItem.setCaption(title);
        appItem.setDirUrl(dirUrl);

        return appItem;
    }
}
