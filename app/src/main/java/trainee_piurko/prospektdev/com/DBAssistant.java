package trainee_piurko.prospektdev.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import trainee_piurko.prospektdev.com.db.AppBaseHalper;
import trainee_piurko.prospektdev.com.db.AppCursorWrapper;
import trainee_piurko.prospektdev.com.db.AppDBSchema;

public class DBAssistant {

    private static final String TAG = "DBAssistant";

    private static DBAssistant sDBAssistant;
    private Context mContext;
    private SQLiteDatabase mDatabas;
    private AppBaseHalper mAppBaseHalper;


    public static DBAssistant get(Context context){
        if(sDBAssistant == null){
            sDBAssistant = new DBAssistant(context);
        }
        return sDBAssistant;
    }

    private DBAssistant(Context context) {
        mAppBaseHalper = new AppBaseHalper(context);
        mContext = context.getApplicationContext();
        mDatabas = mAppBaseHalper.getWritableDatabase();
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
        Log.i(TAG,"Получение данных из БД");
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

            mDatabas.insert(AppDBSchema.PhotoTable.NAME,null,values);      //первый параметр передает название базы, третий значение, а второй дает возможность создать пустую вставку
        }
        Log.i(TAG,"БД создана");
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
        values.put(AppDBSchema.PhotoTable.Cols.DIR_URL, item.getUrl());


        return values;
    }

    private AppCursorWrapper queryAppItems(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabas.query(
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
