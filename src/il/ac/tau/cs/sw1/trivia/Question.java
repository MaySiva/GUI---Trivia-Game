package il.ac.tau.cs.sw1.trivia;

import java.util.List;

public class Question {
    private String question;
    private String correctAnswer;
    private List<String> listOfAnswers;




    public Question(String question,String correctAnswer,List<String>listOfAnswers)
    {
        this.question=question;
        this.correctAnswer=correctAnswer;
        this.listOfAnswers=listOfAnswers;

    }

    public String getQuestion()
    {
        return this.question;
    }

    public String getCorrectAnswer()
    {
        return this.correctAnswer;
    }

    public List<String> getListOfAnswers()
    {
        return this.listOfAnswers;
    }

}
