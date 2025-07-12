import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class QuizGenerator {

    public static List<Quiz> generateFromHeadlines(List<String> headlines) {
        List<Quiz> quizzes = new ArrayList<>();

        String gptResponse = CohereClient.getQuizFromHeadlines(headlines);
        if (gptResponse == null || gptResponse.isEmpty()) {
            System.out.println("⚠️ GPT did not return valid quiz questions. Using default.");
            return quizzes;
        }

        try {
            // Extract JSON array from response
            int start = gptResponse.indexOf("[");
            int end = gptResponse.lastIndexOf("]") + 1;

            if (start == -1 || end == -1) {
                System.out.println("⚠️ Could not find JSON array in response.");
                return quizzes;
            }

            String jsonArrayString = gptResponse.substring(start, end);

            JSONArray quizArray = new JSONArray(jsonArrayString);

            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject item = quizArray.getJSONObject(i);
                String question = item.getString("question");
                JSONArray opts = item.getJSONArray("options");
                String[] options = new String[opts.length()];
                for (int j = 0; j < opts.length(); j++) {
                    options[j] = opts.getString(j);
                }

                // Accept either numeric or string answers
                int correctIndex = -1;
                try {
                    correctIndex = item.getInt("answer");
                } catch (JSONException e) {
                    String correct = item.getString("correct_answer");
                    for (int j = 0; j < options.length; j++) {
                        if (options[j].equalsIgnoreCase(correct)) {
                            correctIndex = j;
                            break;
                        }
                    }
                }

                if (correctIndex >= 0 && correctIndex < options.length) {
                    quizzes.add(new Quiz(question, options, correctIndex));
                } else {
                    System.out.println("⚠️ Invalid answer for: " + question);
                }
            }

        } catch (Exception e) {
            System.out.println("Error parsing GPT response:\n" + gptResponse);
            e.printStackTrace();
        }

        return quizzes;
    }
}
