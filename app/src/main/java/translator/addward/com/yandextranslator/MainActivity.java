package translator.addward.com.yandextranslator;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 1;

    private PagerAdapter mPagerAdapter;

    static String outputString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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