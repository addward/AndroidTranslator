package translator.addward.com.yandextranslator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDatabase {
    static final String DB_NAME = "historydatabase"; // Имя базы данных
    static final int DB_VERSION = 1; // Версия базы данных
    static final String DB_TABLE = "HISTORY"; // Имя таблицы
    static final String DB_ICOLUMN = "INITIAL_TEXT"; //Колонка исходного текста
    static final String DB_OCOLUMN = "FINAL_TEXT"; //Колонка перевода
    static final String DB_FAVCOLUMN = "FAVORITE"; //Колонка нахождения перевода в избранном
    static final String DB_LANGCOLUMN = "LANGUAGES"; //Колонка языков перевода
    static final String DB_INHISTORY = "INHISTORY"; //Колонка нахождения перевода в истории

    private HistoryDatabaseHelper helper;
    private SQLiteDatabase database;
    private Context context;

    HistoryDatabase(Context context) {
        this.context = context;
    }

    public void open() {
        helper = new HistoryDatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public Cursor getAllData(int mode) {
        if (database != null) {
            if (mode == 0)
                /*История*/
                return database.query(DB_TABLE, null, DB_INHISTORY + " = ?", new String[]{"1"}, null, null, null);
            else
                /*Избранное*/
                return database.query(DB_TABLE, null, DB_FAVCOLUMN + " = ?", new String[]{"1"}, null, null, null);
        } else return null;
    }

    public void close() {
        database.close();
        helper.close();
    }

    public void insertElement(String iText, String oText, String languages, int favorite) {
        if (database != null) helper.insertTranslation(database, iText, oText, languages, favorite);
    }

    public void removeElementWithId(int id) {
        if (database != null)
            database.delete(DB_TABLE, "_id = " + id + " AND " + DB_INHISTORY + " = 0 AND " + DB_FAVCOLUMN + " = 0", null);
    }

    public void removeElementWithIdFromHistory(int id) {
        if (database != null) {
            ContentValues content = new ContentValues();
            content.put(DB_INHISTORY, String.valueOf("0"));
            database.update(DB_TABLE, content, "_id = " + id, null);
            removeElementWithId(id);
        }
    }

    public void setElementFavorite(int id, int favorite) {
        if (database != null) {
            ContentValues content = new ContentValues();
            content.put(DB_FAVCOLUMN, favorite);
            database.update(DB_TABLE, content, "_id = " + id, null);
            if (favorite == 0) removeElementWithId(id);
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
                    + DB_FAVCOLUMN + " INTEGER,"
                    + DB_INHISTORY + " INTEGER" + ");");
            /*Добавление значений в таблицу для проверки работы БД*/
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
            contentValues.put(DB_INHISTORY, String.valueOf("1"));
            db.insert(DB_TABLE, null, contentValues);
        }
    }
}
