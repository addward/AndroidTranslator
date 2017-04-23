package translator.addward.com.yandextranslator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int idLayout = R.layout.activity_main;
        final Activity activity = this;
        final TranslatorFragment fragment = (TranslatorFragment) (this.getFragmentManager().findFragmentById(R.id.initial_text_fragment));

        setContentView(idLayout);
        getSupportActionBar().show();

        Spinner spiner1 = (Spinner) findViewById(R.id.initial_language_spinner);
        Spinner spiner2 = (Spinner) findViewById(R.id.final_language_spinner);
        String[] languages = new String[Languages.languages.length];
        for (int i = 0; i < Languages.languages.length; i++) {
            languages[i] = Languages.languages[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        spiner1.setAdapter(adapter);
        spiner2.setAdapter(adapter);

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (!isOpen) {
                            TranslationInBackground translationInBackground = new TranslationInBackground(activity, 1, fragment);
                            translationInBackground.execute();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history_button) {
            Intent intent = new Intent(this, BettaHistoryActivity.class);
            intent.putExtra("MODE", "HISTORY");
            startActivity(intent);
        }
        if (item.getItemId() == R.id.favorite_button) {
            Intent intent = new Intent(this, BettaHistoryActivity.class);
            intent.putExtra("MODE", "FAVORITES");
            startActivity(intent);
        }
        return true;
    }
}