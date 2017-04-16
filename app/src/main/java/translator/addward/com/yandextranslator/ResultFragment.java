package translator.addward.com.yandextranslator;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import translator.addward.com.yandextranslator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {


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
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        //Основная активность
        final Activity activity = getActivity();
        //Кнопка копирования текста
        Button copyButton = (Button) activity.findViewById(R.id.copy_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Запись переведенного текста в буфер обмена
                TextView finalText = (TextView) activity.findViewById(R.id.final_text);
                String text = finalText.getText().toString();
                text = text.subSequence(0,text.length()-Yandex.yandex.length()).toString();
                ClipboardManager clipBoard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("",text);
                clipBoard.setPrimaryClip(clip);
                //Вывод тоста о скопированном тексте
                String messageCopied = activity.getString(R.string.message_copied);
                Toast toast = Toast.makeText(activity,messageCopied,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
