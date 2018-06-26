package trainee_piurko.prospektdev.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import trainee_piurko.prospektdev.com.db.AppBaseHalper;
import trainee_piurko.prospektdev.com.db.AppCursorWrapper;
import trainee_piurko.prospektdev.com.db.AppDBSchema;

public class DBAssistant {

    private static DBAssistant sDBAssistant;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static DBAssistant get(Context context){
        if(sDBAssistant == null){
            sDBAssistant = new DBAssistant(context);
        }
        return sDBAssistant;
    }

    private DBAssistant(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AppBaseHalper(mContext)
                .getWritableDatabase();
//        mAppContext = appContext;
    }


    public List<AppItem> getAppItems() {
        List<AppItem> items = new ArrayList<>();

        AppCursorWrapper cursor = queryAppItems(null,null);

        try {                                       //Курсоры всегда устанавливаются в определенную позицию
            cursor.moveToFirst();                   //чтоб извлечь данные переносим его на парвый элемент
            while (!cursor.isAfterLast()){          //пока мы в пределах выбора данных,
                items.add(cursor.getItem());      //считываем данные каждой строки
                cursor.moveToNext();
            }
        }finally {
            cursor.close();                         //ВАЖНО!!!!!
        }
        return items;
    }

    public void addAppItem(List<AppItem> items){
//        for (int i = 0; i < 10;i++ ){
//            ContentValues values = getContentValues(items.get(i));
//
//            mDatabase.insert(AppDBSchema.PhotoTable.NAME,null,values);
//        }
        for (AppItem item: items
             ) {
            ContentValues values = getContentValues(item);

            mDatabase.insert(AppDBSchema.PhotoTable.NAME,null,values);      //первый параметр передает название базы, третий значение, а второй дает возможность создать пустую вставку
        }

    }


    public AppItem getAppItem(String id){
        AppCursorWrapper cursor = queryAppItems(AppDBSchema.PhotoTable.Cols.ID + " = ?", new String[]{id});

        try {
            if (cursor.getCount()==0){
                return null;
            }

            cursor.moveToFirst();                   //чтоб извлечь данные переносим его на парвый элемент
            return cursor.getItem();
        }finally {
            cursor.close();                         //ВАЖНО!!!!!
        }
    }

    private static ContentValues getContentValues(AppItem item){         //Метод заполняет базу данных значениями
        ContentValues values = new ContentValues();
        values.put(AppDBSchema.PhotoTable.Cols.ID, item.getId() );
        values.put(AppDBSchema.PhotoTable.Cols.TITLE, item.getCaption());
        values.put(AppDBSchema.PhotoTable.Cols.URL, item.getUrl());

        return values;
    }

    private AppCursorWrapper queryAppItems(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                AppDBSchema.PhotoTable.NAME,
                null,                //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new AppCursorWrapper(cursor);
    }


}
