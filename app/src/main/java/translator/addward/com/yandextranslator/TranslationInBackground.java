package translator.addward.com.yandextranslator;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TranslationInBackground extends AsyncTask<Void, Void, String[]> {
    private Spinner initialLangSpinner, finalLangSpinner;
    private TextView initialText, finalText;
    private String initialTextStr, finalTextStr;
    private String initialLang, finalLang;
    private String text;
    private int favorite = 0;
    private Context context;
    private int mode;
    private HistoryDatabase database;
    private Yandex yandex;
    private TranslatorFragment fragment;
    private Boolean addInDB;
    private int addToDbFlag = 0;
    private ResultFragment finalFragment;

    //mode = 0 - перевод без добавления в БД, =1,2 - с добавлением
    TranslationInBackground(Activity mainActivity, int mode, TranslatorFragment fragment) {
        if (mode == 1 || mode == 2 || mode == 3) {
            database = new HistoryDatabase(mainActivity.getApplicationContext());
            database.open();
        }
        this.mode = mode;
        context = mainActivity.getApplicationContext();

        initialLangSpinner = (Spinner) mainActivity.findViewById(R.id.initial_language_spinner);
        finalLangSpinner = (Spinner) mainActivity.findViewById(R.id.final_language_spinner);

        finalText = (TextView) mainActivity.findViewById(R.id.final_text);
        initialText = (TextView) mainActivity.findViewById(R.id.initial_text);

        finalTextStr = String.valueOf(finalText.getText());
        initialTextStr = String.valueOf(initialText.getText());

        finalFragment = (ResultFragment) mainActivity.getFragmentManager().findFragmentById(R.id.final_text_fragment);

        if (fragment != null) {
            yandex = fragment.yandex;
            this.fragment = fragment;
        }
    }

    @Override
    public void onPreExecute() {
        initialLang = Languages.languages[initialLangSpinner.getSelectedItemPosition()].getShortName();
        finalLang = Languages.languages[finalLangSpinner.getSelectedItemPosition()].getShortName();
        if (mode < 2) finalFragment.setFavoriteButton(0);
        text = initialText.getText().toString();
    }

    @Override
    public void onPostExecute(String[] a) {
        if ((mode != 2 && mode != 3) || addToDbFlag == 1) {
            String translateResult = a[0];
            String dictionaryResult = "";
            int code = Integer.parseInt(a[2]);

            if (fragment != null) {
                fragment.yandex = this.yandex;
            }

            if (a[1] != null) dictionaryResult = a[1];
            finalText.setText(translateResult + dictionaryResult);

            if ((mode == 1 && !translateResult.equals(Yandex.EMPTY_REQUEST)) || addToDbFlag == 1 ) {

                String textWithoutYandex;

                if (translateResult.length() > Yandex.YANDEX.length()) {
                    textWithoutYandex = translateResult.subSequence(0, translateResult.length() - Yandex.YANDEX.length()).toString();
                } else textWithoutYandex = translateResult;

                database.insertElement(text, textWithoutYandex, initialLang + "-" + finalLang, favorite);
                favorite = 0;
                database.close();
            }

            if (code != 0) {
                Toast.makeText(context, code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected String[] doInBackground(Void... v) {
        if (yandex == null) yandex = new Yandex();
        String[] result = yandex.translate(initialLang + "-" + finalLang, text);
        if (mode == 2 || mode == 3) {
            Cursor cursor = database.getAllData(0);
            cursor.moveToLast();
            if (cursor.getPosition() >= 0) {
                String textDB = cursor.getString(cursor.getColumnIndex(HistoryDatabase.DB_OCOLUMN));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                addInDB = !(textDB.equals(result[0]));
                if (addInDB) {
                    favorite = 1;
                } else {
                    database.setElementFavorite(id, mode - 2);
                    return null;
                }
            } else {
                favorite = mode - 2;
                addToDbFlag = 1;
            }
        }
        return result;
    }
}
