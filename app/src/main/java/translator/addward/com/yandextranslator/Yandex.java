package translator.addward.com.yandextranslator;

/**
 * Created by adddw on 18.03.2017.
 */
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * Created by adddw on 18.03.2017.
 */
public class Yandex {
    public static String yandex = "\nПереведено сервисом «Яндекс.Переводчик» \nhttp://translate.yandex.ru/";

    public static String translate(String lang, String text) {
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170317T101301Z.7d82235f8892b675.e3ca99fc554300b9ae453281293835f60b0a78c8";
        if (text.equals("")) return "Empty request";
        try {
            URL urlObj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + lang);
            InputStream response = connection.getInputStream();

            String json = new java.util.Scanner(response).nextLine();
            System.out.println(json);
            /*выделение переведенного текста из json*/
            int start = json.indexOf("[");
            int end = json.indexOf("]");
            String translated = json.substring(start + 2, end - 1);
            return translated + yandex;
        }
        catch (UnknownHostException ex){
            return "Check your internet connection";
        }
        catch (Exception ex){
            return "Error: " + ex.toString();
        }
    }
}
