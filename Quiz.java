public class Quiz {
    String question;
    String[] options;
    int correctAnswer; // index (0–3)

    public Quiz(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public void display() {
        System.out.println("\n" + question);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
    }

    public boolean checkAnswer(int choice) {
        return (choice - 1) == correctAnswer;
    }
}
