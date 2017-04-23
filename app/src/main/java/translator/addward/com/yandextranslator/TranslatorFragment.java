package translator.addward.com.yandextranslator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class TranslatorFragment extends Fragment {

    private TranslationInBackground translator;
    public Yandex yandex;

    public TranslatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translator, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        final Activity mainActivity = getActivity();
        Button translateButton = (Button) mainActivity.findViewById(R.id.translate_button);
        EditText initialText = (EditText) mainActivity.findViewById(R.id.initial_text);
        Button deleteButton = (Button) mainActivity.findViewById(R.id.translate_delete_text);
        final TranslatorFragment fragment = this;

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity().getApplicationContext();
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click_animation));

                TranslationInBackground translationInBackground = new TranslationInBackground(mainActivity, 1, fragment);
                translationInBackground.execute();
            }
        });
        initialText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (translator != null) translator.cancel(true);
                translator = new TranslationInBackground(mainActivity, 0, fragment);
                translator.execute();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity().getApplicationContext();
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click_animation));
                Activity activity = getActivity();
                ((EditText) activity.findViewById(R.id.initial_text)).setText("");
            }
        });
    }
}