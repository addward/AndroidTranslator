package translator.addward.com.yandextranslator;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultFragment extends Fragment {
    HistoryDatabase database;
    private int favPosition;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        database = new HistoryDatabase(getActivity().getApplicationContext());
        super.onActivityCreated(bundle);
        //Основная активность
        final Activity activity = getActivity();
        //Кнопка копирования текста
        Button copyButton = (Button) activity.findViewById(R.id.copy_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity().getApplicationContext();
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click_animation));
                //Запись переведенного текста в буфер обмена
                TextView finalText = (TextView) activity.findViewById(R.id.final_text);
                String text = finalText.getText().toString();
                if (text.length() > Yandex.YANDEX.length())
                    text = text.subSequence(0, text.length() - Yandex.YANDEX.length()).toString();
                ClipboardManager clipBoard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", text);
                clipBoard.setPrimaryClip(clip);
                //Вывод тоста о скопированном тексте
                String messageCopied = activity.getString(R.string.message_copied);
                Toast toast = Toast.makeText(activity, messageCopied, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        final Button favoriteButton = (Button) activity.findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity().getApplicationContext();
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click_animation));
                TextView finalText = (TextView) getActivity().findViewById(R.id.final_text);
                String finalTextStr = String.valueOf(finalText.getText());
                if (!finalTextStr.equals("")) {
                    setFavoriteButton(1-favPosition);
                    TranslationInBackground translation = new TranslationInBackground(getActivity(), 2 + favPosition, null);
                    translation.execute();
                }
            }
        });
    }

    public void setFavoriteButton(int favPosition) {
        this.favPosition = favPosition;
        Button button = (Button) this.getActivity().findViewById(R.id.favorite_button);

        int unFavId = R.drawable.ic_favorite_border_black_48dp;
        int favId = R.drawable.ic_favorite_black_48dp;
        int result = 0 == favPosition ? unFavId : favId;
        button.setBackgroundResource(result);
    }
}
