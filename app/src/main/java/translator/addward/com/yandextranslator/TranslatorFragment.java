package translator.addward.com.yandextranslator;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Text;

import translator.addward.com.yandextranslator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslatorFragment extends Fragment {

    static String result = "";

    public TranslatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translator, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        final Activity mainActivity = getActivity();
        mainActivity.findViewById(R.id.translate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslationInBackground translationInBackground = new TranslationInBackground(mainActivity);
                translationInBackground.execute();
            }
        });
    }
}
