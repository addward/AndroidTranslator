package translator.addward.com.yandextranslator;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Yandex {
    public static String YANDEX = "\nПереведено сервисом «Яндекс.Переводчик» \nhttp://translate.yandex.ru";
    public static String EMPTY_REQUEST = "";
    public static int CODE_EMPTY_REQEST = 333;
    public static int CODE_WRONG_KEY = 401;
    public static int CODE_BLOCKED_KEY = 402;
    public static int CODE_LIMIT_KEY = 404;
    public static int CODE_TOO_LONG_TEXT = 413;
    public static int CODE_UNABLE_TO_TRANSLATE = 422;
    public static int CODE_UNSUPPORTED_DIRECTION = 501;
    private YandexDictionary dictionary;

    Yandex() {
        dictionary = new YandexDictionary();
    }

    public String[] translate(String lang, String text) {
        String urlTranslator = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170317T101301Z.7d82235f8892b675.e3ca99fc554300b9ae453281293835f60b0a78c8";

        String translatedText = EMPTY_REQUEST;
        String dictionaryText = null;

        if (text.equals(""))
            return new String[]{EMPTY_REQUEST, null, String.valueOf(R.string.empty_request)};

        try {
            URL urlTra = new URL(urlTranslator);

            HttpsURLConnection connection = (HttpsURLConnection) urlTra.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + lang);
            InputStream response;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                response = connection.getInputStream();
            else {
                response = connection.getErrorStream();
                String line = new java.util.Scanner(response).nextLine();
                String message = String.valueOf(JsonParser.getCodeFromJson(line));
                throw new Exception(message);
            }

            String json = new java.util.Scanner(response).nextLine();

            translatedText = JsonParser.getText(json);

            /*проверка количества слов. Если оно равно 1 то вызываем СЛОВАРЬ*/
            StringTokenizer st = new StringTokenizer(translatedText);
            int words = st.countTokens();
            if (words == 1) {
                String finLang = JsonParser.getLanguages(json)[1];
                String result = dictionary.findInDictionary(translatedText, finLang);
                dictionaryText = result;
            }
            return new String[]{translatedText, dictionaryText, String.valueOf(0)};


        } catch (UnknownHostException ex) {
            return new String[]{translatedText, dictionaryText, String.valueOf(R.string.internet_connection_error)};
        } catch (Exception ex) {
            String error = ex.getMessage();
            ex.printStackTrace();
            return new String[]{translatedText, dictionaryText, String.valueOf(getMessageWithCode(Integer.parseInt(error)))};
        }
    }

    private static int getMessageWithCode(int code) {
        if (code == CODE_BLOCKED_KEY) return R.string.blocked_key_error;
        if (code == CODE_LIMIT_KEY) return R.string.limit_key_error;
        if (code == CODE_TOO_LONG_TEXT) return R.string.too_long_text_error;
        if (code == CODE_UNABLE_TO_TRANSLATE) return R.string.unable_to_translate_error;
        if (code == CODE_UNSUPPORTED_DIRECTION) return R.string.unsupported_direction_error;
        if (code == CODE_WRONG_KEY) return R.string.wrong_key_error;
        if (code == CODE_EMPTY_REQEST) return R.string.empty_request;
        else return R.string.other_error;
    }
}
