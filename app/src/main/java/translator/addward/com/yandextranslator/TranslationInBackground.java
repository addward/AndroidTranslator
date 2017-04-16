package translator.addward.com.yandextranslator;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by adddw on 13.04.2017.
 */
public class TranslationInBackground extends AsyncTask<Void, Void, String>{
    private Spinner initialLangSpinner, finalLangSpinner;
    private TextView initialText,finalText;
    private String initialLang,finalLang;
    private String text;

    TranslationInBackground(Activity mainActivity){
        initialLangSpinner = (Spinner) mainActivity.findViewById(R.id.initial_language_spinner);
        finalLangSpinner = (Spinner) mainActivity.findViewById(R.id.final_language_spinner);
        finalText = (TextView) mainActivity.findViewById(R.id.final_text);
        initialText = (TextView) mainActivity.findViewById(R.id.initial_text);
    }

    @Override
    public void onPreExecute(){
        initialLang = Languages.languages[initialLangSpinner.getSelectedItemPosition()].getShortName();
        finalLang = Languages.languages[finalLangSpinner.getSelectedItemPosition()].getShortName();
        text = initialText.getText().toString();
    }

    @Override
    public void onPostExecute(String a){
        finalText.setText(a);
    }

    protected String doInBackground(Void... v) {
        return Yandex.translate(initialLang+"-"+finalLang,text);
    }

    protected void onProgressUpdate(Void progress) {
    }
}
