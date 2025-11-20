/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Dell
 */
public class Quiz {
    private String title;
    private String lessonId;
    private int passingScore;
    private List<Question>questions;
   

    public Quiz(String title,String lessonId, int passingScore, List<Question> questions) {
        this.title=title;
        this.lessonId = lessonId;
        this.passingScore = passingScore;
        this.questions =questions;
    }

    public String getTitle() {
        return title;
    }
    
    public String getLessonId() {
        return lessonId;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public void addQuestion(Question q){
        if(q!=null)
            questions.add(q);
    }
    public int calculateScore(List<Integer>userAnswers){
        if(userAnswers==null || userAnswers.size() !=questions.size())
            return 0;
        int answered = Math.min(userAnswers.size(), questions.size());//to calculate how many questions was answered
        int correct =0;
        for(int i=0;i<answered;i++){
            if(userAnswers.get(i)==questions.get(i).getCorrectIndex())
                correct++;
        }
        return (int) ((correct/(double) questions.size())*100);//to calculate % el egabat el sa7
    }
    public boolean isPassed(int score){
        return score>=passingScore;
    }
}
