package translator.addward.com.yandextranslator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonsFragment extends Fragment {

    public ButtonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_buttons, container, false);
        Button changeLanguages = (Button) fragmentView.findViewById(R.id.change_language_button);
        changeLanguages.setOnClickListener(new View.OnClickListener() {
            // Создание объекта OnClickListener для кнопки, меняющей языки местами
            @Override
            public void onClick(View v) {
                Spinner initialSpinner = (Spinner) fragmentView.findViewById(R.id.initial_language_spinner);
                Spinner finalSpinner = (Spinner) fragmentView.findViewById(R.id.final_language_spinner);
                //Текущие положения Spinner'ов
                int start = initialSpinner.getSelectedItemPosition();
                int finial = finalSpinner.getSelectedItemPosition();
                //Смена значений Spinner'ов
                finalSpinner.setSelection(start);
                initialSpinner.setSelection(finial);
            }
        });
        return fragmentView;
    }

}
