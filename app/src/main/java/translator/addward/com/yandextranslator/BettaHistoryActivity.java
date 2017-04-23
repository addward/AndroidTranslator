package translator.addward.com.yandextranslator;

import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BettaHistoryActivity extends AppCompatActivity {

    private Context context;
    private HistoryDatabase database;
    private Cursor cursor;
    private int mode; //mode=0 - History page, mode=1 - Favorites page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String modeStr = intent.getStringExtra("MODE");
        mode = modeStr.equals("HISTORY") ? 0 : 1;
        String title = mode == 1 ? getString(R.string.favorites_page) : getString(R.string.history_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);

        context = getApplicationContext();
        database = new HistoryDatabase(context);

        setContentView(R.layout.activity_betta_history);
    }

    @Override
    public void onResume() {
        super.onResume();

        database.open();
        cursor = database.getAllData(mode);

        CustomCursorAdapter adapter = new CustomCursorAdapter(context, R.layout.simple_list_element, cursor, 0);
        ListView list = (ListView) findViewById(R.id.history_list);
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    public class CustomCursorAdapter extends ResourceCursorAdapter {
        public CustomCursorAdapter(Context context, int layout, Cursor c, int flags) {
            super(context, layout, c, flags);
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {

            TextView initialText = (TextView) view.findViewById(R.id.element_itext);
            TextView finalText = (TextView) view.findViewById(R.id.element_ftext);
            ImageButton favorite = (ImageButton) view.findViewById(R.id.element_fav_button);
            ImageButton delete = (ImageButton) view.findViewById(R.id.element_delete_button);
            TextView languageText = (TextView) view.findViewById(R.id.element_lang);

            if (mode == 1) {
                ((ViewManager) view).removeView((View) delete);
                delete = null;
            }

            String iTextStr = cursor.getString(cursor.getColumnIndex(HistoryDatabase.DB_ICOLUMN));
            String fTextStr = cursor.getString(cursor.getColumnIndex(HistoryDatabase.DB_OCOLUMN));
            String languageStr = cursor.getString(cursor.getColumnIndex(HistoryDatabase.DB_LANGCOLUMN));
            final int favoriteInt = cursor.getInt(cursor.getColumnIndex(HistoryDatabase.DB_FAVCOLUMN));

            final int unFavId = R.drawable.ic_favorite_border_black_48dp;
            final int favId = R.drawable.ic_favorite_black_48dp;

            final int[] picture = {1 == favoriteInt ? favId : unFavId};
            final int id = cursor.getInt(cursor.getColumnIndex("_id"));

            if (delete != null) delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click_animation));
                    database.removeElementWithIdFromHistory(id);
                    cursorUpdate();
                }
            });

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click_animation));
                    int pictureRes = 1 == favoriteInt ? unFavId : favId;
                    int antiFavorite = 1 == favoriteInt ? 0 : 1;
                    v.setBackgroundResource(pictureRes);
                    favUpdate(id, antiFavorite);
                    cursorUpdate();
                }
            });
            initialText.setText(iTextStr);
            favorite.setBackgroundResource(picture[0]);
            finalText.setText(fTextStr);
            languageText.setText(languageStr);
        }

        public void cursorUpdate() {
            cursor = database.getAllData(mode);
            this.changeCursor(cursor);
        }

        public void favUpdate(int id, int fav) {
            database.setElementFavorite(id, fav);
        }
    }
}
