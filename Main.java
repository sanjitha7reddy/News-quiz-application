import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("📰 Welcome to the News Quiz App!\n");

        List<String> headlines = NewsFetcher.fetchHeadlines();
        List<Quiz> quizzes = QuizGenerator.generateFromHeadlines(headlines);

        int score = 0;
        Scanner sc = new Scanner(System.in);

        for (Quiz quiz : quizzes) {
            quiz.display();
            System.out.print("Your answer (1-4): ");
            int choice = sc.nextInt();
            if (quiz.checkAnswer(choice)) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong.");
            }
        }

        System.out.println("\n🎉 Quiz Complete! Your score: " + score + "/" + quizzes.size());
    }
}
