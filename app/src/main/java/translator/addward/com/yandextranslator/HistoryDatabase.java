package translator.addward.com.yandextranslator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Created by adddw on 13.04.2017.
 */
//TODO Create HistoryDatabase
public class HistoryDatabase{
    static final String DB_NAME = "historydatabase"; // Имя базы данных
    static final int DB_VERSION = 1; // Версия базы данных
    static final String DB_TABLE = "HISTORY";
    static final String DB_ICOLUMN = "INITIAL_TEXT";
    static final String DB_OCOLUMN = "FINAL_TEXT";
    static final String DB_FAVCOLUMN = "FAVORITE";
    static final String DB_LANGCOLUMN = "LANGUAGEs";

    private HistoryDatabaseHelper helper;
    private SQLiteDatabase database;
    private Context context;

    HistoryDatabase(Context context){
        this.context = context;
    }

    public void open(){
        helper = new HistoryDatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public Cursor getAllData(int mode){
        if (database!=null){
            if (mode == 0) return database.query(DB_TABLE,null,null,null,null,null,null);
            else return database.query(DB_TABLE,null,DB_FAVCOLUMN+ " = ?",new String[] {"1"},null,null,null);
        }
        else return null;
    }

    public void close(){
        database.close();
        helper.close();
    }

    public void insertElement(String iText, String oText, String languages, int favorite){
        if (database!=null) helper.insertTranslation(database,iText,oText,languages,favorite);
    }

    public void removeElementWithId(int id){
        if(database!=null) database.delete(DB_TABLE,"_id = " + id,null);
    }

    public void setElementFavorite(int id, int favorite){
        if(database!=null) {
            ContentValues content = new ContentValues();
            content.put(DB_FAVCOLUMN,favorite);
            database.update(DB_TABLE,content,"_id = " + id, null);
        }
    }
    private class HistoryDatabaseHelper extends SQLiteOpenHelper {

        HistoryDatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DB_TABLE + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DB_ICOLUMN + " TEXT, "
                    + DB_OCOLUMN + " TEXT, "
                    + DB_LANGCOLUMN + " TEXT, "
                    + DB_FAVCOLUMN + " INTEGER);");
            insertTranslation(db, "Hello", "Привет", "en-ru", 0);
            insertTranslation(db, "Goodbye", "Пока", "en-ru", 1);
            insertTranslation(db, "Cat", "Кошка", "en-ru", 1);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        private void insertTranslation(SQLiteDatabase db, String iText, String oText, String languages, int favorite) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DB_ICOLUMN, iText);
            contentValues.put(DB_OCOLUMN, oText);
            contentValues.put(DB_LANGCOLUMN, languages);
            contentValues.put(DB_FAVCOLUMN, favorite);
            db.insert(DB_TABLE, null, contentValues);
        }
    }
}
