import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.*;

public class CohereClient {

    private static final String API_KEY = "T7CeRRSn9h5uFQ7z8bNYl0YnRR0ijhvJLGMYjQZR";  // Replace this
    private static final String API_URL = "https://api.cohere.ai/v1/generate";

    public static String getQuizFromHeadlines(List<String> headlines) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("Create 10 multiple choice questions from these headlines. ");
            prompt.append("Each should have a question, 4 options, and the correct answer index (0-3). Return in JSON array format.\n\n");
            for (String h : headlines) {
                prompt.append("- ").append(h).append("\n");
            }

            JSONObject body = new JSONObject();
            body.put("model", "command");
            body.put("prompt", prompt.toString());
            body.put("max_tokens", 800);
            body.put("temperature", 0.7);

            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBuilder.append(inputLine);
            }
            in.close();

            // 🔍 Print the raw response
            System.out.println("🧾 Raw response: " + responseBuilder.toString());

            JSONObject responseJson = new JSONObject(responseBuilder.toString());

            if (!responseJson.has("generations")) {
                System.out.println("❌ Cohere API response did not contain 'generations'");
                return null;
            }

            String text = responseJson
                .getJSONArray("generations")
                .getJSONObject(0)
                .getString("text");

            return text;

        } catch (Exception e) {
            System.out.println("❌ Cohere API call failed:");
            e.printStackTrace();
            return null;
        }
    }
}
