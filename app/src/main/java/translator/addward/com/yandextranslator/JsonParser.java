package translator.addward.com.yandextranslator;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adddw on 22.04.2017.
 */
public class JsonParser {

    public static String interpretingDictionary(String string) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("def");
            if (jsonArray.length() == 0) return null;
            jsonObject = jsonArray.getJSONObject(0);
            jsonArray = jsonObject.getJSONArray("tr");
            if (jsonArray.length() == 0) return null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject word = jsonArray.getJSONObject(i);
                String text = word.getString("text");
                String pos = word.getString("pos");
                result += "\n" + (i + 1) + ". " + text/* + "(" + pos + ")"*/;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    public static int getCodeFromJson(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            int code = jsonObject.getInt("code");
            return code;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static String getText(String string) {
        int start = string.indexOf("[");
        int end = string.indexOf("]");
        String result = string.substring(start + 2, end - 1);
        return result;
    }

    public static String[] getLanguages(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String result = jsonObject.getString("lang");
            int dashPosition = result.indexOf("-");
            String initialLanguage = result.substring(0, dashPosition);
            String finalLanguage = result.substring(dashPosition + 1, result.length());
            return new String[]{initialLanguage, finalLanguage};
        } catch (Exception ex) {
            ex.printStackTrace();
            return new String[]{""};
        }
    }

    public static ArrayList<String> getLanguagesList(String string) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.getString(i));
            }
            return arrayList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
