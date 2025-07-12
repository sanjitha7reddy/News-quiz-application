import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFetcher {
    private static final String API_KEY = "936a1e3951354e7ba40995fc1bf6798b";
    private static final String API_URL = "https://newsapi.org/v2/everything?q=India&apiKey=" + API_KEY;

    public static List<String> fetchHeadlines() {
        List<String> headlines = new ArrayList<>();

        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("🔍 API Response: " + response.toString()); // helpful debug

            JSONObject json = new JSONObject(response.toString());
            JSONArray articles = json.getJSONArray("articles");

            int max = Math.min(articles.length(), 5);
            for (int i = 0; i < max; i++) {
                JSONObject article = articles.getJSONObject(i);
                if (article.has("title")) {
                    headlines.add(article.getString("title"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return headlines;
    }
}
