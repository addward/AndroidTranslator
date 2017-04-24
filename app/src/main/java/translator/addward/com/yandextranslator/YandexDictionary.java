package translator.addward.com.yandextranslator;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class YandexDictionary {
    /*
    private static int ERR_OK = 200;
    private static int ERR_KEY_INVALID = 401;
    private static int ERR_KEY_BLOCKED = 402;
    private static int ERR_DAILY_REQ_LIMIT_EXCEEDED = 403;
    private static int ERR_TEXT_TOO_LONG = 413;
    private static int ERR_LANG_NOT_SUPPORTED = 501;
    */
    private ArrayList<String> languageList;
    private static String urlDictionaryStr = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20170421T220559Z.27ed2cfbfb591d34.8713611c6c37d3b252236ae1097eff5abbed3135";
    private static String urlGetLanguagesStr = "https://dictionary.yandex.net/api/v1/dicservice.json/getLangs?key=dict.1.1.20170421T220559Z.27ed2cfbfb591d34.8713611c6c37d3b252236ae1097eff5abbed3135";

    YandexDictionary() {
        getLanguages();
    }

    public String findInDictionary(String text, String language) {
        String inputLang = language + "-" + language;

        try {
            if (languageList == null) {
                getLanguages();
            }
            if (languageList == null && !languageList.contains(inputLang)) return null;

            URL urlDictionary = new URL(urlDictionaryStr);
            HttpsURLConnection connection = (HttpsURLConnection) urlDictionary.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("&lang=" + inputLang + "&text=" + URLEncoder.encode(text, "UTF-8"));
            InputStream response = connection.getInputStream();

            String json = new java.util.Scanner(response).nextLine();
            String result = JsonParser.interpretingDictionary(json);

            return result;

        } catch (Exception ex) {
            //Нет смысла выводить пользователю
            return null;
        }
    }

    private ArrayList<String> getLanguages() {
        try {
            URL urlGetLanguages = new URL(urlGetLanguagesStr);
            HttpsURLConnection connection = (HttpsURLConnection) urlGetLanguages.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            InputStream response = connection.getInputStream();

            String json = new java.util.Scanner(response).nextLine();
            languageList = JsonParser.getLanguagesList(json);
            return languageList;
        } catch (Exception ex) {
            languageList = null;
            return null;
        }
    }


}
