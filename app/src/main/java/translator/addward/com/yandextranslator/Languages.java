package translator.addward.com.yandextranslator;

import android.view.LayoutInflater;

/**
 * Created by adddw on 08.04.2017.
 */
public class Languages {
    private String name;
    private String shortName;

    private Languages(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public static Languages[] languages = new Languages[]{
            new Languages("Русский", "ru"),
            new Languages("Английский", "en"),
            new Languages("Польский", "pl"),
            new Languages("Датский", "da"),
            new Languages("Китайский", "zh"),
            new Languages("Латынь", "la"),
            new Languages("Шведский", "sv"),
            new Languages("Финский", "fi")
    };

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
