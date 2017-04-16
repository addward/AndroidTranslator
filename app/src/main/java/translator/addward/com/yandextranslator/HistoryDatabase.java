package translator.addward.com.yandextranslator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Created by adddw on 13.04.2017.
 */
//TODO Create HistoryDatabase
public class HistoryDatabase extends SQLiteOpenHelper{
    private static final String DB_NAME = "historydatabase"; // Имя базы данных
    private static final int DB_VERSION = 1; // Версия базы данных
    HistoryDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE HISTORY ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ITEXT TEXT, "
                + "FTEXT TEXT, "
                + "FAVORITE INTEGER);");
        insertTranslation(db, "Hello", "Привет", 0);
        insertTranslation(db, "Goodbye", "Пока", 1);
        insertTranslation(db, "Cat", "Кошка", 1);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    private static void insertTranslation(SQLiteDatabase db, String iText, String fText, int favorite){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEXT",iText);
        contentValues.put("FTEXT",fText);
        contentValues.put("FAVORITE",favorite);
        db.insert(DB_NAME, null,contentValues);
    }

    private static void deleteTranslation(SQLiteOpenHelper sb){
        
    }
}
