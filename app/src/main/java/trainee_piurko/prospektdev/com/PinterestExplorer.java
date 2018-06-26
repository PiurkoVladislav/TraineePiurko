package trainee_piurko.prospektdev.com;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PinterestExplorer {

    private static final String TAG = "PinterestExplorer";
    private static final String API_KEY = "82cbdf469e3db9e644782afe52674830";
    private static final String SECRET_KEY = "78531ccf36ab8adf";

    public String getJSONString(String urlSpec) throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlSpec)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;
    }


    public List<AppItem> feachItems() {
       List<AppItem> appItems = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s,url_n")
                    .build().toString();
            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(appItems,jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Ошибка загрузки", ioe);
        } catch (JSONException joe) {
            Log.e(TAG, "Ошибка парсинга JSON", joe);
        }
        return appItems;
    }

    private void parseItems (List<AppItem> items, JSONObject jsonBody) throws IOException, JSONException{
        JSONObject photoJSONObject = jsonBody.getJSONObject("photos");
        JSONArray photoJSONArray = photoJSONObject.getJSONArray("photo");

        for (int i=0; i < photoJSONArray.length(); i++){
            JSONObject photoJsonObject = photoJSONArray.getJSONObject(i);
            AppItem item = new AppItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if(!photoJsonObject.has("url_s")
//                    ||!photoJsonObject.has("owner_name")
//                    ||!photoJsonObject.has("geo")
//                    ||!photoJsonObject.has("date_taken")
            ){
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            item.setUrlNormal(photoJsonObject.getString("url_n"));
            item.setUserName(photoJsonObject.getString("owner"));
//            item.setLocation(photoJsonObject.getString("geo"));
//            item.setDates(photoJsonObject.getString("date_taken"));

            items.add(item);

        }


    }
}
